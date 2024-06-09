package io.bitbucket.plt.sdp.bohnanza.gui;

import java.util.Arrays;
import java.util.Objects;

import org.eclipse.swt.graphics.Image;

/**
 * A compartment is a rectangular area on the GUI, which is visually recognizable and
 * which can be used to identify cards located within the compartmen's boundary. Cards
 * can be moved to a compartment and aligned within it.
 * 
 * This class is immutable.
 * 
 * A compartment is created by the method {@link GUI#addCompartment(Coordinate, Size, String)}
 * of {@link GUI#addCompartment(Coordinate, Size, String, String)}.
 * 
 * @author bockisch
 *
 * @see GUI#getCardObjectsInCompartment(Compartment)
 * @see CardObject
 */
public class Compartment {

	/**
	 * The horizontal space in pixels that is to be left empty when distributing
	 * cards horizontally across a compartment.
	 * @see #distributeHorizontal(CardObject[]) 
	 */
    public static final int H_PADDING = 5;

    /**
	 * The vertical space in pixels that is to be left empty when distributing
	 * cards vertically across a compartment.
	 * @see #distributeVertical(CardObject[]) 
	 */
    public static final int V_PADDING = 5;

    public final Coordinate upperLeft;
    public final Size size;
    public final String label;
    public final Image image;
    private final GUI gui;
    
    Compartment(Coordinate upperLeft, Size size, String label, Image image, GUI gui) {
        super();
        this.upperLeft = upperLeft;
        this.size = size;
        this.label = label;
        this.image = image;
        this.gui = gui;
    }

	private int getVEnd() {
		return upperLeft.y + size.height - V_PADDING;
	}

	private int getVStart() {
		return gui.stringExtent(label).y + V_PADDING + upperLeft.y + V_PADDING;
	}

	private int getHEnd() {
		return upperLeft.x + size.width - H_PADDING;
	}

	private int getHStart() {
		return upperLeft.x + H_PADDING;
	}
    
    @Override
    public String toString() {
        return "Compartment [label=" + label + ", upperLeft=" + upperLeft + ", size=" + size + "]";
    }
 
    /**
     * Evenly distributes the y-coordinates of the provided cards across the height of this compartment.
     * The x-coordinates are not changed, such that the provided cards may not be located within this
     * compartment after calling this method, if their horizontal position is out of range.
     * 
     * The GUI is refreshed after moving the cards.
     * 
     * The x-coordinate of the cards can also be moved into the boundary of the compartment by additionally
     * calling {@link #distributeHorizontal(CardObject[])} or {@link #centerHorizontal(CardObject[])}.
     * 
     * @param cards May not be null.
     * 
     * @see Compartment#V_PADDING, {@link Compartment#distribute(int[], int, int)}
     */
    public void distributeVertical(CardObject[] cards) {
    	Objects.requireNonNull(cards);
    	int[] cardHeights = new int[cards.length];
    	for (int i = 0; i < cards.length; i++) {
    		cardHeights[i] = cards[i].getImage().getBounds().height;
    	}
    	
        int[] yPositions = Compartment.distribute(cardHeights, getVStart(), getVEnd());

    	for (int i = 0; i < yPositions.length; i++) {
    		cards[i].y = yPositions[i];
    	}
        gui.redrawDisplay();
    }

    /**
     * Evenly distributes the x-coordinates of the provided cards across the width of this compartment.
     * The y-coordinates are not changed, such that the provided cards may not be located within this
     * compartment after calling this method, if their vertical position is out of range.
     * 
     * The GUI is refreshed after moving the cards.
     * 
     * The x-coordinate of the cards can also be moved into the boundary of the compartment by additionally
     * calling {@link #distributeVertical(CardObject[])} or {@link #centerVertical(CardObject[])}.
     * 
     * @param cards May not be null.
     * 
     * @see Compartment#H_PADDING, {@link Compartment#distribute(int[], int, int)}
     */
    public void distributeHorizontal(CardObject[] cards) {
    	Objects.requireNonNull(cards);
    	int[] cardWidths = new int[cards.length];
    	for (int i = 0; i < cards.length; i++) {
    		cardWidths[i] = cards[i].getImage().getBounds().width;
    	}
    	
        int[] xPositions = Compartment.distribute(cardWidths, getHStart(), getHEnd());

    	for (int i = 0; i < xPositions.length; i++) {
    		cards[i].x = xPositions[i];
    	}
        gui.redrawDisplay();
    }

