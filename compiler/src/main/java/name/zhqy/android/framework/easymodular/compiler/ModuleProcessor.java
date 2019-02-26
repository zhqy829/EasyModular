package name.zhqy.android.framework.easymodular.compiler;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
public class ModuleProcessor extends AbstractProcessor {

    private static final String PACKAGE_NAME = "name.zhqy.android.framework.easymodular.core";
    private static final String IMODULE_INIT_NAME = "IModuleInit";
    private static final String IMODULE_REGISTER_NAME = "IModuleRegister";
    private static final String MODULE_MANAGER_NAME = "ModuleManager";
    private static final String MODULE_REGISTER_PREFIX = "ModuleRegister";
    private static final String PACKAGE_DELIMITED = ".";
    private static final String CLASS_DELIMITED = "$";

    private Filer mFiler;
    private Elements mElements;
    private Types mTypes;
    private String mModuleName;

    @Override public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        mElements = processingEnv.getElementUtils();
        mTypes = processingEnv.getTypeUtils();
        mModuleName = ModuleUtils.getModuleName(mFiler).toLowerCase();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> moduleElementSet = roundEnvironment.getElementsAnnotatedWith(Module.class);
        List<TypeElement> modules = new ArrayList<>();
        TypeMirror typeModuleInit = mElements.getTypeElement(getFullName(IMODULE_INIT_NAME)).asType();
        for (Element element : moduleElementSet) {
            if (!mTypes.isSubtype(element.asType(), typeModuleInit)) {
                throw new RuntimeException("The module class [" + element.asType().toString() + "] annotated by @Modular must be implements the IModular interface." );
            }
            TypeMirror typeModuleDepend = null;
            try {
                Class<? extends IModuleDepend> dependClass = element.getAnnotation(Module.class).depend();
                typeModuleDepend = mElements.getTypeElement(dependClass.getCanonicalName()).asType();
            } catch (MirroredTypeException mte) {
                typeModuleDepend = mte.getTypeMirror();
            }
            if (!mTypes.isSubtype(element.asType(), typeModuleDepend)) {
                throw new RuntimeException("The module class [" + element.asType().toString() + "] annotated by @Modular must be implements the depend interface." );
            }
            modules.add(((TypeElement) element));
        }
        createFile(modules);
        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(Module.class.getCanonicalName());
        return annotations;
    }

    private void createFile(List<TypeElement> modules) {
        try {
            JavaFileObject jfo = mFiler.createSourceFile(getFullName(getModuleLoaderSimpleName()), new Element[]{});
            Writer writer = jfo.openWriter();
            writer.write(brewCode(modules));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String brewCode(List<TypeElement> modules) {
        StringBuilder builder = new StringBuilder();
        builder.append("package " + PACKAGE_NAME + ";\n\n");

        builder.append("import ").append(getFullName(IMODULE_INIT_NAME)).append(";\n");
        builder.append("import ").append(getFullName(IMODULE_REGISTER_NAME)).append(";\n");
        builder.append("import ").append(getFullName(MODULE_MANAGER_NAME)).append(";\n");
        builder.append("import android.app.Application;\n");

        for (TypeElement module : modules) {
            try {
                Class<? extends IModuleDepend> dependClass = module.getAnnotation(Module.class).depend();
                builder.append("import ").append(dependClass.getCanonicalName()).append(";\n");
            } catch (MirroredTypeException mte) {
                TypeElement typeElement = (TypeElement) ((DeclaredType) mte.getTypeMirror()).asElement();
                builder.append("import ").append(typeElement.getQualifiedName()).append(";\n");
            }
        }

        appendComment(builder);
        builder.append("public class ").append(getModuleLoaderSimpleName()).append(" implements " + IMODULE_REGISTER_NAME + " { \n\n");
        builder.append("\t@Override\n");
        builder.append("\tpublic void register() throws Exception { \n");
        builder.append("\t\t");
        for (TypeElement module : modules) {
            String dependClassSimpleName = null;
            try {
                Class<? extends IModuleDepend> dependClass = module.getAnnotation(Module.class).depend();
                dependClassSimpleName = dependClass.getSimpleName();
            } catch (MirroredTypeException mte) {
                TypeElement typeElement = (TypeElement) ((DeclaredType) mte.getTypeMirror()).asElement();
                dependClassSimpleName = typeElement.getSimpleName().toString();
            }
            builder.append("ModuleManager.registerModule(")
                    .append(dependClassSimpleName).append(".class").append(", ")
                    .append("(").append(dependClassSimpleName).append(") Class.forName(\"").append(module.getQualifiedName()).append("\").newInstance(), ")
                    .append(module.getAnnotation(Module.class).priotity()).append(");\n");
        }
        builder.append("\t}\n");
        builder.append("}");
        return builder.toString();
    }

    private void appendComment(StringBuilder builder) {
        builder.append("\n/**\n");
        builder.append(" * This class was generated by EasyModular\n");
        builder.append(" * do not modify it\n");
        builder.append(" * or it will cause EasyRouter run with exception\n");
        builder.append(" */\n\n");
    }

    private String getModuleLoaderSimpleName() {
        return MODULE_REGISTER_PREFIX + CLASS_DELIMITED + mModuleName;
    }

    private String getFullName(String simple) {
        return PACKAGE_NAME + PACKAGE_DELIMITED + simple;
    }
}
