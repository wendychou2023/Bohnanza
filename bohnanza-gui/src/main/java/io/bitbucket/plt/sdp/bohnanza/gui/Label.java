package io.bitbucket.plt.sdp.bohnanza.gui;


/**
 * A wrapper for an SWT label. This class is mutable, but only the label text may change.
 * 
 * The button is managed by the GUI. Also using this wrapper class avoids the necessity of a dependency to SWT in client code.
 * 
 * A Label is created by the method {@link GUI#addLabel(Coordinate, String)}.
 * 
 * @author bockisch
 *
 */
public class Label {

    private final GUI gui;
    public final Coordinate upperLeft;
    private String label;

    Label(GUI gui, Coordinate upperLeft, String label) {
        this.gui = gui;
        this.upperLeft = upperLeft;
        this.label = label;
    }
    
    
    public String getLabel() {
        return label;
    }
    
    /**
     * Change the label text. The  GUI is refreshed accordingly.
     * @param label
     */
    public void updateLabel(String label) {
        this.label = label;
        gui.redrawDisplay();
    }


    @Override
    public String toString() {
        return "Label [label=" + label + ", upperLeft=" + upperLeft + "]";
    }

}