    /**
     * Alters the y-coordinates of the provided cards such that the cards appear to be vertically
     * centered within this compartment. Cards which are larger than the compartment will
     * be top-justified in the compartment. Therefore it is guaranteed that the y-coordinate of all
     * cards will be within the range of the compartment afterwards. 
     * The x-coordinates are not changed, such that the provided cards may not be located within this
     * compartment after calling this method, if their horizontal position is out of range.
     * 
     * The GUI is refreshed after moving the cards.
     * 
     * The x-coordinate of the cards can also be moved into the boundary of the compartment by additionally
     * calling {@link #distributeHorizontal(CardObject[])} or {@link #centerHorizontal(CardObject[])}.
     * 
     * @param cards May not be null.
     * 
     * @see {@link Compartment#V_PADDING}, {@link Compartment#center(int[], int, int)}
     */
    public void centerVertical(CardObject[] cards) {
    	Objects.requireNonNull(cards);
    	int[] cardHeights = new int[cards.length];
    	for (int i = 0; i < cards.length; i++) {
    		cardHeights[i] = cards[i].getImage().getBounds().height;
    	}
    	
        int[] yPositions = center(cardHeights, getVStart(), getVEnd());

        for (int i = 0; i < yPositions.length; i++) {
        	cards[i].y = yPositions[i];
        }
        gui.redrawDisplay();
    }
    
    /**
     * Alters the x-coordinates of the provided cards such that the cards appear to be horizontally
     * centered within this compartment. Cards which are larger than the compartment will
     * be left-justified in the compartment. Therefore it is guaranteed that the x-coordinate of all
     * cards will be within the range of the compartment afterwards.
     * The y-coordinates are not changed, such that the provided cards may not be located within this
     * compartment after calling this method, if their vertical position is out of range.
     * 
     * The GUI is refreshed after moving the cards.
     * 
     * The y-coordinate of the cards can also be moved into the boundary of the compartment by additionally
     * calling {@link #distributeVertical(CardObject[])} or {@link #centerVertical(CardObject[])}.
     * 
     * @param cards
     * 
     * @see {@link Compartment#H_PADDING}, {@link Compartment#center(int[], int, int)}
     */
    public void centerHorizontal(CardObject[] cards) {
    	Objects.requireNonNull(cards);
    	int[] cardWidths = new int[cards.length];
    	for (int i = 0; i < cards.length; i++) {
    		cardWidths[i] = cards[i].getImage().getBounds().width;
    	}
    	
        int[] xPositions = center(cardWidths, getHStart(), getHEnd());

        for (int i = 0; i < xPositions.length; i++) {
        	cards[i].y = xPositions[i];
        }
        gui.redrawDisplay();

    }

