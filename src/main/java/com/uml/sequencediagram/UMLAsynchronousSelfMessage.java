/**
 * Project for course IJA at FIT BUT.
 * <p>
 *     Base element with name.
 * </p>
 * @author: David Chocholaty <xchoch09@stud.fit.vutbr.cz>
 * @author: Adam Kankovsky <xkanko00@stud.fit.vutbr.cz>
 */

package com.uml.sequencediagram;

/**
 * A class represents an asynchronous self message.
 */
public class UMLAsynchronousSelfMessage extends UMLMessageOperationType {
    /**
     * Creates an UMLAsynchronousSelfMessage instance with start and end lifeline.
     *
     * @param lifeline Start and end lifeline.
     */
    public UMLAsynchronousSelfMessage(UMLLifeline lifeline) {
        super(lifeline, lifeline);
    }

}