package org.ipap.cdi.injector;

/**
 * Helper class that holds information of {@link org.ipap.cdi.annotations.CustomComponent}-annotated classes to be registered
 */
public class ComponentRegistration {

    private final Class<?> registeredClass;
    private final CreationType creationType;
    private final Class<?>[] interfacesImplemented;

    public ComponentRegistration(Class<?> registeredClass, CreationType creationType) {
        this.registeredClass = registeredClass;
        this.creationType = creationType;
        this.interfacesImplemented = registeredClass.getInterfaces();
    }

    public Class<?> getRegisteredClass() {
        return registeredClass;
    }

    public CreationType getCreationType() {
        return creationType;
    }

    public Class<?>[] getInterfacesImplemented() {
        return interfacesImplemented;
    }
}
