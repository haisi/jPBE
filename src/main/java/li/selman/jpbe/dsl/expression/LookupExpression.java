package li.selman.jpbe.dsl.expression;

import java.util.Map;
import java.util.Optional;

/**
 * @author Hasan Selman Kara
 */
public class LookupExpression implements Expression {

    private final Map<String, String> lookupTable;

    public LookupExpression(Map<String, String> lookupTable) {
        this.lookupTable = lookupTable;
    }

    @Override
    public Optional<String> apply(String s) {
        if (lookupTable.containsKey(s)) {
            return Optional.ofNullable(lookupTable.get(s));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public int getSize() {
        return 1;
    }
}
