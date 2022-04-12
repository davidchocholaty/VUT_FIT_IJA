/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Enum representing UML visibility types.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */

package com.uml.filehandler;

/**
 * The class representing custom exceptions for program.
 */
public class CustomException extends Exception {
    /**
     * Creates a new instance of CustomException.
     *
     * @param errorMessage Custom exception error message.
     */
    public CustomException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * The class representing illegal file format custom exception.
     */
    public static class IllegalFileFormat extends CustomException {
        /**
         * Creates a new instance of IllegalFileFormat.
         *
         * @param errorMessage Illegal file format exception error message.
         */
        public IllegalFileFormat(String errorMessage) {
            super(errorMessage);
        }
    }

    /**
     * The class representing illegal file extension custom exception.
     */
    public static class IllegalFileExtension extends CustomException {
        /**
         * Creates a new instance of IllegalFileExtension.
         *
         * @param errorMessage Illegal file extension exception error message.
         */
        public IllegalFileExtension(String errorMessage) {
            super(errorMessage);
        }
    }
}