	/**
	 * Computes starting-positions for images with the given dimensions that should be evenly distributed
	 * over the range of coordinates <code>start</code> to <code>end</code>. The beginning position
	 * is considered to be the smaller number of <code>start</code> to <code>end</code> and the ending is
	 * considered to be the other number. No image will be placed at a smaller coordinate than the beginning above.
	 * If possible, the last image will end at the ending. This is not possible if the range is smaller than
	 * the dimension of the last image, or maybe if only one image height is provided.
	 * 
	 * If there is enough space that the images do not have to overlap, the distance between images
	 * will be made equal. Otherwise, the starting-positions are distributed equally.
	 *
	 * @param imageDimensions the dimensions of the images to be distributed. All values must be greater or equal to 0.
	 *   May be null.
	 * @param start the start of the distribution range, must be greater of equal to 0
	 * @param end the end of the distribution range, must be greater of equal to 0
	 * 
	 * @return the computed starting-positions or an empty array if no imageDimensions have been provided
	 * 
	 * @throws IllegalArgumentException if negative numbers are provided
	 */
	public static int[] distribute(int[] imageDimensions, int start, int end) throws IllegalArgumentException {
		if (start < 0)
			throw new IllegalArgumentException("Start must not be < 0.");
		
		if (end < 0)
			throw new IllegalArgumentException("End must not be < 0.");
	
	    if (end < start) {
	    	int temp = start;
	    	start = end;
	    	end = temp;
	    }
		
		if (imageDimensions == null || imageDimensions.length == 0)
	        return new int[0];
		
		Arrays.stream(imageDimensions)
		.filter(h -> h < 0).
		findFirst().
		ifPresent(h -> {
			throw new IllegalArgumentException("ImageDimensions must not be < 0.");
			});
		
	    if (imageDimensions.length == 1)
	    	return new int[] {start};
	    	    
	    int totalImageDimensions = Arrays.stream(imageDimensions).sum();
	    int availableRange = end - start;
	    
	    int[] positions = new int[imageDimensions.length];
	
	    if (totalImageDimensions <= availableRange) {
	    	float diff = (float) (availableRange - totalImageDimensions) / (float) (imageDimensions.length - 1);
	    	float nextPos = start;
	    	for (int i = 0; i < imageDimensions.length; i++) {
	    		positions[i] = (int) nextPos;
	    		nextPos = nextPos + imageDimensions[i] + diff;
	    	}
	    } else {
	    	int availableHeightMinusLastImage = availableRange - imageDimensions[imageDimensions.length - 1];
	    	if (availableHeightMinusLastImage >=0) {
	    		float diff = (float) availableHeightMinusLastImage / (float) (imageDimensions.length - 1);
	    		float nextPos = start;
	        	for (int i = 0; i < imageDimensions.length; i++) {
	        		positions[i] = (int) nextPos;
	        		nextPos = nextPos + diff;
	        	}
	    	} else {
	        	for (int i = 0; i < imageDimensions.length; i++) {
	        		positions[i] = start;
	        	}
	    	}
	    }
	    return positions;
	}


	/**
	 * Computes starting-positions for images with the given dimensions that should be centered
	 * within the range of coordinates <code>start</code> to <code>end</code>. The beginning position
	 * is considered to be the smaller number of <code>start</code> to <code>end</code> and the ending is
	 * considered to be the other number. If the image does not fully fit within this range, it will be placed
	 * at the beginning.
	 *
	 * @param imageDimensions the dimensions of the images to be distributed. All values must be greater or equal to 0.
	 *   May be null.
	 * @param start the start of the distribution range, must be greater of equal to 0
	 * @param end the end of the distribution range, must be greater of equal to 0
	 * 
	 * @return the computed starting-positions or an empty array if no imageDimensions have been provided
	 * 
	 * @throws IllegalArgumentException if negative numbers are provided
	 */
	public static int[] center(int[] imageDimensions, int start, int end) {
		if (start < 0)
			throw new IllegalArgumentException("Start must not be < 0.");
		
		if (end < 0)
			throw new IllegalArgumentException("End must not be < 0.");
	
		if (end < start) {
			int temp = start;
			start = end;
			end = temp;
		}
		
		if (imageDimensions == null || imageDimensions.length == 0)
	        return new int[0];
		
		Arrays.stream(imageDimensions)
		.filter(h -> h < 0).
		findFirst().
		ifPresent(h -> {
			throw new IllegalArgumentException("ImageDimensions must not be < 0.");
			});

		
		int vCenter = (end - start) / 2 + start;
        
        int[] yPositions = new int[imageDimensions.length];
        
        for (int i = 0; i < imageDimensions.length; i++) {
        	yPositions[i] = vCenter - imageDimensions[i] / 2;
        	if (yPositions[i] < start)
        		yPositions[i] = start;
        }
		return yPositions;
	}

}
