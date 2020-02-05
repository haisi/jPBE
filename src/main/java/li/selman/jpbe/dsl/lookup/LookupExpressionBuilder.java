package li.selman.jpbe.dsl.lookup;

import li.selman.jpbe.dsl.ExpressionBuilder;
import li.selman.jpbe.dsl.Expression;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Different LookupExpressionBuilder can be implemented.
 * E.g. using a database to store the lookup values.
 *
 * @author Hasan Selman Kara
 */
public class LookupExpressionBuilder implements ExpressionBuilder {

    private final List<List<String>> columns;

    public LookupExpressionBuilder(List<List<String>> columns) {
        if (columns.size() < 2) {
            throw new IllegalArgumentException("A lookup table only makes sense with two or more columns.");
        }
        assertAllColumnsSameLength(columns);

        this.columns = columns;
    }

    @Override
    public List<Expression> computeExpressions(String input, String substr) {
        for (int outputColIdx = 0; outputColIdx < columns.size(); outputColIdx++) {
            List<String> outputColumn = columns.get(outputColIdx);
            if (!outputColumn.contains(substr)) {
                // I.e. this substring cannot be represented by a lookup value from this column
                continue;
            }
            int outputRowIdx = outputColumn.indexOf(substr);

            // Finding the matching column to map to
            for (int inputColIdx = 0; inputColIdx < columns.size(); inputColIdx++) {
                if (inputColIdx == outputColIdx) {
                    // Mapping from column A to A is invalid
                    continue;
                }
                List<String> inputColumn = columns.get(inputColIdx);
                var element = inputColumn.get(outputRowIdx);
                // Unlike with the substr, the element in the column must only be contained in the input
                if (input.contains(element)) {
                    Map<String, String> lookupTable = combineListsIntoOrderedMap(inputColumn, outputColumn);
                    return List.of(new LookupExpression(lookupTable));
                }

            }
        }

        return Collections.emptyList();
    }

    private Map<String, String> combineListsIntoOrderedMap(List<String> keys, List<String> values) {
        if (keys.size() != values.size()) {
            throw new IllegalArgumentException("Cannot combine lists with dissimilar sizes");
        }
        var map = new LinkedHashMap<String, String>();
        for (int i = 0; i < keys.size(); i++) {
            map.put(keys.get(i), values.get(i));
        }
        return map;
    }

    private void assertAllColumnsSameLength(List<List<String>> columns) {
        int size = -1;
        for (List<String> column : columns) {
            if (size == -1) {
                size = column.size();
                if (size == 0) throw new IllegalArgumentException("Columns cannot be empty");
            } else if (column.size() != size) {
                throw new IllegalArgumentException("All columns must have the same size");
            }
        }
    }
}
