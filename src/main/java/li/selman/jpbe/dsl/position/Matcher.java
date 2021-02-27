/*
 * (c) Copyright 2021 Hasan Selman Kara All rights reserved.
 */
package li.selman.jpbe.dsl.position;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import li.selman.jpbe.dsl.token.TokenSequence;

/**
 * @author Hasan Selman Kara
 */
final class Matcher {

    private Matcher() {
        // NO-OP
    }

    static int positionOfRegex(TokenSequence r, String s, int from, int to) {
        if (from >= to) throw new IllegalArgumentException("From >= To");

        List<MatchResult> matches = matches(r.getMergedPattern(), s);
        if (matches.isEmpty()) throw new IllegalStateException("No matches found");

        int position = 1;
        for (MatchResult match : matches) {
            int start = match.start();
            int end = match.end();

            if (inRange(from, to, start, end)) {
                return position;
            } else {
                position++;
            }
        }

        throw new IllegalStateException("No matches");
    }

    static int totalNumberOfMatches(TokenSequence r, String s) {
        // according to specification
        if (r.getNumberOfTokens() == 0) {
            return 0;
        }

        return matches(r.getMergedPattern(), s).size();
    }

    private static boolean inRange(int from, int to, int start, int end) {
        return from >= start && to <= end;
    }

    static List<MatchResult> matches(final Pattern p, final CharSequence input) {
        final java.util.regex.Matcher matcher = p.matcher(input);
        final List<MatchResult> matchResults = new ArrayList<>();

        while (matcher.find()) {
            matchResults.add(matcher.toMatchResult());
        }

        return matchResults;
    }
}
