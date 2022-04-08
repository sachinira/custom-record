package io.ballerina.customrecords.exceptions;

/**
 *
 */
public class CustomRecordGeneratorException extends Exception {
    public CustomRecordGeneratorException(String message, Throwable e) {
        super(message, e);
    }

    public CustomRecordGeneratorException(String message) {
        super(message);
    }
}
