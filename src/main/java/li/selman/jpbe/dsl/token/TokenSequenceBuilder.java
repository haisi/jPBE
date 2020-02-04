package li.selman.jpbe.dsl.token;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hasan Selman Kara
 */
public class TokenSequenceBuilder {

    private final int maxLength;

    public TokenSequenceBuilder(int maxLength) {
        this.maxLength = maxLength;
    }

    // TODO sleep and look at this again
    public TokenSequence computeTokenSequence(String input, int from, int to) {
        List<Token> tokens = new ArrayList<>();
        if (from == 0) {
            tokens.add(Token.START);
        }

        var substr = input.substring(from, to);
        Token last = null;
        for (char c : substr.toCharArray()) {
            if (last == null) {
                last = computeTokenForChar(c, getLastOrNull(tokens));
                tokens.add(last);
            }

            Token next = computeTokenForChar(c, getLastOrNull(tokens));
            if (!last.equals(next)) {
                last = next;
                tokens.add(last);
            }

            if (tokens.size() > maxLength) {
                // Already to long, preemptive cancellation
                // TODO why return empty list and not `TokenSequence.of(tokens)`?
                return TokenSequence.of();
            }
        }

        if (to == input.length()) {
            tokens.add(Token.END);
        }

        // TODO hmmm
        if (tokens.size() > 0 && tokens.size() <= maxLength) {
            return TokenSequence.of(tokens);
        }

        return TokenSequence.of();
    }

    private <T> T getLastOrNull(List<T> list) {
        if (list.size() == 0) {
            return null;
        } else {
            return list.get(list.size() - 1);
        }
    }

    Token computeTokenForChar(char c, Token lastToken) {
        if ('0' == c && Token.START.equals(lastToken) ||
            '0' == c && Token.LEADING_ZERO.equals(lastToken)) {
            return Token.LEADING_ZERO;
        }

        if (Character.isAlphabetic(c)) {
            return Token.ALPHA;
        }

        if (Character.isDigit(c)) {
            // TODO Token.NUMBER_NO_LEAD_ZEROS
            return Token.NUMBER;
        }

        switch (c)
        {
            // TODO FIX
            case ' ': return Token.SPACE;
            case '.': return Token.DOT;
//            case '\\': return Token.BACKSLASH;
            case '/': return Token.SLASH;
//            case '-': return Token.HYPHEN;
            case '_': return Token.UNDERSCORE;
//            case '(': return Token.OPEN_BRACE;
//            case ')': return Token.CLOSE_BRACE;
            default: return Token.EVERYTHING_ELSE;
        }
    }

}
