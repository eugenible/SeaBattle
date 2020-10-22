package game;

import conditions.CoordinatesNumberException;
import conditions.InvalidShipException;
import conditions.PlaceNotFreeException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Battlefield {
    private Grid[][] grid;

    /* создаем поле боя в начале игры */
    public Battlefield() {
        grid = new Grid[10][10];
        for (Grid[] grid : grid) {
            Arrays.fill(grid, Grid.EMPTY);
        }
    }

    /* выводит игровое поле на экран */
    public void print() {
        for (int i = 0; i < grid.length; i++) {
            if (i == 0) {
                System.out.print(" \t");
                for (int j = 0; j < 10; j++) {
                    System.out.print(j + "\t");
                }
                System.out.println();
            }
            System.out.print(i + "\t");
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print((j == grid[i].length - 1) ? (grid[i][j] + "\n") : grid[i][j] + "\t");
            }
        }
    }

    /*расстановка кораблей и проверка условий */
    public void putShip(String strCoords, Grid ship) throws CoordinatesNumberException, IOException, InvalidShipException, PlaceNotFreeException {

        // Создаем массив строк, каждая из которых в нем - это строковое представление координаты "х,у"
        String[] strCoordsArr = strCoords.split(";");

        // Проверяем, что количество элементов в массиве координат равно кол-ву палуб у корабля, который располагаем на поле
        if (strCoordsArr.length != ship.getLength()) {
            throw new CoordinatesNumberException();
        }

        // Создаем двумерный массив, где у Х и Y будут свои ячейки, и которые мы будем заполнять
        int[][] digitalCoords = new int[strCoordsArr.length][2];

        // Проверяем условия правильного формата: число х и у в диапазоне [0-9], координаты разделены запятой ','.
        for (int i = 0; i < strCoordsArr.length; i++) {

            // Проверяем формат, что в записи каждой строковой координаты 3 символа: предполагаем два символа для цифр и ',' между ними.
            if (strCoordsArr[i].length() != 3) {
                System.out.println("Неверный формат ввода! Координата должна иметь формат \"x,y\" и, значит, состоять из 3 символов");
                throw new IOException();
            }

            // Проверяем формат, что второй символ - это разделитель ','
            if (strCoordsArr[i].charAt(1) != ',') {
                System.out.println("Неверный формат ввода! Разделителем в координате является ','");
                throw new IOException();
            }

            // Проверили, что первый и третий символ в строке - цифры (кординаты X и Y)
            if (!Character.isDigit(strCoordsArr[i].charAt(0)) || !Character.isDigit(strCoordsArr[i].charAt(2))) {
                System.out.println("Неверный формат ввода! X и Y - цифры от 0 до 9");
                throw new IOException();
            }

            // Окончательно внесли в i-ую строку двумерного массива координату корабля.
            digitalCoords[i][0] = Character.getNumericValue(strCoordsArr[i].charAt(0));
            digitalCoords[i][1] = Character.getNumericValue(strCoordsArr[i].charAt(2));
        }

        if (!isValid(digitalCoords)) {
            throw new InvalidShipException();
        }
        if (!isFree(digitalCoords, this.grid)) {
            throw new PlaceNotFreeException();
        }
        // Забиваем корабль в поле
        for (int[] digitalCoord : digitalCoords) {

            grid[digitalCoord[0]][digitalCoord[1]] = Grid.SHIP;
        }
        // Обводим корабль блоками обязательного пространства вокруг корабля
        for (int[] digitalCoord : digitalCoords) {
            int x = digitalCoord[0];
            int y = digitalCoord[1];

            if (x - 1 >= 0 && grid[x - 1][y] != Grid.SHIP) {
                grid[x - 1][y] = Grid.GAP;
            }
            if (x - 1 >= 0 && y + 1 <= 9 && grid[x - 1][y + 1] != Grid.SHIP) {
                grid[x - 1][y + 1] = Grid.GAP;
            }
            if (y + 1 <= 9 && grid[x][y + 1] != Grid.SHIP) {
                grid[x][y + 1] = Grid.GAP;
            }
            if (x + 1 <= 9 && y + 1 <= 9 && grid[x + 1][y + 1] != Grid.SHIP) {
                grid[x + 1][y + 1] = Grid.GAP;
            }
            if (x + 1 <= 9 && grid[x + 1][y] != Grid.SHIP) {
                grid[x + 1][y] = Grid.GAP;
            }
            if (x + 1 <= 9 && y - 1 >= 0 && grid[x + 1][y - 1] != Grid.SHIP) {
                grid[x + 1][y - 1] = Grid.GAP;
            }
            if (y - 1 >= 0 && grid[x][y - 1] != Grid.SHIP) {
                grid[x][y - 1] = Grid.GAP;
            }
            if (x - 1 >= 0 && y - 1 >= 0 && grid[x - 1][y - 1] != Grid.SHIP) {
                grid[x - 1][y - 1] = Grid.GAP;
            }
        }
    }

    // Проверка условия на то, что корабли вертикальны или горизонтальны и части корабля стоят вплотную друг к другу.
    public static boolean isValid(int[][] coords) {
        if (coords.length == 1) {
            return true;
        }

        int x = coords[0][0];
        int y = coords[0][1];
        boolean isCorrect = true;

        // Проверяем, что у корабля координата Х неизменна, а координаты Y отличны друг от друга.
        for (int i = 1; i < coords.length; i++) {
            if (coords[i][0] == x) {

                /* Убедились, что  значение координаты Х i-ого элемента равно значению Х нулевого элемента.
                То есть мы строим корабль по прямой линии. Проверим, что значение координаты Y i-ого элемента
                не равно другим j-ым значениям координаты Y других элементов и не равно Y нулевого элемента (т.к. он не проверяется в цикле).
                Если сравниваем значение i-го элемента с самим собой (i = j), то пропускаем эту итерацию.
                 */
                for (int j = 0; j < coords.length; j++) {
                    if (i == j) {
                        continue;
                    }

                    if (coords[i][1] == coords[j][1] || coords[i][1] == y) {
                        isCorrect = false;
                        break;
                    }
                }

                /* Видим, что Х одинаковы, а Y разные у каждой координаты. Проверим, прикосаются ла блоки корабля друг к другу.
                Для этого создали массив целых чисел, в который внесли значения Y-ка из каждой строки массива координат.
                Затем его отсортировали и проверили условие, чтобы каждый последующий элемент был на 1 больше предыдущего:
                [t + 1] = [t] + 1;
                */
                int[] isAdjacent = new int[coords.length];

                for (int j = 0; j < coords.length; j++) {
                    isAdjacent[j] = coords[j][1];
                }
                Arrays.sort(isAdjacent);

                for (int t = 0; t < isAdjacent.length - 1; t++) {
                    isCorrect = isAdjacent[t + 1] == (isAdjacent[t] + 1) && isCorrect;
                }

            } else if (coords[i][1] == y) {
                for (int j = 0; j < coords.length; j++) {
                    if (i == j) {
                        continue;
                    }

                    if (coords[i][0] == coords[j][0] || coords[i][0] == x) {
                        isCorrect = false;
                        break;
                    }
                }

                int[] isAdjacent = new int[coords.length];
                for (int j = 0; j < coords.length; j++) {
                    isAdjacent[j] = coords[j][0];
                }
                Arrays.sort(isAdjacent);

                for (int t = 0; t < isAdjacent.length - 1; t++) {
                    isCorrect = isAdjacent[t + 1] == (isAdjacent[t] + 1) && isCorrect;
                }

            } else {
                isCorrect = false;
            }
        }
        return isCorrect;
    }

    // Просто проверяем, свобдно ли место для расположения корабля
    public static boolean isFree(int[][] coords, Grid[][] field) {
        for (int[] coord : coords) {
            if (!(field[coord[0]][coord[1]] == Grid.EMPTY)) {
                return false;
            }
        }
        return true;
    }

    public Grid[][] getGrid() {
        return grid;
    }

    // Метод расстановки всех кораблей на поле
    public static void arrangement(Player player, Scanner readIt) throws CoordinatesNumberException, PlaceNotFreeException, InvalidShipException, IOException {

        System.out.println(player.getName() + ", твое поле выведено ниже\n");
        player.getField().print();

        while (true) {
            try {
                System.out.println(player.getName() + ", введи координаты четырехпалубного корабля (формат: х,у;х,у;х,у;х,у)");
                player.getField().putShip(readIt.nextLine(), Grid.BATTLESHIP);
                player.getField().print();
                break;
            } catch (IOException e) {
            } catch (CoordinatesNumberException | PlaceNotFreeException | InvalidShipException e) {
                System.out.println(e.getMessage());
            }
        }

        for (int i = 0; i < 2; i++) {
            while (true) {
                try {
                    System.out.println("Введи координаты трехпалубного корабля (формат: х,у;х,у;х,у)");
                    player.getField().putShip(readIt.nextLine(), Grid.CRUISER);
                    player.getField().print();
                    break;
                } catch (IOException e) {
                } catch (CoordinatesNumberException | PlaceNotFreeException | InvalidShipException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        for (int i = 0; i < 3; i++) {
            while (true) {
                try {
                    System.out.println("Введи координаты двухпалубного корабля (формат: х,у;х,у)");
                    player.getField().putShip(readIt.nextLine(), Grid.SUBMARINE);
                    player.getField().print();
                    break;
                } catch (IOException e) {
                } catch (CoordinatesNumberException | PlaceNotFreeException | InvalidShipException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            while (true) {
                try {
                    System.out.println("Введи координаты однопалубного корабля (формат: х,у)");
                    player.getField().putShip(readIt.nextLine(), Grid.DESTROYER);
                    player.getField().print();
                    break;
                } catch (IOException e) {
                } catch (CoordinatesNumberException | PlaceNotFreeException | InvalidShipException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        System.out.println("Поле с расставленными кораблями: ");
        player.getField().print();
        System.out.println("Введи любое сообщение в консоль, чтобы убрать твое поле с экрана");
        readIt.nextLine();
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n");
    }

    // Определяем, был ли полностью взорван корабль от попадания
    public boolean isDead(int x, int y) {
        for (int i = 1; i <= 3; i++) {
            if (x - i >= 0 && grid[x - i][y] == Grid.SHIP) {
                return false;
            } else if (x - i >= 0 && grid[x - i][y] != Grid.HIT) {
                break;
            }
        }

        for (int i = 1; i <= 3; i++) {
            if (x + i <= 9 && grid[x + i][y] == Grid.SHIP) {
                return false;
            } else if (x + i <= 9 && grid[x + i][y] != Grid.HIT) {
                break;
            }
        }

        for (int i = 1; i <= 3; i++) {
            if (y - i >= 0 && grid[x][y - i] == Grid.SHIP) {
                return false;
            } else if (y - i >= 0 && grid[x][y - i] != Grid.HIT) {
                break;
            }
        }

        for (int i = 1; i <= 3; i++) {
            if (y + i <= 9 && grid[x][y + i] == Grid.SHIP) {
                return false;
            } else if (y + i <= 9 && grid[x][y + i] != Grid.HIT) {
                break;
            }
        }
        return true;
    }


    // Обводим возрванный корабль знаками промаха
    public void surroundDeadShip(int x, int y) {
        List<int[]> deadShipCoords = new ArrayList<>();
        deadShipCoords.add(new int[]{x, y});

        for (int i = 1; i <= 3; i++) {
            if (x - i >= 0 && grid[x - i][y] == Grid.HIT) {
                deadShipCoords.add(new int[]{x - i, y});
            } else {
                break;
            }
        }

        for (int i = 1; i <= 3; i++) {
            if (x + i <= 9 && grid[x + i][y] == Grid.HIT) {
                deadShipCoords.add(new int[]{x + i, y});
            } else {
                break;
            }
        }

        for (int i = 1; i <= 3; i++) {
            if (y - i >= 0 && grid[x][y - i] == Grid.HIT) {
                deadShipCoords.add(new int[]{x, y - i});
            } else {
                break;
            }
        }

        for (int i = 1; i <= 3; i++) {
            if (y + i <= 9 && grid[x][y + i] == Grid.HIT) {
                deadShipCoords.add(new int[]{x, y + i});
            } else {
                break;
            }
        }

        for (int[] coord : deadShipCoords) {
            int a = coord[0];
            int b = coord[1];

            if (a - 1 >= 0 && grid[a - 1][b] != Grid.HIT) {
                grid[a - 1][b] = Grid.MISS;
            }
            if (a - 1 >= 0 && b + 1 <= 9 && grid[a - 1][b + 1] != Grid.SHIP) {
                grid[a - 1][b + 1] = Grid.MISS;
            }
            if (b + 1 <= 9 && grid[a][b + 1] != Grid.HIT) {
                grid[a][b + 1] = Grid.MISS;
            }
            if (a + 1 <= 9 && b + 1 <= 9 && grid[a + 1][b + 1] != Grid.HIT) {
                grid[a + 1][b + 1] = Grid.MISS;
            }
            if (a + 1 <= 9 && grid[a + 1][b] != Grid.HIT) {
                grid[a + 1][b] = Grid.MISS;
            }
            if (a + 1 <= 9 && b - 1 >= 0 && grid[a + 1][b - 1] != Grid.HIT) {
                grid[a + 1][b - 1] = Grid.MISS;
            }
            if (b - 1 >= 0 && grid[a][b - 1] != Grid.HIT) {
                grid[a][b - 1] = Grid.MISS;
            }
            if (a - 1 >= 0 && b - 1 >= 0 && grid[a - 1][b - 1] != Grid.HIT) {
                grid[a - 1][b - 1] = Grid.MISS;
            }
        }
    }

}
