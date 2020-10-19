package game;

import java.io.IOException;
import java.util.Scanner;

public class Player {
    private static final int MAX_HITS = 20;
    private Battlefield myField;
    private Battlefield enemyField;
    private int hitCounter;
    private String name;

    public Player() {
        myField = new Battlefield();
        enemyField = new Battlefield();
        hitCounter = 0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public boolean makeShot(Player enemy, Scanner readIt) throws IOException {

        System.out.println("Ваше поле:");
        this.myField.print();
        System.out.println("Поле противника:");
        this.enemyField.print();

        System.out.println("Ходит игрок " + this.name + ". Введите координату удара в формате \"x,y\", где х и y - цифры от 0 до 9");

        while (true) {
            String strCoord = readIt.nextLine();

            // Проверка,что координата вбита верно
            if (strCoord.length() != 3 || strCoord.charAt(1) != ',' ||
                    !Character.isDigit(strCoord.charAt(0)) || !Character.isDigit(strCoord.charAt(2))) {
                System.out.println("Неверный формат ввода! Введите координату удара в формате \"x,y\", где х и y - цифры от 0 до 9");
                throw new IOException();
            }

            int x = Character.getNumericValue(strCoord.charAt(0));
            int y = Character.getNumericValue(strCoord.charAt(2));

            if (enemy.getGrid()[x][y] == Grid.SHIP) {

                enemy.getGrid()[x][y] = Grid.HIT;
                this.enemyField.getGrid()[x][y] = Grid.HIT;

                System.out.println("Ваше поле:");
                this.myField.print();
                System.out.println("Поле противника:");
                this.enemyField.print();
                System.out.println("Попадание!");
                this.hitCounter++;

                if (enemy.getField().isDead(x, y)) {
                    System.out.println("Корабль полностью разбомблен!");
                }

                if (this.hitCounter == MAX_HITS) {
                    System.out.println("Поздравляем игрока " + this.name + " с победой!");
                    return true;
                }
                System.out.println("Выстрели еще раз!");

            } else {
                enemy.getGrid()[x][y] = Grid.MISS;
                this.enemyField.getGrid()[x][y] = Grid.MISS;
                System.out.println("Ваше поле:");
                this.myField.print();
                System.out.println("Поле противника:");
                this.enemyField.print();
                System.out.println("Промах!");
                break;
            }
        }
        System.out.println(this.name + ", ваш ход окончен. Нажмите Enter, чтобы ход перешел к следующему игроку");
        readIt.nextLine();
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n");
        return false;
    }

    public Grid[][] getGrid() {
        return myField.getGrid();
    }

    public Battlefield getField() {
        return this.myField;
    }
}
