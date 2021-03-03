/*
 * (c) Copyright 2021 Hasan Selman Kara All rights reserved.
 */

package li.selman.jpbe.dsl.position;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import li.selman.jpbe.dsl.token.Token;
import li.selman.jpbe.dsl.token.TokenSequence;
import li.selman.jpbe.dsl.token.TokenSequenceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicPositionBuilder implements PositionBuilder {

    private static final Logger log = LoggerFactory.getLogger(DynamicPositionBuilder.class);

    private final TokenSequenceBuilder tokenSequenceBuilder;

    public DynamicPositionBuilder(TokenSequenceBuilder tokenSequenceBuilder) {
        this.tokenSequenceBuilder = tokenSequenceBuilder;
    }

    @Override
    public Set<Position> computePositions(String input, int k) {
        if (k < 0) throw new IllegalArgumentException("k cannot be < 0");
        if (k > input.length()) throw new IllegalArgumentException("k cannot be > input.length()");

        Map<Integer, TokenSequence> leftTokenSeq = computeLeftTokenSeq(input, k);
        Map<Integer, TokenSequence> rightTokenSeq = computeRightTokenSeq(input, k);

        Set<Position> dynamicPositions = new HashSet<>();
        for (var leftEntry : leftTokenSeq.entrySet()) {
            for (var rightEntry : rightTokenSeq.entrySet()) {
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
            var tokenSequence = tokenSequenceBuilder.computeTokenSequence(input, k, k2);
            if (!tokenSequence.isEmpty()) {
                rightTokenSeq.put(k2, tokenSequence);
            }
        }
        return rightTokenSeq;
    }

    Map<Integer, TokenSequence> computeLeftTokenSeq(String input, int k) {
        Map<Integer, TokenSequence> leftTokenSeq = new HashMap<>();
        for (int k1 = 0; k1 < k; k1++) {
            var tokenSequence = tokenSequenceBuilder.computeTokenSequence(input, k1, k);
            if (!tokenSequence.isEmpty()) {
                leftTokenSeq.put(k1, tokenSequence);
            }
        }
        return leftTokenSeq;
    }
}
