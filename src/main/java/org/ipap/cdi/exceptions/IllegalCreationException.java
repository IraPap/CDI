package org.ipap.cdi.exceptions;

public class IllegalCreationException extends Exception {

    public IllegalCreationException(String message, String className) {
        super(String.format("%s for class %s", message, className));
    }
}
