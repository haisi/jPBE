package li.selman.jpbe.dsl.expression;

import java.util.Map;
import java.util.Optional;

/**
 * Note that not the whole input string has to match for the lookup expression to be applied.
 * For example, if we have a lookup table that translates from German to French numbers, the following input:
 * "einsAAAA, zwei, dreissig" would translate to "uneAAAA, doix, troisssig".
 * <p>
 * Note that if you want a stricter behaviour, i.e. the whole string must match, you can implement your own
 * LookupExpression <b>and</b> LookupExpressionBuilder.
 *
 * @author Hasan Selman Kara
 * @see li.selman.jpbe.algorithm.expressionbuilders.LookupExpressionBuilder
 */
public class LookupExpression implements Expression {

    private final Map<String, String> lookupTable;

    public LookupExpression(Map<String, String> lookupTable) {
        this.lookupTable = lookupTable;
    }

    @Override
    public Optional<String> apply(String input) {
        if (lookupTable.containsKey(input)) {
            return Optional.ofNullable(lookupTable.get(input));
        } else {
            // Use the more computationally expensive search
            String lookupValuesReplaced = null;
            for (var item : lookupTable.entrySet()) {
                String key = item.getKey();
                String value = item.getValue();
                if (input.contains(key)) {
                    if (lookupValuesReplaced == null) {
                        lookupValuesReplaced = input;
                    }
                    lookupValuesReplaced = lookupValuesReplaced.replace(key, value);
                }
            }

            return Optional.ofNullable(lookupValuesReplaced);
        }
    }

    @Override
    public int getSize() {
        return 1;
    }
}
