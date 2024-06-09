package io.bitbucket.plt.sdp.bohnanza.gui;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

/**
 * A representation of a playing card with a front and a back. This class is mutable, whereby
 * the card type cannot be changed and the position can only be changed from within the same package.
 * The position is changed implicitly through user interaction by the GUI class.
 * 
 * A CardObject is created by the method {@link GUI#addCard(CardType, Coordinate)}.
 * 
 * @author bockisch
 *
 */
public class CardObject {

    int x;
    int y;
    private boolean showFront;
    private CardType card;
    private GUI gui;

    /**
     * Create a new CardObject which is displayed by a GUI.
     * 
     * @param x x position of the left corner
     * @param y y position of the upper corner
     * @param showFront whether the front of the card is initially showing
     * @param card the type of playing card that is represented 
     * @param gui the GUI by which the card is displayed
     */
    CardObject(int x, int y, boolean showFront, CardType card, GUI gui) {
        super();
        this.x = x;
        this.y = y;
        this.showFront = showFront;
        this.card = card;
        this.gui = gui;
    }

    /**
     * Get the image that is currently displayed, depending on the current orientation of the card.
     * If it lies on the back (front is facing) the front image is returned. Otherwise the back image
     * is returned.
     * 
     * The dimension of the image is according to the specified regular size provided to the constructor of GUI.
     *  
     * @return
     * 
     * @see GUI#GUI(Size, Size, Size, Color, Color)
     * @see GUI#GUI(Size, Size, Color, Color)
     */
    public Image getImage() {
        if (showFront)
            return Resources.getInstance().getFrontImage(card);
        else
            return Resources.getInstance().getBackImage(card);
    }

   
    public Coordinate getUpperLeftCorner() {
        return new Coordinate(x, y);
    }
    
    /**
     * Alternates the orientation of the card. If it lies on the back (front is facing), it lies on its front
     * after invocation of this method, and vice versa. The GUI is updated accordingly.
     */
    public void flip() {
        showFront = !showFront;
        gui.redrawDisplay();
    }
    
    /**
     * Explicitly sets the orientation of the card. The GUI is updated accordingly.
     * @param showFront if <code>true</code> the card will lie on the back (front is facing),
     * otherwise, it will lie on its front (back is facing).
     */
    public void showFront(boolean showFront) {
        this.showFront = showFront;
        gui.redrawDisplay();
    }
    
    /**
     * Returns the orientation of the card.
     * 
     * @return <code>true</code> if the card lies on the back (front is facing), <code>false</code> otherwise.
     */
    public boolean isFrontShown() {
        return showFront;
    }

    /**
     * Get the x coordinate of the left side of the card when displayed in the regular size.
     * 
     * @return
     * 
     * @see GUI#GUI(Size, Size, Size, Color, Color)
     * @see GUI#GUI(Size, Size, Color, Color)
     */
    public int getX() {
        return x;
    }

    /**
     * Get the y coordinate of the upper side of the card when displayed in the regular size.
     * 
     * @return
     * 
     * @see GUI#GUI(Size, Size, Size, Color, Color)
     * @see GUI#GUI(Size, Size, Color, Color)
     */
    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "CardObject [card=" + card.name() + ", x=" + x + ", y=" + y + ", showFront=" + showFront + "]";
    }

    /**
     * Get a zoomed version of the image that is currently displayed, depending on the current orientation of the card.
     * If it lies on the back (front is facing) the front image is returned. Otherwise the back image
     * is returned.
     * 
     * The dimension of the image is according to the specified zoom size provided to the constructor of GUI.
     *  
     * @return
     * 
     * @see GUI#GUI(Size, Size, Size, Color, Color)
     * @see GUI#GUI(Size, Size, Color, Color)
     */
    public Image getZoomedImage() {
        if (showFront)
            return Resources.getInstance().getZoomedFrontImage(card);
        else
            return Resources.getInstance().getZoomedBackImage(card);
    }

    /**
     * Get the x coordinate of the upper left corner of the card when displayed in the zoomed size.
     * 
     * @return
     * 
     * @see GUI#GUI(Size, Size, Size, Color, Color)
     * @see GUI#GUI(Size, Size, Color, Color)
     */
    public Coordinate getZoomedUpperLeftCorner() {
        
        Rectangle normalBounds = getImage().getBounds();
        Rectangle zoomedBounds = getZoomedImage().getBounds();
        int deltaX = (zoomedBounds.width - normalBounds.width) / 2;
        int deltaY = (zoomedBounds.height - normalBounds.height) / 2;
        
        return new Coordinate(x - deltaX, y - deltaY);
    }
    
    /**
     * Get the type of the card.
     * @return
     */
    public CardType getCardType() {
        return card;
    }
}