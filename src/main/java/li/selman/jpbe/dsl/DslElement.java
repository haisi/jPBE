/*
 * (c) Copyright 2021 Hasan Selman Kara All rights reserved.
 */
package li.selman.jpbe.dsl;

/**
 * @author Hasan Selman Kara
 */
public interface DslElement {

    /**
     * The weight can be used to find an optimal program in a set of valid programs.
     * "Optimal" is subjective and can go into different directions.
     * Some examples include:
     * <li>
     *     <ul>Weight by how expensive a DSL element is to execute.</ul>
     *     <ul>
     *         Weight by how likely a DSL element is to be part of a generic full program.
     *         A ConstantPosition generally is less generic.
     *         Thus, the user has to specify their intent more often to finish their wrangling task.
     *     </ul>
     * </li>
     * @return the weight of the DSL element
     */
    int getDslWeight();

}
