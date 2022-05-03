package com.uml.customexception;

/**
 * The class representing illegal file format custom exception.
 */
public class IllegalFileFormat extends Exception {
    /**
     * Creates a new instance of IllegalFileFormat.
     *
     * @param errorMessage Illegal file format exception error message.
     */
    public IllegalFileFormat(String errorMessage) {
        super(errorMessage);
    }

}
