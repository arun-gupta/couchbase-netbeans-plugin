package org.netbeans.modules.couchbase.bucket;

import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.openide.awt.ActionID;
import org.openide.filesystems.annotations.LayerBuilder.File;
import org.openide.filesystems.annotations.LayerGeneratingProcessor;
import org.openide.filesystems.annotations.LayerGenerationException;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.TopComponent;

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
            ActionID aid = e.getAnnotation(ActionID.class);
            if (aid != null) {
                File actionFile = layer(e).
                        file("Actions/" + aid.category() + "/" + aid.id().replace('.', '-') + ".instance").
                        methodvalue("instanceCreate", "org.openide.windows.TopComponent", "openAction");
                actionFile.instanceAttribute("component", TopComponent.class, reg, null);
                actionFile.write();
            }
        }
        return true;
    }
}