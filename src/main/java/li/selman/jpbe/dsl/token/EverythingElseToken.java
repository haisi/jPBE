/*
 * (c) Copyright 2021 Hasan Selman Kara All rights reserved.
 */
package li.selman.jpbe.dsl.token;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Matches everything that all the other <b>used</b> tokens do not match.
 *
 * @author Hasan Selman Kara
 */
class EverythingElseToken extends Token {

    // both work
    // TODO(#optimization): check which regex negation is faster
//    private final static String negationPrefix = "(?![";
//    private final static String negationSuffix = "]).";
    private static final String negationPrefix = "[^\\^";
    private static final String negationSuffix = "]";

    private EverythingElseToken(Pattern pattern) {
        super(pattern);
    }

    public static EverythingElseToken generate(List<Token> usedTokens) {
        Stream<String> regexPattern = getRegexPattern(usedTokens);
        Stream<String> preparedForNegation = prepareForNegation(regexPattern);
        String orJoinedPattern = orJoinPatterns(preparedForNegation);
        String negatedPattern = negationPrefix + orJoinedPattern + negationSuffix;

        return new EverythingElseToken(Pattern.compile(negatedPattern));
    }

    private static String orJoinPatterns(Stream<String> preparedForNegation) {
        return preparedForNegation
            .collect(Collectors.joining("|"));
    }

    private static Stream<String> prepareForNegation(Stream<String> regexPattern) {
        return regexPattern
            .map(pattern -> pattern.replaceAll("[\\[\\]]", ""));
    }

    private static Stream<String> getRegexPattern(List<Token> usedTokens) {
        return usedTokens.stream()
            .map(Token::getPattern)
            .map(Pattern::pattern);
    }

    @Override
    public String toString() {
        return "ELSE";
    }
}
