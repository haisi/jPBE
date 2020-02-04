package li.selman.jpbe.algorithm;

import li.selman.jpbe.JPbeUtils;
import li.selman.jpbe.dsl.token.TokenSequence;

import java.util.List;
import java.util.regex.MatchResult;

/**
 * @author Hasan Selman Kara
 */
public class Matcher {

    public static int positionOfRegex(TokenSequence r, String s, int from, int to) {
        if (from >= to) throw new IllegalArgumentException("From >= To");

        List<MatchResult> matches = JPbeUtils.matches(r.getMergedPattern(), s);
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

    public static int totalNumberOfMatches(TokenSequence r, String s) {
        // according to specification
        if (r.getNumberOfTokens() == 0) {
            return 0;
        }

        return JPbeUtils.matches(r.getMergedPattern(), s).size();
    }

    private static boolean inRange(int from, int to, int start, int end) {
        return from >= start && to <= end;
    }

}
