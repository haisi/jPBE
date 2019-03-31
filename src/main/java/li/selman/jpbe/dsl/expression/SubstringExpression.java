package li.selman.jpbe.dsl.expression;

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

    @Override
    public Optional<String> apply(final String s) {
        Objects.requireNonNull(s, "Cannot apply substring expression on null!");

        int length = s.length();
        int startIdx = startPosition.evalToPosition(s);
        int endIndex = endPosition.evalToPosition(s);
        if (startIdx > endIndex || endIndex > length) {
            return Optional.empty();
        }

        return Optional.of(s.substring(startIdx, endIndex));
    }

    @Override
    public int getSize() {
        return startPosition.getSize() * endPosition.getSize();
    }
}
