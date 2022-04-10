package com.uml.filehandler;

public class CustomException extends Exception {
    public CustomException(String errorMessage) {
        super(errorMessage);
    }

    public static class IllegalFileFormat extends CustomException {
        public IllegalFileFormat(String errorMessage) {
            super(errorMessage);
        }
    }

    public static class IllegalFileExtension extends CustomException {
        public IllegalFileExtension(String errorMessage) {
            super(errorMessage);
        }
    }
}
