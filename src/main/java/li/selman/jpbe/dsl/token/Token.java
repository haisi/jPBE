/*
 * (c) Copyright 2021 Hasan Selman Kara. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

    public static final Token START = new Token.StartToken();
    public static final Token END = new Token.EndToken();

    public static final Token ALPHA = new Token.AlphaToken();
    public static final Token LOWER_ALPHA = new Token.LowerAlphaToken();
    public static final Token UPPER_ALPHA = new Token.UpperAlphaToken();

    public static final Token LEADING_ZERO = new Token.LeadingZeroToken();
    public static final Token NUM = new Token.NumToken();
    public static final Token NUM_NO_LEADING_ZEROS = new NumNoLeadingZerosToken();
    public static final Token ALPHA_NUM = new Token.AlphaNumToken();
    public static final Token ALPHA_NUM_NO_LEADING_ZEROS = new Token.AlphaNumNoLeadingZerosToken();

    public static final Token SPACE = new Token.SpaceToken();

    public static final Token COLON = new Token.ColonToken();
    public static final Token SEMI_COLON = new Token.SemiColonToken();

    public static final Token DOT = new Token.DotToken();
    public static final Token COMMA = new Token.CommaToken();

    public static final Token HYPHEN = new Token.HyphenToken();
    public static final Token UNDERSCORE = new Token.UnderscoreToken();

    public static final Token BACK_SLASH = new Token.BackSlashToken();
    public static final Token FORWARD_SLASH = new Token.ForwardSlashToken();

    private final Pattern pattern;

    public Token(Pattern pattern) {
        if (pattern == null) throw new IllegalArgumentException("Pattern cannot be null");

        this.pattern = pattern;
    }

    public final Pattern getPattern() {
        return pattern;
    }

    public final String getRegexPattern() {
        return pattern.pattern();
    }

    /**
     * Check whether a string matches the pattern.
     * @param s to match
     * @return {@code true} if the s matches the Regex
     */
    public boolean matches(String s) {
        return pattern.matcher(s).matches();
    }

    /**
     * Checks whether a single character matches the token.
     * @param c to match
     * @return {@code true} if the c matches the Regex
     */
    public boolean matches(char c) {
        return matches(String.valueOf(c));
    }

    public boolean matches(String s, Token lastToken) {
        return matches(s);
    }

    public boolean matches(char c, Token lastToken) {
        return matches(c);
    }

    @Override
    public String toString() {
        return pattern.toString();
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

    // TODO(#idea): do we need Token.EVERYTHING ?
    //  When a token sequence is empty. It matches everything.
    //  I.e. we could represent it with `TokenSequence.of(List.of(Token.EVERYTHING))`
    private static class EverythingToken extends Token {
        EverythingToken() {
            super(Pattern.compile("(.*?)"));
        }
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

        @Override
        public boolean matches(String s, Token lastToken) {
            return ("0".equals(s) && Token.START.equals(lastToken))
                || ("0".equals(s) && Token.LEADING_ZERO.equals(lastToken));
        }

        @Override
        public boolean matches(char c, Token lastToken) {
            return this.matches(String.valueOf(c), lastToken);
        }
    }

    private static class NumNoLeadingZerosToken extends Token {
        NumNoLeadingZerosToken() {
            super(Pattern.compile("([1-9]+[0-9]*)"));
        }

        @Override
        public boolean matches(String s, Token lastToken) {
            if ("0".equals(s)) {
                return !Token.LEADING_ZERO.equals(lastToken);
            } else {
                return isPositiveNumeric(s);
            }
        }

        @Override
        public boolean matches(char c, Token lastToken) {
            if (c == '0') {
                return !Token.LEADING_ZERO.equals(lastToken);
            } else {
                return Character.isDigit(c);
            }
        }

        private static boolean isPositiveNumeric(String str) {
            for (char c : str.toCharArray()) {
                if (!Character.isDigit(c)) return false;
            }
            return true;
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

        @Override
        public String toString() {
            return "AN";
        }
    }

    private static class AlphaNumNoLeadingZerosToken extends Token {
        AlphaNumNoLeadingZerosToken() {
            super(Pattern.compile("([a-zA-Z1-9]+[0-9]*[a-zA-Z]*)"));
        }

        @Override
        public String toString() {
            return "AN-0";
        }
    }

    private static class SpaceToken extends Token {
        SpaceToken() {
            super(Pattern.compile("[\\s]+"));
        }

        @Override
        public String toString() {
            return "s+";
        }
    }

    private static class DotToken extends Token {
        DotToken() {
            super(Pattern.compile("[\\.]+"));
        }

        @Override
        public String toString() {
            return ".";
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
