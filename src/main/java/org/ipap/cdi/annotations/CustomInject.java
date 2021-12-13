package org.ipap.cdi.annotations;


import java.lang.annotation.*;

/**
 * Identifies injectable constructors and fields. An injectable member may have any access
 * modifier (private, package-private, protected, public). Constructors are
 * injected first, followed by fields.
 *
 * <p>Injectable constructors are annotated with {@code @Inject} and accept
 * zero or more dependencies as arguments. {@code @Inject} can apply to at most
 * one constructor per class.
 *
 *
 * <p>Injectable fields:
 * <ul>
 *   <li>are annotated with {@code @Inject}.
 *   <li>are not final.
 *   <li>may have any otherwise valid name.</li></ul>
 */
@Target({ElementType.CONSTRUCTOR, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomInject {
}
