package org.ipap.cdi.annotations;

import java.lang.annotation.*;

/**
 * <p>
 *  Specifies that an annotated class is a {@link CustomComponent @CustomComponent} with a Dependent scope in the application.
 * </p>
 *
 * <p>
 * Classes declared with scope <tt>@Dependent</tt> behave differently to classes with the Singleton scope. When a class is
 * declared to have scope <tt>@Dependent</tt>:
 * </p>
 *
 * <ul>
 * <li>No injected instance of the class is ever shared between multiple injection points.</li>
 * <li>Any instance of the class injected into an object that is being created by the container is bound to the lifecycle of the
 * newly created object.</li>
 *
 *  @see CustomComponent
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@CustomComponent
@Documented
public @interface CustomDependentComponent {
}
