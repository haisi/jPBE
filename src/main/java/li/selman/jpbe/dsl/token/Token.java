package li.selman.jpbe.dsl.token;

import java.util.regex.Pattern;

/**
 * Restrictions on Regular Expressions:
 * <ul>
 * <li>Kleene star is restricted to <b>one or more</b> occurrences (instead of zero or more)</li>
 * <li>No disjunction operator allowed (or-operator)</li>
 * </ul>
 * These restrictions are in place to efficiently enumerate regular expressions.
 *
 * @author Hasan Selman Kara
 */
public abstract class Token {

    public final static Token START = new Token.StartToken();
    public final static Token END = new Token.EndToken();

    public final static Token ALPHA = new Token.AlphaToken();
    public final static Token LOWER_ALPHA = new Token.LowerAlphaToken();
    public final static Token UPPER_ALPHA = new Token.UpperAlphaToken();

    public final static Token LEADING_ZERO = new Token.LeadingZeroToken();
    public final static Token NUM = new Token.NumToken();
    public final static Token ALPHA_NUM = new Token.AlphaNumToken();

    public final static Token SPACE = new Token.SpaceToken();

    public final static Token COLON = new Token.ColonToken();
    public final static Token SEMI_COLON = new Token.SemiColonToken();

    public final static Token DOT = new Token.DotToken();
    public final static Token COMMA = new Token.CommaToken();

    public final static Token HYPHEN = new Token.HyphenToken();
    public final static Token UNDERSCORE = new Token.UnderscoreToken();

    public final static Token BACK_SLASH = new Token.BackSlashToken();
    public final static Token FORWARD_SLASH = new Token.ForwardSlashToken();

    private final Pattern pattern;

    public Token(Pattern pattern) {
        if (pattern == null) throw new IllegalArgumentException("Pattern cannot be null");

        this.pattern = pattern;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public String getRegexPattern() {
        return pattern.pattern();
    }

    /**
     * @param s to match
     * @return {@code true} if the s matches the Regex
     */
    public boolean matches(String s) {
        return pattern.matcher(s).matches();
    }

    /**
     * @param c to match
     * @return {@code true} if the c matches the Regex
     */
    public boolean matches(char c) {
        return matches(String.valueOf(c));
    }

    @Override
    public String toString() {
        return "Token{" + pattern + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        return pattern.equals(token.pattern);
    }

    @Override
    public int hashCode() {
        return pattern.hashCode();
    }

    private static class StartToken extends Token {
        StartToken() {
            super(Pattern.compile("^"));
        }
    }

    private static class EndToken extends Token {
        EndToken() {
            super(Pattern.compile("$"));
        }
    }

    private static class AlphaToken extends Token {
        AlphaToken() {
            super(Pattern.compile("[a-zA-Z]+"));
        }
    }

    private static class LowerAlphaToken extends Token {
        LowerAlphaToken() {
            super(Pattern.compile("[a-z]+"));
        }
    }

    private static class UpperAlphaToken extends Token {
        UpperAlphaToken() {
            super(Pattern.compile("[A-Z]+"));
        }
    }

    private static class LeadingZeroToken extends Token {
        LeadingZeroToken() {
            super(Pattern.compile("(^)[0]+"));
        }
    }

    private static class NumToken extends Token {
        NumToken() {
            super(Pattern.compile("[0-9]+"));
        }
    }

    private static class AlphaNumToken extends Token {
        AlphaNumToken() {
            super(Pattern.compile("[a-zA-Z0-9]+"));
        }
    }

    private static class SpaceToken extends Token {
        SpaceToken() {
            super(Pattern.compile("[\\s]]+"));
        }
    }

    private static class DotToken extends Token {
        DotToken() {
            super(Pattern.compile("[\\.]+"));
        }
    }

    private static class ColonToken extends Token {
        ColonToken() {
            super(Pattern.compile("[:]+"));
        }
    }

    private static class CommaToken extends Token {
        CommaToken() {
            super(Pattern.compile("[\\,]+"));
        }
    }

    private static class SemiColonToken extends Token {
        SemiColonToken() {
            super(Pattern.compile("[;]+"));
        }
    }

    private static class BackSlashToken extends Token {
        BackSlashToken() {
            super(Pattern.compile("[\\\\]+"));
        }
    }

    private static class ForwardSlashToken extends Token {
        ForwardSlashToken() {
            super(Pattern.compile("[/]+"));
        }
    }

    private static class HyphenToken extends Token {
        HyphenToken() {
            super(Pattern.compile("[-]+"));
        }
    }

    private static class UnderscoreToken extends Token {
        UnderscoreToken() {
            super(Pattern.compile("[_]+"));
        }
    }
}
