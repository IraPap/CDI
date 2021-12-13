package org.ipap.cdi.annotations;

import java.lang.annotation.*;

/**
 * Indicates that an annotated class is a "component".
 * Such classes are considered as candidates for auto-detection
 * by the custom Dependency Injector.
 *
 * <p>Other class-level annotations may be considered as identifying
 * a component as well, typically a special kind of component:
 * e.g. the {@link CustomSingletonComponent @CustomSingletonComponent} annotation or
 * {@link CustomDependentComponent @CustomDependentComponent} annotation.
 *
 * Cannot be used on interfaces or abstract classes.
 *
 * The default scope for a class with this annotation is Singleton.
 *
 * @see CustomSingletonComponent
 * @see CustomDependentComponent
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface CustomComponent {
}
