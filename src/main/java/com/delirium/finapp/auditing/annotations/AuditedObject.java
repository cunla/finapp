package com.delirium.finapp.auditing.annotations;

import java.lang.annotation.*;

/**
 * Created by Daniel Moran on 11/23/2015.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditedObject {
    String value();
}
