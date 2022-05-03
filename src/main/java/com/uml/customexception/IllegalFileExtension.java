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
 * The class representing illegal file extension custom exception.
 */
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
