package org.netbeans.modules.couchbase.bucket;

import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import org.openide.awt.ActionID;
import org.openide.filesystems.annotations.LayerBuilder.File;
import org.openide.filesystems.annotations.LayerGeneratingProcessor;
import org.openide.filesystems.annotations.LayerGenerationException;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.TopComponent;
import org.openide.windows.TopComponent.Description;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
@ServiceProvider(service = Processor.class)
public final class ObjectTopComponentProcessor extends LayerGeneratingProcessor {
    public ObjectTopComponentProcessor() {
    }
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> hash = new HashSet<String>();
        hash.add(ObjectTopComponent.OpenActionForObjectRegistration.class.getCanonicalName());
        return hash;
    }
    @Override
    protected boolean handleProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) throws LayerGenerationException {
        for (Element e : roundEnv.getElementsAnnotatedWith(ObjectTopComponent.OpenActionForObjectRegistration.class)) {
            ObjectTopComponent.OpenActionForObjectRegistration reg = e.getAnnotation(ObjectTopComponent.OpenActionForObjectRegistration.class);
            assert reg != null;
            Description info = findInfo(e);
            ActionID aid = e.getAnnotation(ActionID.class);
            if (aid != null) {
                File actionFile = layer(e).
                        file("Actions/" + aid.category() + "/" + aid.id().replace('.', '-') + ".instance").
                        methodvalue("instanceCreate", "org.openide.windows.TopComponent", "openAction");
                actionFile.instanceAttribute("component", TopComponent.class, reg, null);
                if (reg.preferredID().length() > 0) {
                    actionFile.stringvalue("preferredID", reg.preferredID());
                }
                generateContext(e, actionFile);
                actionFile.bundlevalue("displayName", reg.displayName(), reg, "displayName");
                if (info != null && info.iconBase().length() > 0) {
                    actionFile.stringvalue("iconBase", info.iconBase());
                }
                actionFile.write();
            }
        }
        return true;
    }
    private void generateContext(Element e, File f) throws LayerGenerationException {
        ExecutableElement ee = null;
        ExecutableElement candidate = null;
        for (ExecutableElement element : ElementFilter.constructorsIn(e.getEnclosedElements())) {
            if (element.getKind() == ElementKind.CONSTRUCTOR) {
                candidate = element;
                if (!element.getModifiers().contains(Modifier.PUBLIC)) {
                    continue;
                }
                if (ee != null) {
                    throw new LayerGenerationException("Only one public constructor allowed", e, processingEnv, null); // NOI18N
                }
                ee = element;
            }
        }
        if (ee == null || ee.getParameters().size() != 1) {
            if (candidate != null) {
                throw new LayerGenerationException("Constructor has to be public with one argument", candidate);
            }
            throw new LayerGenerationException("Constructor must have one argument", ee);
        }
        VariableElement ve = (VariableElement) ee.getParameters().get(0);
        TypeMirror ctorType = ve.asType();
        switch (ctorType.getKind()) {
            case ARRAY:
                String elemType = ((ArrayType) ctorType).getComponentType().toString();
                throw new LayerGenerationException("Use List<" + elemType + "> rather than " + elemType + "[] in constructor", e, processingEnv, null);
            case DECLARED:
                break; // good
            default:
                throw new LayerGenerationException("Must use SomeType (or List<SomeType>) in constructor, not " + ctorType.getKind());
        }
        DeclaredType dt = (DeclaredType) ctorType;
        String dtName = processingEnv.getElementUtils().getBinaryName((TypeElement) dt.asElement()).toString();
        if ("java.util.List".equals(dtName)) {
            if (dt.getTypeArguments().isEmpty()) {
                throw new LayerGenerationException("Use List<SomeType>", ee);
            }
            f.stringvalue("type", binaryName(dt.getTypeArguments().get(0)));
            f.methodvalue("delegate", "org.openide.awt.Actions", "inject");
            f.stringvalue("injectable", processingEnv.getElementUtils().getBinaryName((TypeElement) e).toString());
            f.stringvalue("selectionType", "ANY");
            f.methodvalue("instanceCreate", "org.openide.awt.Actions", "context");
            return;
        }
        if (!dt.getTypeArguments().isEmpty()) {
            throw new LayerGenerationException("No type parameters allowed in ", ee);
        }
        f.stringvalue("type", binaryName(ctorType));
        f.methodvalue("delegate", "org.openide.awt.Actions", "inject");
        f.stringvalue("injectable", processingEnv.getElementUtils().getBinaryName((TypeElement) e).toString());
        f.stringvalue("selectionType", "EXACTLY_ONE");
        f.methodvalue("instanceCreate", "org.openide.awt.Actions", "context");
    }
    private String binaryName(TypeMirror t) {
        Element e = processingEnv.getTypeUtils().asElement(t);
        if (e != null && (e.getKind().isClass() || e.getKind().isInterface())) {
            return processingEnv.getElementUtils().getBinaryName((TypeElement) e).toString();
        } else {
            return t.toString(); // fallback - might not always be right
        }
    }
    private Description findInfo(Element e) throws LayerGenerationException {
        Element type;
        switch (e.asType().getKind()) {
            case DECLARED:
                type = e;
                break;
            case EXECUTABLE:
                type = ((DeclaredType) ((ExecutableType) e.asType()).getReturnType()).asElement();
                break;
            default:
                throw new LayerGenerationException("" + e.asType().getKind(), e);
        }
        TopComponent.Description info = type.getAnnotation(TopComponent.Description.class);
        return info;
    }
}