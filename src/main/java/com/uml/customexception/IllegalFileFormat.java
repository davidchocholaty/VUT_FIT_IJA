/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Enum representing UML visibility types.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */


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
