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

/**
 * @author Hasan Selman Kara
 */
public class PositionBuilder {

    private static final Logger logger = LoggerFactory.getLogger(PositionBuilder.class);

    private final TokenSequenceBuilder tokenSequenceBuilder;

    private String lastString;
    private final Map<Integer, Set<Position>> cache = new HashMap<>();

    public PositionBuilder(TokenSequenceBuilder tokenSequenceBuilder) {
        this.tokenSequenceBuilder = tokenSequenceBuilder;
    }

    /**
     * Generates all possible position which match a given index on a given string.
     *
     * @param input Input string
     * @param k     index of position to generate to
     * @return set of positions
     */
    public Set<Position> computePositions(String input, int k) {
        if (input.equals(lastString)) {
            cache.clear();
            lastString = input;
        } else if (cache.containsKey(k)) {
            return cache.get(k);
        }

        Set<Position> constantPositions = computeConstantPositions(input, k);
        Set<Position> dynamicPositions = computeDynamicPositions(input, k);

        Set<Position> union = union(constantPositions, dynamicPositions);
        cache.put(k, union);
        return union;
    }

    private Set<Position> computeConstantPositions(String input, int k) {
        ConstantPosition positionFromTheLeft = new ConstantPosition(k);
        ConstantPosition positionFromTheRight;
        if (input.length() == k) {
            positionFromTheRight = new ConstantPosition(Integer.MIN_VALUE);
        } else {
            positionFromTheRight = new ConstantPosition(-(input.length() - k));
        }

        return Set.of(
            positionFromTheLeft,
            positionFromTheRight
        );
    }

    private Set<Position> computeDynamicPositions(String input, int k) {
        Set<Position> dynamicPositions = new HashSet<>();

        Map<Integer, TokenSequence> leftTokenSeq = computeLeftTokenSeq(input, k);
        Map<Integer, TokenSequence> rightTokenSeq = computeRightTokenSeq(input, k);

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
                        logger.error("Matcher.positionOfRegex failed", ex);
                        continue;
                    } else {
                        // TODO(#bug): should not be need?
                        // bubble up
                        throw ex;
                    }
                }

                int cMax = Matcher.totalNumberOfMatches(r12, input);
                dynamicPositions.add(new DynamicPosition(leftEntry.getValue(), leftEntry.getValue(), c));
                dynamicPositions.add(new DynamicPosition(leftEntry.getValue(), leftEntry.getValue(), -(cMax - c + 1)));
            }

        }

        return dynamicPositions;
    }

    private Map<Integer, TokenSequence> computeRightTokenSeq(String input, int k) {
        Map<Integer, TokenSequence> rightTokenSeq = new HashMap<>();
        // TODO(#bug): check this out. It's the same in reference
        for (int k2 = k; k2 <= input.length(); k2++) {
            var tokenSequence = tokenSequenceBuilder.computeTokenSequence(input, k, k2);
            if (!tokenSequence.isEmpty()) {
                rightTokenSeq.put(k2, tokenSequence);
            }
        }
        return rightTokenSeq;
    }

    private Map<Integer, TokenSequence> computeLeftTokenSeq(String input, int k) {
        Map<Integer, TokenSequence> leftTokenSeq = new HashMap<>();
        for (int k1 = 0; k1 <= k; k1++) {
            var tokenSequence = tokenSequenceBuilder.computeTokenSequence(input, k1, k);
            if (!tokenSequence.isEmpty()) {
                leftTokenSeq.put(k1, tokenSequence);
            }
        }
        return leftTokenSeq;
    }

    private Set<Position> union(Set<Position> a, Set<Position> b) {
        var union = new HashSet<Position>(a.size() + b.size());
        union.addAll(a);
        union.addAll(b);
        return union;
    }

}
