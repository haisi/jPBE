package li.selman.jpbe.datastructure;

import li.selman.jpbe.dsl.ConstStr.ConstStrExpressionBuilder;
import li.selman.jpbe.dsl.ConstStr.ConstantStringExpression;
import li.selman.jpbe.dsl.Expression;
import li.selman.jpbe.dsl.ExpressionBuilder;
import li.selman.jpbe.dsl.expression.SubstringExpression;
import li.selman.jpbe.dsl.expression.SubstringExpressionBuilder;
import li.selman.jpbe.dsl.position.ConstantPosition;
import li.selman.jpbe.dsl.position.PositionBuilder;
import li.selman.jpbe.dsl.token.TokenSequenceBuilder;
import li.selman.jpbe.dsl.token.Tokens;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static li.selman.jpbe.dsl.token.Token.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Hasan Selman Kara
 */
class GraphBuilderTest {

    /**
     * If we return an expression for each substring, we should get {@code n * (n - 1) / 2} edges,
     * where {@code n} is {@code outputStr.length + 1}.
     */
    @Test
    @DisplayName("Max possible edges")
    void maxEdges() {
        ExpressionBuilder mockExpBuilder = mock(ExpressionBuilder.class);
        when(mockExpBuilder.computeExpressions(anyString(), anyString()))
            .thenReturn(List.of(mock(Expression.class)));

        var graphBuilder = new GraphBuilder(List.of(mockExpBuilder));
        var outputs = List.of("", "1", "12", "123", "1234", "12345", "123456");
        for (String output : outputs) {
            Graph graph = graphBuilder.createAllPrograms("doesn't matter", output);
            assertThat(graph.getMaxNode()).isEqualTo(output.length());

            int n = output.length() + 1;
            int expectedNodeCount = n * (n - 1) / 2;
            assertThat(graph.getEdges().size()).isEqualTo(expectedNodeCount);
        }
    }

    @Test
    @DisplayName("Create all programs with only ConstStrBuilder")
    void createAllProgramsWithConstStrBuilderTest() {
        var graphBuilder = new GraphBuilder(List.of(new ConstStrExpressionBuilder()));
        // n * (n-1) / 2 nodes
        Graph actualGraph = graphBuilder.createAllPrograms("Doesn't matter", "ABC");
        assertThat(actualGraph.getEdges()).containsExactlyInAnyOrder(
            new Edge(0, 1, Set.of(new ConstantStringExpression("A"))),
            new Edge(1, 2, Set.of(new ConstantStringExpression("B"))),
            new Edge(2, 3, Set.of(new ConstantStringExpression("C"))),
            new Edge(0, 2, Set.of(new ConstantStringExpression("AB"))),
            new Edge(1, 3, Set.of(new ConstantStringExpression("BC"))),
            new Edge(0, 3, Set.of(new ConstantStringExpression("ABC")))
        );
    }

    @Nested
    class FindingPaths {

        @Test
        void foo() {
            Expression heavyExpression = mock(Expression.class, "Heavy Expression");
            when(heavyExpression.getSize()).thenReturn(10000000);

            var graph = new Graph(2, List.of(
                // direct edge
                new Edge(0, 2, Set.of(new ConstantStringExpression("AB"), heavyExpression)),
                new Edge(0, 1, Set.of(new ConstantStringExpression("A"), heavyExpression)),
                new Edge(1, 2, Set.of(new ConstantStringExpression("B"), heavyExpression))
            ));

            var actual = graph.computeLocalOptimaPath();

            System.out.println(actual);
        }

    }

    @Test
    void integrationTest() {
        Tokens tokens = new Tokens(List.of(START, END, ALPHA, NUM, COMMA, DOT));
        int maxTokenSeqLength = 2;
        var positionBuilder = new PositionBuilder(new TokenSequenceBuilder(maxTokenSeqLength, tokens));

        ExpressionBuilder constStrExpBuilder = new ConstStrExpressionBuilder();
        ExpressionBuilder substringExpBuilder = new SubstringExpressionBuilder(positionBuilder);

        var graphBuilder = new GraphBuilder(List.of(constStrExpBuilder, substringExpBuilder));

        List<Expression> foo = List.of(
            new ConstantStringExpression("Smith"),
            new SubstringExpression(new ConstantPosition(6), new ConstantPosition(10))
        );

        Graph graph = graphBuilder.createAllPrograms("Peter,Smith,1990", "Smith");
//        List<TraceExpression> traceExpressions = graph.computeLocalOptimaPath();
//
//        assertThat(traceExpressions.get(0).getExpressions()).containsAll(foo);
//
//        for (TraceExpression traceExpression : traceExpressions) {
//            System.out.println(traceExpression.apply("Peter,Smith,1990"));
//        }
    }

}
