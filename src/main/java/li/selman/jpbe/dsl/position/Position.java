package li.selman.jpbe.dsl.position;

import li.selman.jpbe.dsl.DslElement;

/**
 * @author Hasan Selman Kara
 */
public interface Position extends DslElement {

    int evalToPosition(String s);

}
