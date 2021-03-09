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
package li.selman.jpbe.classifier;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import li.selman.jpbe.dsl.token.Token;
import li.selman.jpbe.dsl.token.TokenSequence;

/**
 * Matches if the TokenSequence {@code r} occurs {@code k} times in a given string or
 * inverse if positive is false.
 *
 * @author Hasan Selman Kara
 * @see li.selman.jpbe.dsl.token.TokenSequence
 */
public final class Match implements Predicate {

    private final TokenSequence r;
    private final int k;
    private final boolean positive;

    private Match(TokenSequence r, int k, boolean positive) {
        if (r.getNumberOfTokens() == 0) throw new IllegalArgumentException("TokenSequence can't be empty.");
        // TODO(#bug): does the possibility '0' matches make sense?
        if (k < 0) throw new IllegalArgumentException("Number of matches can't be negative");


        this.r = r;
        this.k = k;
        this.positive = positive;
    }

    static Match positive(TokenSequence r, int k) {
        return new Match(r, k, true);
    }

    static Match negative(TokenSequence r, int k) {
        return new Match(r, k, false);
    }

    @Override
    public boolean matches(String s) {
        int numberOfMatches = getNumberOfMatches(s);

        if (positive) {
            return numberOfMatches >= k;
        } else {
            return !(numberOfMatches >= k);
        }
    }

    private int getNumberOfMatches(String s) {
        Matcher matcher = r.getMergedPattern().matcher(s);
        int numberOfMatches = 0;
        while (matcher.find()) {
            numberOfMatches++;
        }
        return numberOfMatches;
    }

    @Override
    public String toString() {
        String text = positive ? "P" : "N";
        String tokenSeqStr = r.getTokens().stream().map(Token::toString).collect(Collectors.joining(", ", "{", "}"));
        return MessageFormat.format("({0}, {1}, {2})", text, k, tokenSeqStr);
    }
}
