package li.selman.jpbe.datastructure;

import li.selman.jpbe.dsl.ConstStr.ConstantStringExpression;
import li.selman.jpbe.dsl.Expression;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Hasan Selman Kara
 */
class StartToFinishPathTest {

    @Test
    void computeOptimalTraceExpression() {
        var a = new ConstantStringExpression("A");
        var b = new ConstantStringExpression("B");

        TraceExpression expected = new TraceExpression(List.of(a, b));

        Expression heavyExpression = mock(Expression.class, "Heavy Expression");
        when(heavyExpression.getSize()).thenReturn(10000000);

        var graph = new Graph(2, List.of(
            // direct edge
            new Edge(0, 1, Set.of(a, heavyExpression)),
            new Edge(1, 2, Set.of(heavyExpression, b))
        ));

        var path = new StartToFinishPath(graph.getEdges(), graph.getMaxNode());
        TraceExpression actualOptimal = path.computeOptimalTraceExpression();

        assertThat(actualOptimal).isEqualTo(expected);
    }
}
