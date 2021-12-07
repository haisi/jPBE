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

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Matches everything that all the other <b>used</b> tokens do not match.
 *
 * @author Hasan Selman Kara
 */
final class EverythingElseToken extends Token {

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
