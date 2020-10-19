package conditions;

public class InvalidShipException extends Exception {
    public InvalidShipException() {
        super("Неправильное размещение корабля! Корабль должен быть полностью либо вертикален, либо горизонтален, а также должен иметь цельное строение");
    }
}
