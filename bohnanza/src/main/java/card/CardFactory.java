package card;

import java.lang.reflect.InvocationTargetException;

public class CardFactory {
    public static Card createCard(Class<? extends Card> cardClass) {
        try {
            Card card = cardClass.getDeclaredConstructor().newInstance();
            return card;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
