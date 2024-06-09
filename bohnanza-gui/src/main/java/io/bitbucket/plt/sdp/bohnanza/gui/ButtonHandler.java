package io.bitbucket.plt.sdp.bohnanza.gui;


/**
 * A functional interface for call-backs to handle pressing a button.
 * 
 * @author bockisch
 *
 * @see Button
 * @see Button#Button(String, Coordinate, Size, ButtonHandler)
 */
public interface ButtonHandler {
    
	/**
	 * Called when a button is pressed by the user.
	 * 
	 * @param button The pressed button.
	 */
    public void buttonPressed(Button button);

}
