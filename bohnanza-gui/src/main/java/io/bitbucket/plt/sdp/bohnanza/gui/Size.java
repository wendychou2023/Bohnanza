package io.bitbucket.plt.sdp.bohnanza.gui;

/**
 * A size consisting of a width and a height. This class is immutable.
 * @author bockisch
 *
 */
public class Size {
    
    public final int width, height;

    /**
     * Create a new size object.
     * 
     * @param width the width. Must be >= 0.
     * @param height the height. Must be >= 0.
     * 
     * @throws IllegalArgumentException if the width or height is negative. 
     */
    public Size(int width, int height) {
        if (width < 0 || height < 0)
            throw new IllegalArgumentException("Width and height must not be negative.");
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return "Size [width=" + width + ", height=" + height + "]";
    }

}
