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
package li.selman.jpbe.datastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import li.selman.jpbe.dsl.Expression;
import li.selman.jpbe.dsl.expression.ConstantStringExpression;
import li.selman.jpbe.dsl.expression.Expressions;
import org.junit.jupiter.api.Test;

/**
 * @author Hasan Selman Kara
 */
class StartToFinishPathTest {

    @Test
    void computeOptimalTraceExpression() {
        var a = new ConstantStringExpression("A");
        var b = new ConstantStringExpression("B");

        Expressions expected = new Expressions(List.of(a, b));

        Expression heavyExpression = mock(Expression.class, "Heavy Expression");
        when(heavyExpression.getDslWeight()).thenReturn(10000000);

        var graph = new Graph(2, List.of(
            // direct edge
            new Edge(0, 1, Set.of(a, heavyExpression)),
            new Edge(1, 2, Set.of(heavyExpression, b))
        ));

        var path = new StartToFinishPath(graph.getEdges(), graph.getMaxNode());
        Expressions actualOptimal = path.computeOptimalTraceExpression();

        assertThat(actualOptimal).isEqualTo(expected);
    }
}
