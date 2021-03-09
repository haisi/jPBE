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
