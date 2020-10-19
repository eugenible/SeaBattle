package game;

import conditions.CoordinatesNumberException;
import conditions.InvalidShipException;
import conditions.PlaceNotFreeException;

import java.io.IOException;
import java.util.Scanner;

public class test {
    public static void main(String[] args) throws CoordinatesNumberException, PlaceNotFreeException, InvalidShipException, IOException {
        Scanner readIt = new Scanner(System.in);

        Player player1 = new Player();
        Player player2 = new Player();
        System.out.println("Введите имя первого игрока");
        player1.setName(readIt.nextLine());
        System.out.println("Введите имя второго игрока");
        player2.setName(readIt.nextLine());

        Battlefield.arrangement(player1, readIt);
        Battlefield.arrangement(player2, readIt);

        Player a, b, c = null;
        // Обпределяем того, кто ходит первым
        int first = (int) (Math.random() * 2);
        if (first == 0) {
            a = player1;
            b = player2;
        } else {
            a = player2;
            b = player1;
        }
        System.out.println("Игроки расставили свои корабли. Приступаем к боевым действиям!\nНачинает игрок " + a.getName());

        // Реализуем передачу хода.
        boolean win = false;
        do {
            while (true) {
                try {
                    win = a.makeShot(b, readIt);
                    break;
                } catch (IOException e) {
                    System.out.println(a.getName() + ", введите координату удара в формате \"x,y\", где х и y - цифры от 0 до 9");
                }
            }
            c = a;
            a = b;
            b = c;
        } while (!win);

        System.out.println("Игра окончена!");
        readIt.close();
    }
}