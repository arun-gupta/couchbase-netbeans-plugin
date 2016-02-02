package org.netbeans.modules.couchbase.bucket;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.openide.windows.TopComponent;

public class ObjectTopComponent extends TopComponent {
    @Retention(RetentionPolicy.SOURCE)
    @Target({ ElementType.TYPE, ElementType.METHOD })
    public static @interface OpenActionForObjectRegistration {}
} 