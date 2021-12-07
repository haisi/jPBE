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

package li.selman.jpbe.dsl.position;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import li.selman.jpbe.dsl.token.Token;
import li.selman.jpbe.dsl.token.TokenSequence;
import li.selman.jpbe.dsl.token.TokenSequenceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicPositionBuilder implements PositionBuilder {

    // com.palantir.logsafe.logger.SafeLogger;
    // https://github.com/palantir/gradle-baseline/blob/develop/baseline-error-prone/src/test/java/com/palantir/baseline/errorprone/PreferSafeLoggerTest.java#L46
    private static final Logger log = LoggerFactory.getLogger(DynamicPositionBuilder.class);

    private final TokenSequenceBuilder tokenSequenceBuilder;

    public DynamicPositionBuilder(TokenSequenceBuilder tokenSequenceBuilder) {
        this.tokenSequenceBuilder = tokenSequenceBuilder;
    }

    @Override
    public Set<Position> computePositions(String input, int k) {
        Preconditions.checkArgument(k >= 0, "k cannot be < 0");
        Preconditions.checkArgument(k <= input.length(), "k cannot be > input.length()");

        Map<Integer, TokenSequence> leftTokenSeq = computeLeftTokenSeq(input, k);
        Map<Integer, TokenSequence> rightTokenSeq = computeRightTokenSeq(input, k);

        Set<Position> dynamicPositions = new HashSet<>();
        for (Entry<Integer, TokenSequence> leftEntry : leftTokenSeq.entrySet()) {
            for (Entry<Integer, TokenSequence> rightEntry : rightTokenSeq.entrySet()) {
                if (Objects.equals(leftEntry.getValue().getLastToken(), rightEntry.getValue().getLastToken())
                            && !Objects.equals(leftEntry.getValue().getLastToken(), Token.START)
                            && !Objects.equals(leftEntry.getValue().getLastToken(), Token.END)) {
                    // No valid token combination
                    continue;
                }

                TokenSequence r12 = leftEntry.getValue().union(rightEntry.getValue());
                int c;
                try {
                    c = Matcher.positionOfRegex(r12, input, leftEntry.getKey(), rightEntry.getKey());
                } catch (Exception ex) {
                    if (ex instanceof IllegalStateException) {
                        // TODO(#api): check if this ever happens and whether we should return Optional in Matcher
                        //  .positionOfRegex
                        log.error("Matcher.positionOfRegex failed", ex);
                        continue;
                    } else {
                        // TODO(#bug): should not be need?
                        // bubble up
                        throw ex;
                    }
                }

                int cMax = Matcher.totalNumberOfMatches(r12, input);
                dynamicPositions.add(new DynamicPosition(leftEntry.getValue(), rightEntry.getValue(), c));
                dynamicPositions.add(new DynamicPosition(leftEntry.getValue(), rightEntry.getValue(), -(cMax - c + 1)));
            }

        }

        return dynamicPositions;
    }

    Map<Integer, TokenSequence> computeRightTokenSeq(String input, int k) {
        Map<Integer, TokenSequence> rightTokenSeq = new HashMap<>();
        for (int k2 = k + 1; k2 <= input.length(); k2++) {
            TokenSequence tokenSequence = tokenSequenceBuilder.computeTokenSequence(input, k, k2);
            if (!tokenSequence.isEmpty()) {
                rightTokenSeq.put(k2, tokenSequence);
            }
        }
        return rightTokenSeq;
    }

    Map<Integer, TokenSequence> computeLeftTokenSeq(String input, int k) {
        Map<Integer, TokenSequence> leftTokenSeq = new HashMap<>();
        for (int k1 = 0; k1 < k; k1++) {
            TokenSequence tokenSequence = tokenSequenceBuilder.computeTokenSequence(input, k1, k);
            if (!tokenSequence.isEmpty()) {
                leftTokenSeq.put(k1, tokenSequence);
            }
        }
        return leftTokenSeq;
    }
}
