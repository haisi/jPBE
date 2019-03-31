package li.selman.jpbe.dsl.position;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hasan Selman Kara
 */
class ConstantPositionTest {

    @Test
    void NegativeKTest() {
        String s = "ABCDE";

        String expectedSubString;
        String actualSubString;
        ConstantPosition cPos;
        int actualPosition;

        cPos = new ConstantPosition(-1);
        expectedSubString = "E";
        actualPosition = cPos.evalToPosition(s);
        actualSubString = s.substring(actualPosition);
        assertThat(actualSubString).isEqualTo(expectedSubString);

        cPos = new ConstantPosition(-2);
        expectedSubString = "DE";
        actualPosition = cPos.evalToPosition(s);
        actualSubString = s.substring(actualPosition);
        assertThat(actualSubString).isEqualTo(expectedSubString);
    }

}
