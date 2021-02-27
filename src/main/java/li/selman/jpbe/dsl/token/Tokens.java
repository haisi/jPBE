/*
 * (c) Copyright 2021 Hasan Selman Kara All rights reserved.
 */
package li.selman.jpbe.dsl.token;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hasan Selman Kara
 */
public class Tokens {

    private final List<Token> tokens;
    private final EverythingElseToken elseToken;

    public Tokens(List<Token> tokens) {
        if (tokens.isEmpty()) throw new IllegalArgumentException("Tokens cannot be empty");

        this.elseToken = EverythingElseToken.generate(tokens);

        this.tokens = combine(tokens, elseToken);
    }

    private List<Token> combine(List<Token> otherTokens, Token otherElseToken) {
        List<Token> tokensWithElseToken = new ArrayList<>(otherTokens.size() + 1);
        tokensWithElseToken.addAll(otherTokens);
        tokensWithElseToken.add(otherElseToken);
        return tokensWithElseToken;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public EverythingElseToken getElseToken() {
        return elseToken;
    }
}
