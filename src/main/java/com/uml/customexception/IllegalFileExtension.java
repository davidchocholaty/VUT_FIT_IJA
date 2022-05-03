package com.uml.customexception;

public class IllegalFileExtension extends Exception {
    /**
     * Creates a new instance of IllegalFileExtension.
     *
     * @param errorMessage Illegal file extension exception error message.
     */
    public IllegalFileExtension(String errorMessage) {
        super(errorMessage);
    }

}
