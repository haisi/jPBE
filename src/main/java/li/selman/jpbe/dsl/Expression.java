package li.selman.jpbe.dsl;

import java.util.Optional;

/**
 * @author Hasan Selman Kara
 */
public interface Expression extends DslElement {

    Optional<String> apply(String s);

}
