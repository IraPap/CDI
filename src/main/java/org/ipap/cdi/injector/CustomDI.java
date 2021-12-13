package org.ipap.cdi.injector;

import org.ipap.cdi.annotations.*;
import org.ipap.cdi.exceptions.CircularDependencyException;
import org.ipap.cdi.exceptions.IllegalCreationException;
import org.ipap.cdi.utils.ClassUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public class CustomDI {

    /**
     * key -> class name, value -> {@link ComponentRegistration}
     */
    private final Map<String, ComponentRegistration> registrationMap;

    /**
     * key -> class name
     */
    private final Map<String, Object> createdSingletonObjects;

    public Set<Object> getDiObjects() {
        return diObjects;
    }

    private final Set<Object> diObjects;

    public CustomDI(String packageName) throws IllegalCreationException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, CircularDependencyException {
        this.registrationMap = new HashMap<>();
        this.createdSingletonObjects = new HashMap<>();
        this.diObjects = new HashSet<>();
        init(packageName);
    }

    /**
     * The injector scans, creates and injects dependencies under the package specified.
     *
     * @param packageName the name of the package
     */
    private void init(String packageName) throws ClassNotFoundException, IllegalCreationException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, CircularDependencyException {

        //get all classes annotated as Custom Components
        Set<Class<?>> components = ClassUtils.getAllClasses(packageName)
                .stream()
                .filter(c -> c.isAnnotationPresent(CustomComponent.class) || c.isAnnotationPresent(CustomSingletonComponent.class) || c.isAnnotationPresent(CustomDependentComponent.class))
                .collect(Collectors.toSet());

        for (var component : components) {
            registerComponent(component);
        }


        for (var entry : registrationMap.entrySet()) {

            if (entry.getValue().getCreationType() == CreationType.SINGLETON && createdSingletonObjects.containsKey(entry.getKey())) {
                continue;
            }

            Object obj = createComponentObject(entry.getValue().getRegisteredClass(), null);
            addToInjectorContext(entry.getValue().getRegisteredClass().getName(), obj);

        }


    }

    /**
     * Registers all the components to a Map holding information needed for creating and injecting objects
     *
     * @param component a class annotated as {@link CustomComponent}
     *
     * @throws IllegalCreationException if the convention of the CustomComponent annotation is broken
     */
    private void registerComponent(Class<?> component) throws IllegalCreationException {

        if (Modifier.isInterface(component.getModifiers())) {

            throw new IllegalCreationException("Trying to create interface component!", component.getSimpleName());
        }

        if (Modifier.isAbstract(component.getModifiers())) {

            throw new IllegalCreationException("Trying to create abstract component!", component.getSimpleName());
        }

        if (isNotRegistered(component)) {

            CreationType creationType = component.isAnnotationPresent(CustomDependentComponent.class) ? CreationType.DEPENDENT : CreationType.SINGLETON;
            registrationMap.put(component.getName(), new ComponentRegistration(component, creationType));
        }

    }

    /**
     * @param creationClass the class to create the object from
     * @param dependencyList a list with dependencies found in the current object, used to identify circular dependencies
     * @return the newly created object
     */
    private Object createComponentObject(Class<?> creationClass, Set<String> dependencyList) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IllegalCreationException, CircularDependencyException {

        if (dependencyList == null) {
            dependencyList = new HashSet<>();
        }

        if (dependencyList.contains(creationClass.getName())) {
            throw new CircularDependencyException(dependencyList, creationClass.getName());
        }
        dependencyList.add(creationClass.getName());

        Constructor<?> constructor = creationClass.getDeclaredConstructors()[0];
        Object obj = constructor.newInstance(handleConstructorParams(constructor, dependencyList).toArray());
        handleClassFields(creationClass, obj, dependencyList);
        addToInjectorContext(creationClass.getName(), obj);
        return obj;
    }


    /**
     * @param creationClass the class to create the object from
     * @param toInject true if class object is to be injected as a dependency
     * @param dependencyList a list with dependencies found in the current object, used to identify circular dependencies
     * @return the newly created object
     */
    private Object createComponentObject(Class<?> creationClass, boolean toInject, Set<String> dependencyList) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IllegalCreationException, CircularDependencyException {

        if (!toInject) {
            return createComponentObject(creationClass, dependencyList);
        } else {

            if (isNotRegistered(creationClass)) {

                throw new IllegalCreationException("Attempting to inject dependency not registered as component!", creationClass.getSimpleName());
            }

            //checks for singleton dependencies
            Object singletonObj = createdSingletonObjects.getOrDefault(creationClass.getName(), null);
            if (singletonObj == null) {
                return createComponentObject(creationClass, dependencyList);
            } else {
                return singletonObj;
            }

        }

    }

    /**
     * Creates the objects required as constructor parameters of specified constructor.
     * Checks for constructor injection and evaluates interface components
     *
     */
    private List<Object> handleConstructorParams(Constructor<?> constructor, Set<String> dependencyList) throws IllegalCreationException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, CircularDependencyException {

        boolean hasInject = constructor.getAnnotation(CustomInject.class) != null;
        List<Object> constructorParams = new ArrayList<>();

        for (var parameter : constructor.getParameters()) {

            if (Modifier.isInterface(parameter.getType().getModifiers())) {

                //qualifier value is the annotation value or if absent, the constructor parameter's name
                CustomQualifier customQualifier = parameter.getAnnotation(CustomQualifier.class);
                String qualifierValue = customQualifier != null ? customQualifier.value() : parameter.getName();
                Class<?> implementation = hasInject ? checkInterfaceImpl(parameter.getType(), qualifierValue) : parameter.getType();
                constructorParams.add(createComponentObject(implementation, hasInject, dependencyList));

            } else {

                constructorParams.add(createComponentObject(parameter.getType(), hasInject, dependencyList));
            }

        }

        return constructorParams;
    }

    /**
     * Handles injected field dependencies of object
     *
     * @param creationClass the class to create the dependency from
     * @param createdObject the created object with possible field dependencies
     * @param dependencyList a list with dependencies found in the current object, used to identify circular dependencies
     */
    private void handleClassFields(Class<?> creationClass, Object createdObject, Set<String> dependencyList) throws IllegalCreationException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, CircularDependencyException {

        for (var field : creationClass.getDeclaredFields()) {

            boolean hasInject = field.getAnnotation(CustomInject.class) != null;

            if (!hasInject) {
                continue;
            }
            field.setAccessible(true);
            if (Modifier.isInterface(field.getType().getModifiers())) {

                //qualifier value is the annotation value or if absent, the field's name
                CustomQualifier customQualifier = field.getAnnotation(CustomQualifier.class);
                String qualifierValue = customQualifier != null ? customQualifier.value() : field.getName();
                Class<?> implementation = checkInterfaceImpl(field.getType(), qualifierValue);
                field.set(createdObject, createComponentObject(implementation, true, dependencyList));

            } else {

                field.set(createdObject, createComponentObject(field.getType(), true, dependencyList));
            }

        }
    }

    /**
     * Evaluates an injected interface and attempts to resolve to its implementation class
     *
     * @param interfaceClass the interface class
     * @param qualifierValue a quantifier String to resolve the interface implementation
     * @return the class that implements the interface
     */
    private Class<?> checkInterfaceImpl(Class<?> interfaceClass, String qualifierValue) throws IllegalCreationException {

        //get all classes registered as components that implement the interface
        Set<Class<?>> implementations = registrationMap.values()
                .stream()
                .filter(componentRegistration -> Arrays.stream(componentRegistration.getInterfacesImplemented()).anyMatch(interfaceClass::isAssignableFrom))
                .map(ComponentRegistration::getRegisteredClass)
                .collect(Collectors.toSet());

        if (implementations.isEmpty()) {
            throw new IllegalCreationException("No implementation found as component!", interfaceClass.getSimpleName());
        }

        if (implementations.size() == 1) {

            return implementations.iterator().next();
        }

        Class<?> implementation = implementations.stream()
                .filter(impl -> impl.getSimpleName().equalsIgnoreCase(qualifierValue))
                .findFirst().orElse(null);

        if (implementation == null) {
            throw new IllegalCreationException("More than one implementations found and no qualifier to resolve conflict!", interfaceClass.getSimpleName());
        }

        return implementation;


    }

    private void addToInjectorContext(String className, Object createdObject) {

        if (registrationMap.get(className).getCreationType() == CreationType.SINGLETON) {

            createdSingletonObjects.putIfAbsent(className, createdObject);

        }

        diObjects.add(createdObject);
    }

    private boolean isNotRegistered(Class<?> component) {

        return !registrationMap.containsKey(component.getName());
    }

}
