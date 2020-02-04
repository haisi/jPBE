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
public enum Token {
    ALPHA("[a-zA-Z]+") {
        @Override
        public String toString() {
            return "A";
        }
    },
    NUMBER("[0-9]+") {
        @Override
        public String toString() {
            return "N";
        }
    },
    LOWER("[a-z]+") {
        @Override
        public String toString() {
            return "L";
        }
    },
    UPPER("[A-Z]+") {
        @Override
        public String toString() {
            return "U";
        }
    },
    ALPHA_NUM("[a-zA-Z0-9]+") {
        @Override
        public String toString() {
            return "AN";
        }
    },
    START("^") {
        @Override
        public String toString() {
            return "S";
        }
    },
    LEADING_ZERO("(^)[0]+") {
        @Override
        public String toString() {
            return "0+";
        }
    },
    SLASH("[\\\\]+") {
        @Override
        public String toString() {
            return "\\";
        }
    },
    DOT("[\\.]+") {
        @Override
        public String toString() {
            return ".";
        }
    },
    END("$") {
        @Override
        public String toString() {
            return "-";
        }
    },
    UNDERSCORE("[_]+") {
        @Override
        public String toString() {
            return "_";
        }
    },
    SPACE("[\\s]+") {
        @Override
        public String toString() {
            return "s+";
        }
    },
    // TODO everything_else token
    //  - Negated regex is wrong
    //  - Dynamically generate depending on what is used
    EVERYTHING_ELSE("[^\\^$a-zA-Z0-9\\s+_\\\\/-\\\\.()]") {
        @Override
        public String toString() {
            return "ELSE";
        }
    };

    private final Pattern pattern;

    Token(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    public String getRegexPattern() {
        return this.pattern.pattern();
    }

    public Pattern getPattern() {
        return pattern;
    }

    /**
     * @param s to match
     * @return {@code true} if the s matches the Regex
     */
    public boolean matches(String s) {
        return pattern.matcher(s).matches();
    }
}

