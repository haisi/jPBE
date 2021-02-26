/*
 * (c) Copyright 2021 Hasan Selman Kara All rights reserved.
 */
package li.selman.jpbe.dsl.position;

import java.util.List;
import java.util.regex.MatchResult;
import li.selman.jpbe.dsl.token.TokenSequence;

/**
 * @author Hasan Selman Kara
 */
public class DynamicPosition implements Position {

    private final TokenSequence prefix;
    private final TokenSequence suffix;
    private final int occurrence;

    public DynamicPosition(TokenSequence prefix, TokenSequence suffix, int occurrence) {
        if (occurrence == 0) throw new IllegalArgumentException("Occurrence cannot be zero");

        this.prefix = prefix;
        this.suffix = suffix;
        this.occurrence = occurrence;
    }


    @Override
    public int getSize() {
        return prefix.getSize() * suffix.getSize();
    }

    /**
     * @throws NoPositionException if no position can be evaluated to
     */
    @Override
    public int evalToPosition(String s) throws NoPositionException {
        List<MatchResult> prefixMatches = Matcher.matches(prefix.getMergedPattern(), s);
        if (prefixMatches.isEmpty()) throw new RuntimeException("No matches for prefix");
        List<MatchResult> suffixMatches = Matcher.matches(suffix.getMergedPattern(), s);
        if (suffixMatches.isEmpty()) throw new RuntimeException("No matches for suffix");

        int c = occurrence;
        if (c > 0) {
            for (MatchResult prefixMatch : prefixMatches) {
                int endLeft = prefixMatch.end();

                for (MatchResult suffixMatch : suffixMatches) {
                    int startRight = suffixMatch.start();

                    if (endLeft != startRight) continue;
                    if (--c == 0) {
                        return endLeft;
                    }
                }
            }
        } else {
            for (int i = prefixMatches.size() - 1; i >= 0; i--) {
                int endLeft = prefixMatches.get(i).end();

                for (int j = suffixMatches.size() - 1; j >= 0; j--) {
                    int startRight = suffixMatches.get(j).start();
                    if (endLeft != startRight) continue;
                    if (++c == 0) {
                        return endLeft;
                    }
                }
            }
        }

        throw new NoPositionException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DynamicPosition that = (DynamicPosition) o;

        if (occurrence != that.occurrence) return false;
        if (!prefix.equals(that.prefix)) return false;
        return suffix.equals(that.suffix);
    }

    @Override
    public int hashCode() {
        int result = prefix.hashCode();
        result = 31 * result + suffix.hashCode();
        result = 31 * result + occurrence;
        return result;
    }

    @Override
    public String toString() {
        return "DynamicPosition(" + prefix + ", " + suffix + ", " + occurrence + ')';
    }
}
