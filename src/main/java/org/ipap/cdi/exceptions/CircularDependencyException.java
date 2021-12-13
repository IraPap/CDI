package org.ipap.cdi.exceptions;

import java.util.Set;

public class CircularDependencyException extends Exception {

    public CircularDependencyException(Set<String> dependencyList, String circularDependency) {
        super(String.format("Circular dependency found for class %s in list of dependencies: %s", circularDependency, dependencyList.toString()));
    }
}
