/*
 * (c) Copyright 2021 Hasan Selman Kara All rights reserved.
 */

package li.selman.jpbe.dsl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import li.selman.jpbe.dsl.expression.Expressions;
import li.selman.jpbe.dsl.expression.SubstringExpression;
import li.selman.jpbe.dsl.position.ConstantPosition;
import li.selman.jpbe.dsl.position.DynamicPosition;
import li.selman.jpbe.dsl.position.Position;
import li.selman.jpbe.dsl.token.Token;
import li.selman.jpbe.dsl.token.TokenSequence;
import org.junit.jupiter.api.Test;

/**
 * Tests which make use of the full DSL to express all possible String transformation.
 */
public class EndToEndDslTest {

    @Test
    void productSizeTest() {
        DynamicPosition beforeNumber = new DynamicPosition(TokenSequence.of(), TokenSequence.of(Token.NUM), 1);
        ConstantPosition endOfString = ConstantPosition.lastPosition();

        Expressions trace = new Expressions(List.of(new SubstringExpression(beforeNumber, endOfString)));

        assertThat(trace.apply("BTR KRNL WK CORN 15Z")).contains("15Z");
        assertThat(trace.apply("CAMP DRY DBL NDL 3.6 OZ")).contains("3.6 OZ");
        assertThat(trace.apply("CHROE BOY HD SC SPNG 1 PK")).contains("1 PK");
        assertThat(trace.apply("French Worcester - foobar 5 Z")).contains("5 Z");
    }

    @Test
    void urlTest() {
        Position left = new ConstantPosition(0);
        Position slash = new DynamicPosition(TokenSequence.of(Token.BACK_SLASH), TokenSequence.of(), -1);

        Expressions trace = new Expressions(List.of(new SubstringExpression(left, slash)));

        assertThat(trace.apply("Company\\Code\\index.html")).contains("Company\\Code\\");
        assertThat(trace.apply("Company\\Docs\\Spec\\sepcs.doc")).contains("Company\\Docs\\Spec\\");
    }

}
