package io.bitbucket.plt.sdp.bohnanza.gui;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

/**
 * Manages the loading and caching of image resources. For internal use.
 * @author bockisch
 *
 */
class Resources {

    private final Display display;
    private final int cardWidth;
    private final int cardHeight;

    private static Resources INSTANCE;

    private Map<String, Image> images = new HashMap<String, Image>();
    private int zoomedCardWidth;
    private int zoomedCardHeight;

    public static void init(Display display, int cardWidth, int cardHeight) {
        init(display, cardWidth, cardHeight, cardWidth, cardHeight);
    }

    public static void init(Display display, int cardWidth, int cardHeight, int zoomedCardWidth, int zoomedCardHeight) {
        if (INSTANCE != null)
            throw new IllegalStateException("Resources already initialized.");

        INSTANCE = new Resources(display, cardWidth, cardHeight, zoomedCardWidth, zoomedCardHeight);
    }

    public static Resources getInstance() {
        if (INSTANCE == null)
            throw new IllegalStateException("Resources not initialized.");

        return INSTANCE;
    }

    private Resources(Display display, int cardWidth, int cardHeight, int zoomedCardWidth, int zoomedCardHeight) {
        this.display = display;
        this.cardWidth = cardWidth;
        this.cardHeight = cardHeight;
        this.zoomedCardWidth = zoomedCardWidth;
        this.zoomedCardHeight = zoomedCardHeight;
    }

    public Image getImage(String imageName, int width, int height) {

        String key = "I" + imageName + "@" + width + "x" + height;
        Image result;
        if (images.containsKey(key)) {
            result = images.get(key);
        } else {
            Image fromFile = new Image(display,
                    this.getClass().getClassLoader().getResourceAsStream(imageName + ".jpg"));
            result = getResizedImage(fromFile, width, height);
            images.put(key, result);
        }
        return result;
    }

    public Image getFrontImage(CardType card) {
        String key = "F" + card.name();
        Image result;
        if (images.containsKey(key)) {
            result = images.get(key);
        } else {
            Image fromFile = new Image(display,
                    this.getClass().getClassLoader().getResourceAsStream(card.name() + ".jpg"));
            result = getResizedImageKeepAspect(fromFile, cardWidth, cardHeight);
            images.put(key, result);
        }
        return result;
    }

    public Image getBackImage(CardType card) {
        boolean rotate = getFrontImage(card).getBounds().width > getFrontImage(card).getBounds().height;
        String filename = getBackImageFilename(card);
        String key = "B" + rotate + filename;
        Image result;
        if (images.containsKey(key)) {
            result = images.get(key);
        } else {
            Image fromFile = new Image(display,
                    this.getClass().getClassLoader().getResourceAsStream(filename + ".jpg"));

            result = getResizedImageKeepAspect(fromFile, cardWidth, cardHeight);
            if (rotate)
                result = rotate(display, result);
            images.put(key, result);
        }
        return result;
    }

    private String getBackImageFilename(CardType card) {
        String filename;
        switch (card) {
        case AL_CABOHNE:
        case CASINO_1:
        case CASINO_3:
        case CASINO_4:
        case DON_CORLEBOHNE:
        case GAS_STATION_1:
        case GAS_STATION_3:
        case GAS_STATION_4:
        case JOE_BOHNANO:
        case NIGHTBAR_1:
        case NIGHTBAR_3:
        case NIGHTBAR_4:
        case POLICE_STATION_3:
        case POLICE_STATION_4:
        case PROHIBON_PHASENKARTE_GRUEN_1:
        case PROHIBON_PHASENKARTE_GRUEN_2:
        case PROHIBON_PHASENKARTE_GRUEN_3:
        case PROHIBON_PHASENKARTE_GRUEN_4:
        case PROHIBON_PHASENKARTE_GRUEN_5:
        case PROHIBON_PHASENKARTE_GRUEN_6:
        case PROHIBON_PHASENKARTE_ROT_1:
        case PROHIBON_PHASENKARTE_ROT_2:
        case PROHIBON_PHASENKARTE_ROT_3:
        case PROHIBON_PHASENKARTE_ROT_4:
        case PROHIBON_PHASENKARTE_ROT_5:
        case PROHIBON_PHASENKARTE_ROT_6:
        case PUB_1:
        case PUB_3:
        case PUB_4:
        case RAZZIA_4:
        case RESTAURANT_1:
        case RESTAURANT_3:
        case RESTAURANT_4:
        case UNDERTAKER_1:
        case UNDERTAKER_3:
        case UNDERTAKER_4:
        case VARIETE_1:
        case VARIETE_3:
        case VARIETE_4:
            filename = "RUECKSEITE_AL_CABOHNE";
            break;

        case AUGEN_BOHNE:
        case BLAUE_BOHNE:
        case BRECH_BOHNE:
        case COGNAC_BOHNE:
        case FEUER_BOHNE:
        case GARTEN_BOHNE:
        case KAFFEE_BOHNE:
        case KAKAO_BOHNE:
        case KIDNEY_BOHNE:
        case PUFF_BOHNE:
        case ROTE_BOHNE:
        case SAU_BOHNE:
        case SOJA_BOHNE:
        case STANGEN_BOHNE:
        case WEINBRAND_BOHNE:
            filename = "RUECKSEITE_BASIS";
            break;

        case GEBAEUDE_AUGEN_BOHNE_1:
        case GEBAEUDE_AUGEN_BOHNE_2:
        case GEBAEUDE_AUGEN_BOHNE_3:
        case GEBAEUDE_AUGEN_BOHNE_4:
        case GEBAEUDE_BLAUE_BOHNE_1:
        case GEBAEUDE_BLAUE_BOHNE_2:
        case GEBAEUDE_BLAUE_BOHNE_3:
        case GEBAEUDE_BLAUE_BOHNE_4:
        case GEBAEUDE_BRECH_BOHNE_1:
        case GEBAEUDE_BRECH_BOHNE_2:
        case GEBAEUDE_BRECH_BOHNE_3:
        case GEBAEUDE_BRECH_BOHNE_4:
        case GEBAEUDE_COGNAC_BOHNE_1:
        case GEBAEUDE_COGNAC_BOHNE_2:
        case GEBAEUDE_COGNAC_BOHNE_3:
        case GEBAEUDE_COGNAC_BOHNE_4:
        case GEBAEUDE_FEUER_BOHNE_1:
        case GEBAEUDE_FEUER_BOHNE_2:
        case GEBAEUDE_FEUER_BOHNE_3:
        case GEBAEUDE_FEUER_BOHNE_4:
        case GEBAEUDE_GARTEN_BOHNE_1:
        case GEBAEUDE_GARTEN_BOHNE_2:
        case GEBAEUDE_GARTEN_BOHNE_3:
        case GEBAEUDE_GARTEN_BOHNE_4:
        case GEBAEUDE_ROTE_BOHNE_1:
        case GEBAEUDE_ROTE_BOHNE_2:
        case GEBAEUDE_ROTE_BOHNE_3:
        case GEBAEUDE_ROTE_BOHNE_4:
        case GEBAEUDE_SAU_BOHNE_1:
        case GEBAEUDE_SAU_BOHNE_2:
        case GEBAEUDE_SAU_BOHNE_3:
        case GEBAEUDE_SAU_BOHNE_4:
        case GEBAEUDE_SOJA_BOHNE_1:
        case GEBAEUDE_SOJA_BOHNE_2:
        case GEBAEUDE_SOJA_BOHNE_3:
        case GEBAEUDE_SOJA_BOHNE_4:
        case UEBERSICHT_HIGH_BOHN:
            filename = "RUECKSEITE_HIGH_BOHN";
            break;

        case BOHNENFELD_3:
            filename = "BOHNENFELD_3";
            break;

        default:
            throw new IllegalArgumentException("No rear image for card " + card);
        }

        return filename;
    }

