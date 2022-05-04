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
 * A class represents a return self message.
 */
public class UMLReturnSelfMessage extends UMLMessageOperationType {
    /**
     * Creates an UMLReturnSelfMessage instance with start and end lifeline.
     *
     * @param lifeline Start and end lifeline.
     */
    public UMLReturnSelfMessage(UMLLifeline lifeline) {
        super(lifeline, lifeline);
    }

}