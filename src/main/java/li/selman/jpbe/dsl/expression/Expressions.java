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
package li.selman.jpbe.dsl.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import li.selman.jpbe.dsl.DslElement;
import li.selman.jpbe.dsl.Expression;

/**
 * Represents a full program {@code p} which can be applied on a input string to get the intended output.
 * {@code p} is made up of {@code n} {@link Expression}s, while itself being an {@link Expression}.
 * @author Hasan Selman Kara
 */
public class Expressions implements Expression {

    private final List<Expression> expressions;

    public Expressions(List<Expression> expressions) {
        this.expressions = expressions;
    }

    /**
     * Applies all expressions on {@code s} and joins the results in order.
     *
     * @param s input string
     * @return the whole output string
     */
    @Override
    public Optional<String> apply(final String s) {
        List<String> substrings = new ArrayList<>();
        for (var expression : expressions) {
            Optional<String> outputSubstring = expression.apply(s);
            if (outputSubstring.isEmpty()) {
                // Applying failed
                return Optional.empty();
            }

            substrings.add(outputSubstring.get());
        }

        String output = String.join("", substrings);
        return Optional.of(output);
    }

    @Override
    public int getDslWeight() {
        return expressions.stream().mapToInt(DslElement::getDslWeight).sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Expressions that = (Expressions) o;

        return expressions.equals(that.expressions);
    }

    @Override
    public int hashCode() {
        return expressions.hashCode();
    }

    @Override
    public String toString() {
        return String.format("TraceExpression(%s)", expressions);
    }
}
