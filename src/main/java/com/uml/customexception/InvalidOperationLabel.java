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
 * The class representing invalid operation label custom exception.
 */
public class InvalidOperationLabel extends Exception {
    /**
     * Creates a new instance of InvalidOperationLabel.
     *
     * @param errorMessage Invalid operation label exception error message.
     */
    public InvalidOperationLabel(String errorMessage) {
        super(errorMessage);
    }

}
