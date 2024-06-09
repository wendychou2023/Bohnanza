package io.bitbucket.plt.sdp.bohnanza.gui;


/**
 * A 2 dimensional coordinate. This class is immutable.
 * 
 * @author bockisch
 *
 */
public class Coordinate {
    
    public final int x, y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinate [x=" + x + ", y=" + y + "]";
    }
    
}
