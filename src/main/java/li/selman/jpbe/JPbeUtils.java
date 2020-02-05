package li.selman.jpbe;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Hasan Selman Kara
 */
// TODO remove
final public class JPbeUtils {

    private JPbeUtils() {
        throw new AssertionError("No JPbeUtils instances for you!");
    }

    /**
     * https://stackoverflow.com/a/6020436/2290286
     *
     * @param p
     * @param input
     * @return
     */
    // TODO never used. why did I write this again?
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
