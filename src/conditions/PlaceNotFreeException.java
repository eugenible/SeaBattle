package conditions;

public class PlaceNotFreeException extends Exception {
    public PlaceNotFreeException()  {
        super("В данном месте нельзя расположить корабль: это место занято или здесь корабль будет соприкасаться с другим кораблем");
    }
}
