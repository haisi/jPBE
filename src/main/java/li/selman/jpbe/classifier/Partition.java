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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import li.selman.jpbe.dsl.expression.Expressions;
import li.selman.jpbe.dsl.token.TokenSequence;
import li.selman.jpbe.dsl.token.TokenSequenceBuilder;

/**
 * @author Hasan Selman Kara
 */
// TODO(#wip): not even close to done
public class Partition {

    private final List<Conjunct> disjunctiveClassifiers = new ArrayList<>();
    private final Set<TokenSequence> predicates = new HashSet<>();

    private final Expressions expression;
    private final int maxPredicateSequenceLength;
    private final TokenSequenceBuilder tokenSequenceBuilder;

    public Partition(Expressions expression, int maxPredicateSequenceLength,
                     TokenSequenceBuilder tokenSequenceBuilder) {
        this.expression = expression;
        this.maxPredicateSequenceLength = maxPredicateSequenceLength;
        this.tokenSequenceBuilder = tokenSequenceBuilder;
    }

    public boolean matches(String input) {
        return disjunctiveClassifiers.stream().anyMatch(conjunct -> conjunct.matches(input));
    }

    public Set<TokenSequence> generatePredicates(List<String> inputs) {
        Set<TokenSequence> tokenSequences = new HashSet<>();
        for (String input : inputs) {
            TokenSequence ts = tokenSequenceBuilder.computeTokenSequence(input, 0, input.length());
            for (int i = 0; i < ts.getNumberOfTokens(); i++) {
                int endIndex = (Math.min(ts.getNumberOfTokens(), i + maxPredicateSequenceLength) - 1);
                for (int j = endIndex; j >= i; j--) {
//                    tokenSequences.add(ts.getRange(i, j));
                }
            }
        }
        return tokenSequences;
    }

    public Set<TokenSequence> getPredicates() {
        return Collections.unmodifiableSet(predicates);
    }

    @Override
    public String toString() {
        String classifiers = disjunctiveClassifiers.stream().map(Object::toString).collect(Collectors.joining(" ∧ "));
        return "CASE( {" + classifiers + "} )\n\t\t{" + expression + "}";
    }
}
