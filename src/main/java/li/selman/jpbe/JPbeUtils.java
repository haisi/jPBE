package li.selman.jpbe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Hasan Selman Kara
 */
final public class JPbeUtils {

    private JPbeUtils() {
        throw new AssertionError("No JPbeUtils instances for you!");
    }

    /**
     * Checks that the specified object reference is not {@code null} or empty!
     *
     * @param string  string
     * @param message detail message to be used in the event that a {@code IllegalArgumentException} is thrown
     * @return string
     * @throws IllegalArgumentException if {@code string} is {@code null} or empty
     */
    public static String requireIsNotNullOrEmpty(String string, String message) {
        if ((string == null || string.isEmpty())) {
            throw new IllegalArgumentException(message);
        }
        return string;
    }

    public static List<MatchResult> matches(final Pattern p, final CharSequence input) {
        final Matcher matcher = p.matcher(input);
        final List<MatchResult> matchResults = new ArrayList<>();

        while (matcher.find()) {
            matchResults.add(matcher.toMatchResult());
        }

        return matchResults;
    }

    /**
     * https://stackoverflow.com/a/6020436/2290286
     *
     * @param p
     * @param input
     * @return
     */
    public static Iterable<MatchResult> allMatches(
        final Pattern p, final CharSequence input) {
        return () -> new Iterator<MatchResult>() {
            // Use a matcher internally.
            final Matcher matcher = p.matcher(input);
            // Keep a match around that supports any interleaving of hasNext/next calls.
            MatchResult pending;

            public boolean hasNext() {
                // Lazily fill pending, and avoid calling find() multiple times if the
                // clients call hasNext() repeatedly before sampling via next().
                if (pending == null && matcher.find()) {
                    pending = matcher.toMatchResult();
                }
                return pending != null;
            }

            public MatchResult next() {
                // Fill pending if necessary (as when clients call next() without
                // checking hasNext()), throw if not possible.
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                // Consume pending so next call to hasNext() does a find().
                MatchResult next = pending;
                pending = null;
                return next;
            }

            /** Required to satisfy the interface, but unsupported. */
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

}
