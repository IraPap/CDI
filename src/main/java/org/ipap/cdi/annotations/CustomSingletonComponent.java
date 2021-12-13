package org.ipap.cdi.annotations;

import java.lang.annotation.*;

/**
 * <p>
 * Specifies that an annotated class is a {@link CustomComponent @CustomComponent} with a Singleton scope in the application.
 * </p>
 *
 * @see CustomComponent
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@CustomComponent
@Documented
public @interface CustomSingletonComponent {
}
