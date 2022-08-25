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
public class UMLReturnSelfMessage extends UMLMessageLabelType {
    /**
     * Creates an UMLReturnSelfMessage instance with start and end lifeline and label.
     *
     * @param lifeline Start and end lifeline.
     * @param label Message label.
     */
    public UMLReturnSelfMessage(UMLLifeline lifeline, String label) {
        super(lifeline, lifeline, label);
    }

}