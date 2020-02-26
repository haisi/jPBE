package li.selman.jpbe.datastructure;

import java.util.Comparator;
import java.util.List;

/**
 * Represents all paths on {@link Graph} that lead from start to finish.
 *
 * @author Hasan Selman Kara
 */
public class TraceExpressionSet {

    private final List<TraceExpression> paths;

    public TraceExpressionSet(List<TraceExpression> paths) {
        if (paths == null || paths.isEmpty()) throw new IllegalArgumentException("Paths cannot be null or empty");

        this.paths = paths;
    }

    /**
     *
     * @return the
     */
    public TraceExpression findOptimalPath() {
        return paths.stream()
            .min(Comparator.comparingInt(TraceExpression::getSize))
            .orElseThrow(); // This should never throw, as the list cannot be empty
    }

}
