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
 * The class representing custom exception that operation does not exist in class diagram.
 */
public class OperationNotExists extends Exception {
    /**
     * Creates a new instance of OperationNotExists.
     *
     * @param errorMessage Invalid operation label exception error message.
     */
    public OperationNotExists(String errorMessage) {
        super(errorMessage);
    }
}