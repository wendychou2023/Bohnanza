package io.bitbucket.plt.sdp.bohnanza.gui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * The graphical user interface manages an SWT window a draws the game board. All graphical items to be displayed
 * must be  created through this class. The GUI allows to drag'n'drop cards and to zoom in on a card. To zoom in
 * a user must right-click a card. It is then displayed in the configured zoom size in the foreground. Also the rest
 * of the GUI is dimmed down.
 * 
 * To activate the GUI, the method {@link #start()} must be invoked from the main thread.
 *  
 * @author bockisch
 *
 */
public class GUI {

    private CardObject cardBeingMoved = null;
    private CardObject zoomedCard = null;
    private int moveX;
    private int moveY;
    private int deltaX;
    private int deltaY;

    private Shell shell;
    private Display display;

    private List<CardObject> displayedCards = new LinkedList<CardObject>();
    private List<Compartment> compartments = new LinkedList<Compartment>();
    private List<Label> labels = new LinkedList<Label>();
    private List<Button> buttons = new LinkedList<Button>();

    private org.eclipse.swt.graphics.Color backgroundColor;
    private org.eclipse.swt.graphics.Color foregroundColor;
    private CardDnDHandler dndHandler;
    private final Canvas canvas;
    private final GC globalGc;
    private ScrolledComposite sc;
    private int scrollbarWidth;
    private int scrollbarHeight;
    private boolean redrawNeeded = true;
    private Image offScreen;

    /**
     * Create a new GUI. Using this constructor, the zoom size is the same as the regular card size.
     * 
     * @param initialWindowSize the size to create the window with. It can be resized lated by the user. May not be null.
     * @param cardSize the size in which card images are to be displayed. May not be null.
     * @param background the background color of the game board. May not be null.
     * @param foreground the color in which labels are printed and compartment boundaries are drawn. May not be null.
     * 
     * @see GUI#GUI(Size, Size, Size, Color, Color)
     */
    public GUI(Size initialWindowSize, Size cardSize, Color background, Color foreground) {
        this(initialWindowSize, cardSize, cardSize, background, foreground);
    }

    /**
     * Create a new GUI.
     * 
     * @param initialWindowSize the size to create the window with. It can be resized lated by the user. May not be null.
     * @param cardSize the size in which card images are to be displayed. May not be null.
     * @param zoomedCardSize the size in  which card images are displayed, when the user zooms in on them. Zooming
     * is achieved by right-clicking a card. May not be null.
     * @param background the background color of the game board. May not be null.
     * @param foreground the color in which labels are printed and compartment boundaries are drawn. May not be null.
     */
    public GUI(Size initialWindowSize, Size cardSize, Size zoomedCardSize, Color background, Color foreground) {
        display = new Display();
        shell = new Shell(display);
        shell.setLayout(new FillLayout());

        shell.setLayout(new FillLayout());
        // Create the ScrolledComposite to scroll horizontally and vertically
        sc = new ScrolledComposite(shell, SWT.H_SCROLL | SWT.V_SCROLL);

        sc.setLayout(new FillLayout());
        // Create a child composite to hold the controls
        Composite child = new Composite(sc, SWT.NONE);
        sc.setContent(child);

        canvas = new Canvas(child, SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE);

        child.setLayout(new FillLayout());
        sc.setMinSize(initialWindowSize.width, initialWindowSize.height);

        // Expand both horizontally and vertically
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);

        scrollbarHeight = sc.getHorizontalBar().getSize().y;
        scrollbarWidth = sc.getVerticalBar().getSize().x;

        globalGc = new GC(canvas);
        backgroundColor = new org.eclipse.swt.graphics.Color(display,
                new RGB(background.r, background.g, background.b));
        foregroundColor = new org.eclipse.swt.graphics.Color(display,
                new RGB(foreground.r, foreground.g, foreground.b));
        canvas.setBackground(backgroundColor);

        setupDnD(canvas);
        setupPainting(display, canvas);
        setupClosing();

        shell.setSize(initialWindowSize.width, initialWindowSize.height);

        Resources.init(display, cardSize.width, cardSize.height, zoomedCardSize.width, zoomedCardSize.height);

        
    }

    private void setupClosing() {
        shell.addListener(SWT.Close, e -> {
        	stop();
        });
    }

    private void setupPainting(Display display, final Canvas canvas) {
        canvas.addListener(SWT.Paint, e -> {
            synchronized (GUI.this) {
                if (redrawNeeded || offScreen != null) {
                    Rectangle bounds = getBounds();
                    if (offScreen == null || offScreen.getBounds().width != bounds.width
                            || offScreen.getBounds().height != bounds.height) {
                        if (offScreen != null)
                            offScreen.dispose();
                        offScreen = new Image(display, bounds.width, bounds.height);
                    }
                    GC gc = new GC(offScreen);
                    Rectangle client = canvas.getClientArea();
                    gc.setBackground(backgroundColor);
                    gc.fillRectangle(0, 0, client.width, client.height);

                    for (Compartment compartment : compartments) {
                        gc.setForeground(foregroundColor);
                        if (compartment.image != null)
                            gc.drawImage(compartment.image, compartment.upperLeft.x, compartment.upperLeft.y);

                        gc.drawRectangle(compartment.upperLeft.x, compartment.upperLeft.y, compartment.size.width,
                                compartment.size.height);
                        gc.drawText(compartment.label, compartment.upperLeft.x + Compartment.H_PADDING,
                                compartment.upperLeft.y + Compartment.V_PADDING);
                    }

                    for (CardObject karte : displayedCards) {
                        if (karte == cardBeingMoved) {
                            Image im = karte.getImage();
                            ImageData imData = im.getImageData();
                            imData.alpha = 128;
                            Image im2 = new Image(display, imData);
                            gc.drawImage(im2, karte.x, karte.y);

                        } else
                            gc.drawImage(karte.getImage(), karte.x, karte.y);
                    }

                    for (Label label : labels) {
                        gc.setForeground(foregroundColor);
                        gc.drawText(label.getLabel(), label.upperLeft.x, label.upperLeft.y);
                    }

                    if (cardBeingMoved != null) {
                        Image im = cardBeingMoved.getImage();
                        ImageData imData = im.getImageData();
                        imData.alpha = 196;
                        Image im2 = new Image(display, imData);
                        gc.drawImage(im2, moveX - deltaX, moveY - deltaY);

                    }

                    if (zoomedCard != null) {
                        gc.setAlpha(95);
                        gc.fillRectangle(client);
                        gc.setAlpha(255);
                        Image im = zoomedCard.getZoomedImage();
                        Coordinate zoomedUpperLeft = zoomedCard.getZoomedUpperLeftCorner();
                        gc.drawImage(im, zoomedUpperLeft.x, zoomedUpperLeft.y);
                    }

                    for (Button button : buttons) {
                        if (button.swtButton != null)
                            button.swtButton.setLocation(button.pos.x, button.pos.y);
                    }

                    gc.dispose();
                    redrawNeeded = false;
                }
                e.gc.drawImage(offScreen, 0, 0);
            }
        });
    }

    private void setupDnD(final Canvas canvas) {
        canvas.addListener(SWT.MouseDown, e -> {
            synchronized (GUI.this) {
                CardObject card = getCardAtPosition(new Coordinate(e.x, e.y));
                if (card == null)
                    return;

                if (e.button == 3) {
                    zoomedCard = card;
                    cardBeingMoved = null;
                } else {
                    zoomedCard = null;
                    cardBeingMoved = card;

                    deltaX = e.x - card.x;
                    deltaY = e.y - card.y;

                    moveX = e.x;
                    moveY = e.y;
                }
                canvas.redraw();
            }
        });

        canvas.addListener(SWT.MouseMove, e -> {
            synchronized (GUI.this) {
                moveX = e.x;
                moveY = e.y;
                canvas.redraw();
            }
        });

        canvas.addListener(SWT.MouseUp, e -> {
            synchronized (GUI.this) {
                if (dndHandler != null && cardBeingMoved != null) {
                    Coordinate dndResult = dndHandler.cardDraggedAndDropped(cardBeingMoved, new Coordinate(e.x, e.y),
                            new Coordinate(e.x - deltaX, e.y - deltaY));
                    if (dndResult != null) {
                        cardBeingMoved.x = dndResult.x;
                        cardBeingMoved.y = dndResult.y;
                    }
                }

                cardBeingMoved = null;
                zoomedCard = null;

                canvas.redraw();
            }
        });

    }

    private synchronized Rectangle getBounds() {
        Rectangle rect = new Rectangle(0, 0, 0, 0);
        for (CardObject karte : displayedCards) {
            Rectangle karteBounds = karte.getImage().getBounds();
            karteBounds = new Rectangle(karte.x, karte.y, karteBounds.width, karteBounds.height);
            rect = rect.union(karteBounds);
        }

        for (Compartment compartment : compartments) {
            rect = rect.union(new Rectangle(compartment.upperLeft.x, compartment.upperLeft.y, compartment.size.width,
                    compartment.size.height));
        }

        for (Label label : labels) {
            Point extent = stringExtent(label.getLabel());
            rect = rect.union(new Rectangle(label.upperLeft.x, label.upperLeft.y, extent.x, extent.y));
        }

        for (Button button : buttons) {
            rect = rect.union(new Rectangle(button.pos.x, button.pos.y, button.size.width, button.size.height));
        }

        return rect;
    }

    Point stringExtent(String string) {
        return globalGc.stringExtent(string);
    }

    /**
     * Activate the GUI, i.e., show the windows and execute the event dispatch loop to process user interaction.
     * This method blocks. It must be called from the main thread.
     */
    public void start() {
        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
        globalGc.dispose();
    }

    /**
     * Terminate the GUI, i.e., stop the event dispatch loop and close the window. This method can be called from
     * any thread.
     * 
     * This method is automatically called when the user closes the window.
     */
    public void stop() {
        display.syncExec(new Runnable() {

            @Override
            public void run() {
                if (!shell.isDisposed())
                    shell.dispose();
            }
        });
    }

    /**s
     * Create a new card object that is displayed by the GUI. This method can be called from any thread.
     * The display is refreshed accordingly.
     * 
     * @param cardType the type of card determines the front and back image. May not be null.
     * @param upperLeftCorner the initial position to display the card. May not be null.
     * @return
     */
    public synchronized CardObject addCard(CardType cardType, Coordinate upperLeftCorner) {
        CardObject newCardObject = new CardObject(upperLeftCorner.x, upperLeftCorner.y, false, cardType, this);
        displayedCards.add(newCardObject);
        updateScrollMinSize();
        redrawDisplay();
        return newCardObject;
    }

    private void updateScrollMinSize() {
        display.asyncExec(new Runnable() {

            @Override
            public void run() {
                synchronized (GUI.this) {
                    Rectangle bounds = getBounds();
                    sc.setMinSize(bounds.width + scrollbarWidth + 1, bounds.height + scrollbarHeight + 1);
                }
            }
        });
    }

    /**
     * Remove the card from the display. The card object should not be used afterwards and it cannot be added
     * to the GUI again. To put a representation of the card back on the GUI, a new one must be created by
     * {@link #addCard(CardType, Coordinate)}.
     * 
     * @param card
     */
    public synchronized void removeCard(CardObject card) {
        displayedCards.remove(card);
        updateScrollMinSize();
        redrawDisplay();
    }

    /**
     * Returns the card whose display overlaps with the provided coordinate. If multiple cards overlap
     * the one which is displayed top-most is returned.
     * @param pos may not be null.
     * @return
     */
    public synchronized CardObject getCardAtPosition(Coordinate pos) {
        for (int i = displayedCards.size() - 1; i >= 0; i--) {
            CardObject card = displayedCards.get(i);
            Rectangle bounds = card.getImage().getBounds();
            bounds = new Rectangle(card.x, card.y, bounds.width, bounds.height);
            if (bounds.contains(pos.x, pos.y)) {
                return card;
            }
        }
        return null;
    }

    /**
     * Create a new label object that is displayed by the GUI. This method can be called from any thread.
     * The display is refreshed accordingly.
     * 
     * @param upperLeft may not be null.
     * @param label
     * @return
     */
    public synchronized Label addLabel(Coordinate upperLeft, String label) {
        Label newLabel = new Label(this, upperLeft, label);
        labels.add(newLabel);
        updateScrollMinSize();
        redrawDisplay();
        return newLabel;
    }

    public synchronized void removeLabel(Label label) {
        labels.remove(label);
        updateScrollMinSize();
        redrawDisplay();
    }

    /**
     * Create a new compartment object that is displayed by the GUI with an empty background. This method can be called from any thread.
     * The display is refreshed accordingly.
     * 
     * @param upperLeft may not be null.
     * @param size may not be null
     * @param label
     * @return
     */
    public synchronized Compartment addCompartment(Coordinate upperLeft, Size size, String label) {
        return addCompartment(upperLeft, size, label, null);
    }

    /**
     * Create a new compartment object that is displayed by the GUI with an image as background. This method can be called from any thread.
     * The display is refreshed accordingly.
     * 
     * @param upperLeft may not be null.
     * @param size may not be null.
     * @param label
     * @param imageName the name of the image to be displayed as background of the compartment. There must be an image file with the name
     * <code>imageName + ".jpg"</code> on the class path. The image is resized to match the specifized <code>size</code> of the
     * compartment. 
     * @return
     */
    public synchronized Compartment addCompartment(Coordinate upperLeft, Size size, String label, String imageName) {
        Image image = null;
        if (imageName != null)
            image = Resources.getInstance().getImage(imageName, size.width, size.height);

        Compartment newCompartment = new Compartment(upperLeft, size, label, image, this);
        compartments.add(newCompartment);
        updateScrollMinSize();
        redrawDisplay();
        return newCompartment;
    }

    /**
     * Removes the compartment from the display. The compartment should not be used afterwards and it
     * cannot be added to the display again. To add an equivalent compartment again, use the method
     * {@link #addCompartment(Coordinate, Size, String)} or {@link #addCompartment(Coordinate, Size, String, String)}.
     * This method can be called from any thread. The display is refreshed accordingly.
     * @param compartment
     */
    public synchronized void removeCompartment(Compartment compartment) {
        compartments.remove(compartment);
        updateScrollMinSize();
        redrawDisplay();
    }

    /**
     * Create a new button object that is displayed by the GUI. This method can be called from any thread.
     * The display is refreshed accordingly.
     * 
     * @param pos may not be null.
     * @param size may not be null.
     * @param label
     * @param buttonHandler a call-back object that is is invoked when the button is pressed by the user.
     * @return
     */
    public synchronized Button addButton(String label, Coordinate pos, Size size, ButtonHandler buttonHandler) {
        final Button newButton = new Button(label, pos, size, buttonHandler);
        buttons.add(newButton);
        updateScrollMinSize();
        display.asyncExec(new Runnable() {

            @Override
            public void run() {
                org.eclipse.swt.widgets.Button swtButton = new org.eclipse.swt.widgets.Button(canvas, SWT.PUSH);
                swtButton.setText(label);
                swtButton.setLocation(pos.x, pos.y);
                swtButton.setSize(size.width, size.height);
                swtButton.addSelectionListener(new SelectionListener() {

                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        buttonHandler.buttonPressed(newButton);
                    }

                    @Override
                    public void widgetDefaultSelected(SelectionEvent e) {
                    }
                });
                newButton.swtButton = swtButton;
            }
        });
        return newButton;
    }

    public void removeButton(Button button) {
        display.asyncExec(new Runnable() {
            @Override
            public void run() {
                synchronized (GUI.this) {
                    if (buttons.contains(button)) {
                        button.swtButton.dispose();
                        buttons.remove(button);
                        updateScrollMinSize();
                        redrawDisplay();
                    }
                }
            }
        });
    }

    /**
     * Enables or disables a button. This method can be invoked from any thread.
     * The display is refreshed accordingly.
     * 
     * @param button may not be null.
     * @param enabled
     */
    public void setButtonEnabled(Button button, boolean enabled) {
        display.asyncExec(new Runnable() {

            @Override
            public void run() {
                button.swtButton.setEnabled(enabled);
            }
        });
    }

    /**
     * Makes the card to be displayed on top if multiple cards overlap at the same position. This method can be invoked from any thread.
     * The display is refreshed accordingly.

     * @param card the card must be managed by the GUI, i.e., it must not have been removed before. May not be null.
     */
    public synchronized void moveToTop(CardObject card) {
        if (!displayedCards.contains(card))
            throw new IllegalArgumentException("Card not displayed.");
        displayedCards.remove(card);
        displayedCards.add(card);
    }

    /**
     * Sets the drag'n'drop handler, which is called back whenever the user finishes a d'n'd action on a card. 
     * @param dndHandler may be null.
     */
    public void setCardDnDHandler(CardDnDHandler dndHandler) {
        this.dndHandler = dndHandler;
    }

    void redrawDisplay() {
        display.asyncExec(new Runnable() {

            @Override
            public void run() {
                synchronized (GUI.this) {
                    redrawNeeded = true;
                    canvas.redraw();
                }
            }
        });
    }

    /**
     * Returns all card objects that have are managed by this GUI, where the card
     * object's upper-left corner is within the bounds of the compartment.
     * 
     * @param compartment may not be null
     * @return
     */
    public synchronized CardObject[] getCardObjectsInCompartment(Compartment compartment) {
        List<CardObject> cards = new ArrayList<CardObject>();
        Rectangle compartmentBounds = new Rectangle(compartment.upperLeft.x, compartment.upperLeft.y,
                compartment.size.width, compartment.size.height);
        for (CardObject displayedCard : displayedCards) {
            if (compartmentBounds.contains(displayedCard.x, displayedCard.y))
                cards.add(displayedCard);
        }

        CardObject[] result = new CardObject[cards.size()];
        cards.toArray(result);
        return result;
    }

    /**
	 * initiallyFlipped flags of the all card objects are reset to false when the discard pile is shuffled.
	 */
    public void resetAllCardFlipFlags(CardObject[] discardedCards) {
        for (CardObject card : discardedCards) {
            card.resetFlipFlag();
        }
    }

    /**
     * initiallyFlipped flags of the all card objects are set to true when the card is drawn and added to player handcard.
     */
    public void setCardFlipFlags(CardObject[] discardedCards) {
        for (CardObject card : discardedCards) {
            card.setFlipFlagTrue();
        }
    }

}