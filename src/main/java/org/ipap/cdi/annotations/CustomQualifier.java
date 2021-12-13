package org.ipap.cdi.annotations;

import java.lang.annotation.*;

/**
 * Identifies qualifier for interfaces. Should be used to avoid conflict if
 * there are multiple implementations of the same interface.
 * <br>
 * The value should be the name of the class that implements the interface.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomQualifier {

    String value() default "";
}
