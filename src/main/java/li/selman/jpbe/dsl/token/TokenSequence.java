/*
 * (c) Copyright 2021 Hasan Selman Kara All rights reserved.
 */
package li.selman.jpbe.dsl.token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import li.selman.jpbe.dsl.DslElement;

/**
 * Merges a list of tokens together to a new combined token.
 * The whole TokenSequence must match.
 * <p>
 * If no tokens are past, the sequence <b>matches everything</b>
 *
 * @author Hasan Selman Kara
 * @see TokenSequenceBuilder
 */
public class TokenSequence implements DslElement, Iterable<Token> {

    private final List<Token> tokens;
    private final Pattern mergedPattern;

    private TokenSequence(List<Token> tokens) {
        this.tokens = tokens;
        // 'and' merge all regex pattern together, i.e. the whole pattern must match
        // 'or' merge with an '|' pipe is wrong!
        final String regexPattern = tokens.stream().map(Token::getRegexPattern).collect(Collectors.joining());
        this.mergedPattern = Pattern.compile(regexPattern);
    }

    public static TokenSequence of(Token... tokens) {
        return new TokenSequence(Arrays.asList(tokens));
    }

    public static TokenSequence of(List<Token> tokens) {
        return new TokenSequence(tokens);
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public int getNumberOfTokens() {
        return tokens.size();
    }

    public boolean isEmpty() {
        return tokens.isEmpty();
    }

    /**
     * @return the last token or {@code null} if the sequence is empty
     */
    public Token getLastToken() {
        if (!tokens.isEmpty()) {
            return tokens.get(tokens.size() - 1);
        }
        return null;
    }

    /**
     * Is only called in {@link TokenSequence#union(TokenSequence)} and only if
     * the size of sequence is >= 0.
     * Therefore, returning null in the else case is not an issue.
     *
     * @return the first token in the sequence
     */
    private Token getFirstElement() {
        return tokens.size() >= 1 ? tokens.get(0) : null;
    }

    public Pattern getMergedPattern() {
        return mergedPattern;
    }

    public TokenSequence union(TokenSequence other) {
        if (sequenceLength() == 0 && other.sequenceLength() == 0) {
            // empty token sequence
            return TokenSequence.of();
        }

        List<Token> newSeq = new ArrayList<>(tokens);
        if (sequenceLength() > 0
            && other.sequenceLength() > 0
            && Objects.equals(other.getFirstElement(), newSeq.get(newSeq.size() - 1))) {
            newSeq.remove(newSeq.size() - 1);
        }

        newSeq.addAll(other.getTokens());
        return new TokenSequence(newSeq);
    }

    /**
     * @return the size of {@link TokenSequence#tokens}
     */
    public int sequenceLength() {
        return tokens.size();
    }

    /**
     * Note that the size of the DslElement and the size of {@link TokenSequence#tokens} can differ!
     * Do not use this method to get the number of tokens in the sequence.
     * @return the weight of the token TokenSequence
     */
    @Override
    public int getDslWeight() {
        // This implementation just happens to use the token list length as the DslElement size.
        return tokens.size();
    }

    @Override
    public Iterator<Token> iterator() {
        return tokens.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TokenSequence tokens1 = (TokenSequence) o;

        return tokens.equals(tokens1.tokens);
    }

    @Override
    public int hashCode() {
        return tokens.hashCode();
    }

    @Override
    public String toString() {
        return tokens.stream()
                .map(Token::toString)
                .collect(Collectors.joining(", ", "{", "}"));
    }
}
