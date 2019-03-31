package li.selman.jpbe.dsl.token;

import li.selman.jpbe.dsl.DslElement;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Hasan Selman Kara
 */
public class TokenSequence implements DslElement, Iterable<Token> {

    private final List<Token> tokens;
    private final Pattern mergedPattern;

    private TokenSequence(List<Token> tokens) {
        this.tokens = tokens;
        // TODO what if regexPattern is empty?! Match everything or nothing?
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

}
