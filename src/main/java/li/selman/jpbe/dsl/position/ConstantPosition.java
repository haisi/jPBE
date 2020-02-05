package li.selman.jpbe.dsl.position;

/**
 * @author Hasan Selman Kara
 */
public class ConstantPosition implements Position {

    private final int constant;

    public ConstantPosition(int constant) {
        this.constant = constant;
    }

    @Override
    public int evalToPosition(String s) {
        if (constant == Integer.MIN_VALUE) {
            return s.length();
        }

        return constant >= 0 ? constant : s.length() + constant;
    }

    @Override
    public int getSize() {
        return 1;
    }
}
