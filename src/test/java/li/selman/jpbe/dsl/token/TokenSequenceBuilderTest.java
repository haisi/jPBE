package li.selman.jpbe.dsl.token;

import org.junit.jupiter.api.Test;

import java.util.List;

import static li.selman.jpbe.dsl.token.Token.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hasan Selman Kara
 */
class TokenSequenceBuilderTest {

    private final Tokens tokens = new Tokens(List.of(
        START,
        END,
        ALPHA,
        SPACE,
        // Note that if you use LEAD_ZERO, you have to use NUM_NO_LEADING_ZEROS!
        // TODO maybe handle this in Tokens class? I.e. throw exception or auto add?
        LEADING_ZERO,
        NUM_NO_LEADING_ZEROS,
        COMMA,
        DOT
    ));
    private final TokenSequenceBuilder tsb = new TokenSequenceBuilder(999, tokens);

    @Test
    void computeTokenSequence() {

        assertThat(tsb.computeTokenSequence("A", 0, 1)).isEqualTo(TokenSequence.of(START, ALPHA, END));
        assertThat(tsb.computeTokenSequence("AA", 1, 2)).isEqualTo(TokenSequence.of(ALPHA, END));
        assertThat(tsb.computeTokenSequence("AA", 0, 1)).isEqualTo(TokenSequence.of(START, ALPHA));
        assertThat(tsb.computeTokenSequence("A ", 0, 2)).isEqualTo(TokenSequence.of(START, ALPHA, SPACE, END));
        assertThat(tsb.computeTokenSequence("AA  ", 0, 4)).isEqualTo(TokenSequence.of(START, ALPHA, SPACE, END));
        assertThat(tsb.computeTokenSequence("XA AX", 1, 4)).isEqualTo(TokenSequence.of(ALPHA, SPACE, ALPHA));
        assertThat(tsb.computeTokenSequence("AA   A", 0, 6)).isEqualTo(TokenSequence.of(START, ALPHA, SPACE, ALPHA, END));

        assertThat(tsb.computeTokenSequence("0016415", 0, 6)).isEqualTo(TokenSequence.of(START, LEADING_ZERO, NUM_NO_LEADING_ZEROS));
        assertThat(tsb.computeTokenSequence("16415", 0, 5)).isEqualTo(TokenSequence.of(START, NUM_NO_LEADING_ZEROS, END));
        assertThat(tsb.computeTokenSequence("0016415", 0, 7)).isEqualTo(TokenSequence.of(START, LEADING_ZERO, NUM_NO_LEADING_ZEROS, END));
        assertThat(tsb.computeTokenSequence("0309665.B.00", 0, 12)).isEqualTo(TokenSequence.of(START, LEADING_ZERO, NUM_NO_LEADING_ZEROS, DOT, ALPHA, DOT, NUM_NO_LEADING_ZEROS, END));
    }
}