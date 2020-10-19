package conditions;

public class CoordinatesNumberException extends Exception {

    public CoordinatesNumberException() {
        super("Количество введенных координат не соответствует длине корабля");
    }
}
