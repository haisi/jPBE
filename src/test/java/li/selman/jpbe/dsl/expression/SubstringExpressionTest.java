/*
 * (c) Copyright 2021 Hasan Selman Kara All rights reserved.
 */
package li.selman.jpbe.dsl.expression;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import li.selman.jpbe.dsl.position.ConstantPosition;
import org.junit.jupiter.api.Test;

/**
 * @author Hasan Selman Kara
 */
class SubstringExpressionTest {

    @Test
    void getWholeString() {
        String expected = "12345";
        String input = "12345";

        ConstantPosition endPosition = ConstantPosition.lastPosition();

        var subStr = new SubstringExpression(new ConstantPosition(0), endPosition);
        Optional<String> actual = subStr.apply(input);

        assertThat(actual).isNotEmpty().containsSame(expected);
    }

}
