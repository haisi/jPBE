/*
 * (c) Copyright 2021 Hasan Selman Kara All rights reserved.
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

    private static final BiFunction<Character, Token, Optional<Token>> defaultHook = (character, token) -> Optional.empty();

    public TokenSequenceBuilder(int maxLength, Tokens tokens) {
        this(maxLength, defaultHook, tokens);
    }

    public TokenSequenceBuilder(int maxLength, BiFunction<Character, Token, Optional<Token>> computeTokenForCharHook, Tokens tokens) {
        if (maxLength <= 0) throw new IllegalArgumentException("MaxLength cannot be smaller than 1");
        if (computeTokenForCharHook == null) throw new IllegalArgumentException("Hook cannot be null. Use default hook!");
        if (tokens == null) throw new IllegalArgumentException("Tokens cannot be null");

        this.maxLength = maxLength;
        this.computeTokenForCharHook = computeTokenForCharHook;
        this.tokens = tokens;
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
                // Handle first token
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
