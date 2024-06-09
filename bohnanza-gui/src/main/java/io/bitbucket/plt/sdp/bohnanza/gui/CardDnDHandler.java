package io.bitbucket.plt.sdp.bohnanza.gui;


/**
 * A functional interface for call-backs to handle drag'n'drop events of cards. The call-back is invoked
 * at the end of a DnD event, i.e., when the mouse button is released. This call-back can influence the position
 * where the card is actually droped.
 * 
 * @author bockisch
 *
 * @see GUI#setCardDnDHandler(CardDnDHandler)
 */
public interface CardDnDHandler {

	/**
	 * Called at the end of a drag'n'drop event of a card. 
	 * @param card the card which is dragged and dropped
	 * @param mouseCoordinate the coordinates of the mouse cursor when the mouse button was released
	 * @param newCoordinate the coordinates of the upper left corner of the card when the mouse button was released
	 * @return the coordinate of the upper left corner of the card where the card should be placed according to this handler.
	 * 	If the card should be placed exactly where the user dropped it, the argument <code>newCoordinate</code> must be returned.
	 */
    public Coordinate cardDraggedAndDropped(CardObject card, Coordinate mouseCoordinate, Coordinate newCoordinate);
    
}
