package li.selman.jpbe.dsl.expression;

import li.selman.jpbe.dsl.Expression;
import li.selman.jpbe.dsl.position.NoPositionException;
import li.selman.jpbe.dsl.position.Position;

import java.util.Objects;
import java.util.Optional;

/**
 * @author Hasan Selman Kara
 */
public class SubstringExpression implements Expression {

    private final Position startPosition;
    private final Position endPosition;

    public SubstringExpression(Position startPosition, Position endPosition) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    /**
     * @param s input string
     * @return substring of input or empty if start or end index of substring evals to out of bounds
     */
    @Override
    public Optional<String> apply(final String s) {
        Objects.requireNonNull(s, "Cannot apply substring expression on null!");

        int startIdx;
        int endIndex;
        try {
            startIdx = startPosition.evalToPosition(s);
            endIndex = endPosition.evalToPosition(s);
        } catch (NoPositionException ex) {
            return Optional.empty();
        }

        if (isAnyIndexOutOfBounds(s.length(), startIdx, endIndex)) {
            return Optional.empty();
        }

        return Optional.of(s.substring(startIdx, endIndex));
    }

    private boolean isAnyIndexOutOfBounds(int stringLength, int startIdx, int endIndex) {
        return startIdx > endIndex || endIndex > stringLength;
    }

    @Override
    public int getSize() {
        return startPosition.getSize() * endPosition.getSize();
    }
}
