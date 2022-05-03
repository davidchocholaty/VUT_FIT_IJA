package com.uml.customexception;

public class InvalidOperationLabel extends Exception {
    public InvalidOperationLabel(String errorMessage) {
        super(errorMessage);
    }

}
