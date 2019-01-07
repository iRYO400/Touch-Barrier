package wei.mark.standout;

import android.view.WindowManager;

public abstract class BaseStandOutLayoutParams extends WindowManager.LayoutParams {

    /**
     * Special value for x position that represents the left of the screen.
     */
    public static final int LEFT = 0;
    /**
     * Special value for y position that represents the top of the screen.
     */
    public static final int TOP = 0;
    /**
     * Special value for x position that represents the right of the screen.
     */
    public static final int RIGHT = Integer.MAX_VALUE;
    /**
     * Special value for y position that represents the bottom of the
     * screen.
     */
    public static final int BOTTOM = Integer.MAX_VALUE;
    /**
     * Special value for x or y position that represents the center of the
     * screen.
     */
    public static final int CENTER = Integer.MIN_VALUE;
    /**
     * Special value for x or y position which requests that the system
     * determine the position.
     */
    public static final int AUTO_POSITION = Integer.MIN_VALUE + 1;

    /**
     * Optional constraints of the window.
     */
    public int minWidth, minHeight, maxWidth, maxHeight;

    /**
     * The distance that distinguishes a tap from a drag.
     */
    public int threshold;

    BaseStandOutLayoutParams(int _type, int _flags, int _format) {
        super(_type, _flags, _format);
    }

    public abstract void setFocusFlag(boolean focused);
}
