/*
 * (c) Copyright 2021 Hasan Selman Kara All rights reserved.
 */
package li.selman.jpbe.classifier;

import java.util.regex.Matcher;
import li.selman.jpbe.dsl.token.TokenSequence;

/**
 * Matches if the TokenSequence {@code r} occurs {@code k} times in a given string or
 * inverse if positive is false.
 *
 * @author Hasan Selman Kara
 * @see li.selman.jpbe.dsl.token.TokenSequence
 */
public final class Match implements Predicate {

    private final TokenSequence r;
    private final int k;
    private final boolean positive;

    private Match(TokenSequence r, int k, boolean positive) {
        if (r.getSize() == 0) throw new IllegalArgumentException("TokenSequence can't be empty.");
        // TODO(#bug): does the possibility '0' matches make sense?
        if (k < 0) throw new IllegalArgumentException("Number of matches can't be negative");


        this.r = r;
        this.k = k;
        this.positive = positive;
    }

    static Match positive(TokenSequence r, int k) {
        return new Match(r, k, true);
    }

    static Match negative(TokenSequence r, int k) {
        return new Match(r, k, false);
    }

    @Override
    public boolean matches(String s) {
        int numberOfMatches = getNumberOfMatches(s);

        if (positive) {
            return numberOfMatches >= k;
        } else {
            return !(numberOfMatches >= k);
        }
    }

    private int getNumberOfMatches(String s) {
        Matcher matcher = r.getMergedPattern().matcher(s);
        int numberOfMatches = 0;
        while (matcher.find()) {
            numberOfMatches++;
        }
        return numberOfMatches;
    }
}
