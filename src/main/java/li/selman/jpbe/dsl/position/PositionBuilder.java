package li.selman.jpbe.dsl.position;

import li.selman.jpbe.dsl.token.Token;
import li.selman.jpbe.dsl.token.TokenSequence;
import li.selman.jpbe.dsl.token.TokenSequenceBuilder;
import li.selman.jpbe.dsl.token.Tokens;

import java.util.*;

/**
 * @author Hasan Selman Kara
 */
// TODO refactor
public class PositionBuilder {

    private int maxTokenSequenceLength = 2;

    private final Tokens tokens = new Tokens(List.of(
        Token.START,
        Token.END,
        Token.ALPHA,
        Token.NUM,
        Token.COMMA,
        Token.DOT
    ));
    private final TokenSequenceBuilder tokenSequenceBuilder = new TokenSequenceBuilder(maxTokenSequenceLength, tokens);

    private String lastString;
    private final Map<Integer, HashSet<Position>> cache = new HashMap<>();

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

        HashSet<Position> union = union(constantPositions, dynamicPositions);
        cache.put(k, union);
        return union;
    }

    private Set<Position> computeConstantPositions(String input, int k) {
        ConstantPosition positionFromTheLeft = new ConstantPosition(k);
        ConstantPosition positionFromTheRight = input.length() == k ? new ConstantPosition(Integer.MIN_VALUE) : new ConstantPosition(-(input.length() - k));

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
                        continue;
                    } else {
                        // TODO should not be need?
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
        for (int k2 = 0; k2 <= input.length(); k2++) {
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

    private HashSet<Position> union(Set<Position> a, Set<Position> b) {
        var union = new HashSet<Position>(a.size() + b.size());
        union.addAll(a);
        union.addAll(b);
        return union;
    }

}
