package io.bitbucket.plt.sdp.bohnanza.gui;


/**
 * A wrapper for an SWT push button widget. This class is immutable, except for the package private swtButton field.
 * Due to constraints of SWT, the underlying SWT button is created and initialized asynchronously.
 * 
 * The button is managed by the GUI. Also using this wrapper class avoids the necessity of a dependency to SWT in client code.
 * 
 * A button is created by the method {@link GUI#addButton(String, Coordinate, Size, ButtonHandler)}.
 * 
 * @author bockisch
 *
 */
public class Button {
    
    public final String label;
    
    public final Coordinate pos;
    
    public final Size size;
    
    public final ButtonHandler buttonHandler;
    
    /**
     * The underlying SWT push button object. This is for internal use only, do not use or modify this object directly.
     */
    org.eclipse.swt.widgets.Button swtButton;
    
    Button(String label, Coordinate pos, Size size, ButtonHandler buttonHandler) {
        super();
        this.label = label;
        this.pos = pos;
        this.size = size;
        this.buttonHandler = buttonHandler;
    }

    @Override
    public String toString() {
        return "Button [label=" + label + ", pos=" + pos + ", size=" + size + "]";
    }

    
}
