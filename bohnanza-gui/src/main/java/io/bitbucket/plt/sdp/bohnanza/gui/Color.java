package io.bitbucket.plt.sdp.bohnanza.gui;


/**
 * A data object for red-green-blue coded colors. This class is immutable.
 * 
 * Using this wrapper class avoids the necessity of a dependency to SWT in client code.
 */
public class Color {

    public final int r, g, b;

    /**
     * Create a new color based on the red, green and blue channels.
     * 
     * @param r value of the red channel, a value >= 0 and <= 255
     * @param g value of the green channel, a value >= 0 and <= 255
     * @param b value of the blue channel, a value >= 0 and <= 255
     * 
     * @throws IllegalArgumentException if the value of a channel is outside the allowed range
     */
    public Color(int r, int g, int b) {
        if (r < 0 || r > 255)
            throw new IllegalArgumentException("Red value must be between 0 and 255 (inclusive).");
        if (g < 0 || g > 255)
            throw new IllegalArgumentException("Green value must be between 0 and 255 (inclusive).");
        if (b < 0 || b > 255)
            throw new IllegalArgumentException("Blue value must be between 0 and 255 (inclusive).");
        this.r = r;
        this.g = g;
        this.b = b;
    }

    @Override
    public String toString() {
        return "Color [r=" + r + ", g=" + g + ", b=" + b + "]";
    }
    
    
    
}
