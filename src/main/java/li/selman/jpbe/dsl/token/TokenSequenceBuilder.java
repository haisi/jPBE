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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * @author Hasan Selman Kara
 */
public class TokenSequenceBuilder {

    private final int maxLength;
    private final BiFunction<Character, Token, Optional<Token>> computeTokenForCharHook;
    private final Tokens tokens;

    private static Optional<Token> defaultHook(Character character, Token token) {
        return Optional.empty();
    }

    public TokenSequenceBuilder(int maxLength, Tokens tokens) {
        this(maxLength, TokenSequenceBuilder::defaultHook, tokens);
    }

    public TokenSequenceBuilder(int maxLength, BiFunction<Character, Token, Optional<Token>> computeTokenForCharHook,
                                Tokens tokens) {
        if (maxLength <= 0) {
            throw new IllegalArgumentException("MaxLength cannot be smaller than 1");
        }
        if (computeTokenForCharHook == null) {
            throw new IllegalArgumentException("Hook cannot be null. Use default hook!");
        }
        if (tokens == null) {
            throw new IllegalArgumentException("Tokens cannot be null");
        }

        this.maxLength = maxLength;
        this.computeTokenForCharHook = computeTokenForCharHook;
        this.tokens = tokens;
    }

    /**
     * @param input the whole input string provided by the data set
     * @param from  start index for sub-string on {@code input}
     * @param to    end index for sub-string on {@code input}
     * @return sequence of tokens representing the token structure of a substring on {@code input}
     */
    @SuppressWarnings("checkstyle:CyclomaticComplexity")
    public TokenSequence computeTokenSequence(String input, int from, int to) {
        if (to < 1 || to > input.length()) throw new IllegalArgumentException("'to' index is invalid.");
        if (from < 0 || from >= input.length()) throw new IllegalArgumentException("'from' index is invalid.");
        if (to <= from) throw new IllegalArgumentException("'from' index must be smaller than 'to' index.");

        List<Token> tmpTokens = new ArrayList<>();

        if (from == 0) {
            tmpTokens.add(Token.START);
        }

        String substr = input.substring(from, to);
        Token last = null;
        for (int i = 0; i < substr.length(); i++) {
            char character = substr.charAt(i);
            if (last == null) {
                // Handle first token
                last = computeTokenForChar(character, getLastOrNull(tmpTokens));
                tmpTokens.add(last);
            }

            Token next = computeTokenForChar(character, getLastOrNull(tmpTokens));
            if (!last.equals(next)) {
                last = next;
                tmpTokens.add(last);
            }

            if (tmpTokens.size() > maxLength) {
                // Already too long, preemptive cancellation with current tokens
                return TokenSequence.of(tmpTokens);
            }
        }

        if (to == input.length()) {
            tmpTokens.add(Token.END);
        }

        return TokenSequence.of(tmpTokens);
    }

    private <T> T getLastOrNull(List<T> list) {
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(list.size() - 1);
        }
    }

    Token computeTokenForChar(char c, Token lastToken) {

        Optional<Token> hookToken = computeTokenForCharHook.apply(c, lastToken);
        if (hookToken.isPresent()) {
            return hookToken.get();
        }

        return tokens.getTokens().stream()
                .filter(token -> token.matches(c, lastToken))
                .findFirst()
                .orElse(tokens.getElseToken());
    }

}
