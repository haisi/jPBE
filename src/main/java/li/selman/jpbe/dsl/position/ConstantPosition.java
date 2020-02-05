package li.selman.jpbe.dsl.position;

/**
 * @author Hasan Selman Kara
 */
public class ConstantPosition implements Position {

    private static final int LAST_POSITION_INDEX = Integer.MIN_VALUE;

    private final int constant;

    public ConstantPosition(int constant) {
        this.constant = constant;
    }

    public static ConstantPosition lastPosition() {
        return new ConstantPosition(LAST_POSITION_INDEX);
    }

    @Override
    public int evalToPosition(String s) {
        if (constant == LAST_POSITION_INDEX) {
            return s.length();
        }

        return constant >= 0 ? constant : s.length() + constant;
    }

    @Override
    public int getSize() {
        return 1;
    }
}