    private Image getResizedImageKeepAspect(Image image, int width, int height) {
        if (image.getBounds().width > image.getBounds().height) {
            int temp = width;
            width = height;
            height = temp;
        }
        return getResizedImage(image, width, height);
    }

    private Image getResizedImage(Image image, int width, int height) {
        Image scaled = new Image(display, width, height);
        GC gc = new GC(scaled);
        gc.setAntialias(SWT.ON);
        gc.setInterpolation(SWT.HIGH);
        gc.drawImage(image, 0, 0, image.getBounds().width, image.getBounds().height, 0, 0, width, height);
        gc.dispose();
        image.dispose();
        return scaled;
    }

    static Image rotate(Display display, Image image) {
        ImageData srcData = image.getImageData();
        int bytesPerPixel = srcData.bytesPerLine / srcData.width;
        int destBytesPerLine = srcData.height * bytesPerPixel;
        byte[] newData = new byte[srcData.width * destBytesPerLine];
        int width = 0, height = 0;
        for (int srcY = 0; srcY < srcData.height; srcY++) {
            for (int srcX = 0; srcX < srcData.width; srcX++) {
                int destX = 0, destY = 0, destIndex = 0, srcIndex = 0;
                destX = srcY;
                destY = srcData.width - srcX - 1;
                width = srcData.height;
                height = srcData.width;
                destIndex = (destY * destBytesPerLine) + (destX * bytesPerPixel);
                srcIndex = (srcY * srcData.bytesPerLine) + (srcX * bytesPerPixel);
                System.arraycopy(srcData.data, srcIndex, newData, destIndex, bytesPerPixel);
            }
        }
        // destBytesPerLine is used as scanlinePad to ensure that no padding is
        // required
        return new Image(display,
                new ImageData(width, height, srcData.depth, srcData.palette, destBytesPerLine, newData));
    }

    public Image getZoomedFrontImage(CardType card) {
        String key = "f" + card.name();
        Image result;
        if (images.containsKey(key)) {
            result = images.get(key);
        } else {
            Image fromFile = new Image(display,
                    this.getClass().getClassLoader().getResourceAsStream(card.name() + ".jpg"));
            result = getResizedImageKeepAspect(fromFile, zoomedCardWidth, zoomedCardHeight);
            images.put(key, result);
        }
        return result;
    }

    public Image getZoomedBackImage(CardType card) {
        boolean rotate = getFrontImage(card).getBounds().width > getFrontImage(card).getBounds().height;
        String filename = getBackImageFilename(card);
        String key = "b" + rotate + filename;
        Image result;
        if (images.containsKey(key)) {
            result = images.get(key);
        } else {
            Image fromFile = new Image(display,
                    this.getClass().getClassLoader().getResourceAsStream(filename + ".jpg"));

            result = getResizedImageKeepAspect(fromFile, zoomedCardWidth, zoomedCardHeight);
            if (rotate)
                result = rotate(display, result);
            images.put(key, result);
        }
        return result;
    }
    
    public Size getZoomedDeltaSize() {
        return new Size(zoomedCardWidth - cardWidth, zoomedCardHeight - cardHeight);
    }

}
