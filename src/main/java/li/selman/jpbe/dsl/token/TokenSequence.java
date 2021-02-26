/*
 * (c) Copyright 2021 Hasan Selman Kara All rights reserved.
 */
package li.selman.jpbe.dsl.token;

import java.util.*;
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
        if (getSize() == 0 && other.getSize() == 0) {
            // empty token sequence
            return TokenSequence.of();
        }

        List<Token> newSeq = new ArrayList<>(tokens);
        if (getSize() > 0 && other.getSize() > 0 && other.getFirstElement().equals(newSeq.get(newSeq.size() - 1))) {
            newSeq.remove(newSeq.size() - 1);
        }

        newSeq.addAll(other.getTokens());
        return new TokenSequence(newSeq);
    }


    @Override
    public int getSize() {
        return 1;
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
        return String.format("TokenSeq(%s)", tokens);
    }
}
