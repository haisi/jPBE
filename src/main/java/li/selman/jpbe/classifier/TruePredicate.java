/*
 * (c) Copyright 2021 Hasan Selman Kara All rights reserved.
 */
package li.selman.jpbe.classifier;

/**
 * Always returns true.
 *
 * @author Hasan Selman Kara
 */
class TruePredicate implements Predicate {

    /**
     * @return Always returns {@code true}
     */
    @Override
    public boolean matches(String s) {
        return true;
    }

    @Override
    public String toString() {
        return "TRUE";
    }
}
