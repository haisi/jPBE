/*
 * (c) Copyright 2021 Hasan Selman Kara All rights reserved.
 */
package li.selman.jpbe.dsl.position;

import li.selman.jpbe.dsl.DslElement;

/**
 * @author Hasan Selman Kara
 */
public interface Position extends DslElement {

    /**
     * Given an input, dynamically evaluate to an index inside for string manipulation operations.
     * @param s input
     * @return the index
     * @throws NoPositionException in certain implementations
     */
    // TODO(#api-design): make return optional?
    //  As this is a hot path and in most cases a happy case,
    //  wrapping each return in an Optional might be more expensive,
    //  than a rare exception.
    int evalToPosition(String s) throws NoPositionException;

}
