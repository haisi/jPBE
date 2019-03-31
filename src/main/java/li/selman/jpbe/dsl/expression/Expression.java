package li.selman.jpbe.dsl.expression;

import li.selman.jpbe.dsl.DslElement;

import java.util.Optional;

/**
 * @author Hasan Selman Kara
 */
public interface Expression extends DslElement {

    Optional<String> apply(String s);

}
