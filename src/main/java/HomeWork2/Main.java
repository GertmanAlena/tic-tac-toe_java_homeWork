package HomeWork2;

import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final int COUNT_WIN = 4;
    private static final char DOT_HUMAN = '✕'; //элемент для обозначения хода человека
    private static final char DOT_COMP = '◯';  //элемент для обозначения хода компьютера
    private static final char DOT_EMPTY = '▫'; //элемент для обозначения пустого поля
    private static char[][] field;  //массив для игрового поля
    private static Integer[] sizeField;
     private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static int fieldSizeX;  // длина по оси X
    private static int fieldSizeY;  // длина по оси Y

    public static void main(String[] args) {
        do {
            interSizeField();
            initial();
            print();
            while (true) {
                turnHuman();
                print();
                if (checkStateGame(DOT_HUMAN, "Вы победили! "))
                    break;
                turnComp();
                print();
                if (checkStateGame(DOT_COMP, "Победа за Компьютером! "))
                    break;
            }
            System.out.print("Желаете сыграть еще раз? (Y - да): ");
        } while (scanner.next().equalsIgnoreCase("Y"));
    }
    /**
     * Метод ввода размеров игрового поля
     */
    private static void interSizeField() {
        sizeField = new Integer[2];
        System.out.println("Введите ширину поля: >> ");
        if (scanner.hasNextInt()) {
            sizeField[0] = scanner.nextInt();
            scanner.nextLine();
        }
        System.out.print("Введите длину поля: >>> ");
        if (scanner.hasNextInt()) {
            sizeField[1] = scanner.nextInt();
            scanner.nextLine();
        }
    }
    /**
     * инициализация
     */
    private static void initial(){
        fieldSizeX = sizeField[1];
        fieldSizeY = sizeField[0];
        field = new char[fieldSizeX][fieldSizeY];
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                field[x][y] = DOT_EMPTY;
            }
        }
    }
    /**
     * вывод в консоль игры
     */
    private static void print(){
        System.out.print("+");
        for (int x = 0; x < fieldSizeX * 2 + 1; x++) {
            System.out.print((x % 2 == 0) ? " " : x / 2 + 1);
        }
        System.out.println();
        for (int x = 0; x < fieldSizeX * 2 + 2; x++) {
            System.out.print("-");
        }
        System.out.println();
        for (int x = 0; x < fieldSizeX; x++) {
            System.out.print(x + 1 + "|");
            for (int y = 0; y < fieldSizeY; y++) {
                System.out.print(field[x][y] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
    /**
     * ход игрока
     */
    private static void turnHuman(){
        int x, y;
        do {
            while (true){
                System.out.print("введите координату X: >>> ");
                if(scanner.hasNextInt()) {
                    x = scanner.nextInt() - 1;
                    scanner.nextLine();
                    break;
                }
                else {
                    System.out.println("Некорректно введено число, попробуй ещё раз! ");
                    scanner.nextLine();
                }
            }
            while (true){
                System.out.print("введите координату Y: >>> ");
                if(scanner.hasNextInt()) {
                    y = scanner.nextInt() - 1;
                    scanner.nextLine();
                    break;
                }
                else {
                    System.out.println("Некорректно введено число, попробуй ещё раз! ");
                    scanner.nextLine();
                }
            }
        } while (!isFieldLimits(x, y) || !isEmptyValue(x, y));
        field[x][y] = DOT_HUMAN;
//        if(winHuman(x, y)) {
//            System.out.println("Victory!!! You Win!");
//            return true;
//
//        }
//        return false;
    }

    /**
     * Проверка на ввод координат (пустое поле или занято ходом противника)
     * @param x координата
     * @param y координата
     * @return возвращает пустую ячейку истиной
     */
    private static boolean isEmptyValue(int x, int y) {
        return field[x][y] == DOT_EMPTY;
    }

    /**
     * Проверка на ввод координат (выход за пределы игрового поля)
     * @param x координата
     * @param y координата
     * @return возвращает истину, если не вышли за границы игрового поля
     */
    private static boolean isFieldLimits(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    /**
     * Ход компьютера. Сначала просматривает выигрышные позиции человека
     * если таких нет, переходит в рандомный метод хода
     */
    private static void turnComp(){
        if(checkWin(DOT_HUMAN)){
            if(checkWin(DOT_COMP))
                turnCompRandom();
        }
    }
    /**
     * рандомный ход компьютера
     */
    private static void turnCompRandom(){
        int x, y;
        do {
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        } while (!isEmptyValue(x, y));
        field[x][y] = DOT_COMP;
    }
    /**
     * перебор каждой клеточки для хода
     * @return true если сделал нужны ход
     */
    private static boolean checkWin(char player) {
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                if (isEmptyValue(x, y)) {
                    field[x][y] = player;
                    if(!win(COUNT_WIN - 1, player, x, y)){
                        if(!winDiagonal(COUNT_WIN - 1, player, x, y)){
                            field[x][y] = DOT_EMPTY;
                        }else {
                            field[x][y] = DOT_COMP;
                            return false;
                        }
                    }else {
                        field[x][y] = DOT_COMP;
                        return false;
                    }
                }
            }
        }
        return true;
    }
     /**
     * метод проверки победы по горизонтали и вертикали
     * @param c символ игрока или компьютера
     * @param x координата
     * @param y координата
     * @return истину, если победная комбинация совпала, ложь - если нет
     */
    private static boolean win(int count, char c, int x, int y) {
        int startY = y;
        int startX = x;

        int countWin = 0;
        // вправо
        for (y = startY; y < fieldSizeY; y++) {
            if (field[startX][y] == c) {
                countWin++;
                if (countWin == count) return true;
            } else break;
        }
        //вниз
        countWin = 0;
        for (x = startX; x < fieldSizeX; x++) {
            if (field[x][startY] == c) {

                countWin++;
                if (countWin == count) return true;
            } else break;
        }
        //влево
        countWin = 0;
        for (y = startY; y > 0; y--) {
            if (field[startX][y] == c) {
                countWin++;
                if (countWin == count) return true;
            } else break;
        }
        //вверх
        for (x = startX; x > 0; x--) {
            if (field[x][startY] == c) {
                countWin++;
                if (countWin == count) return true;
            } else break;
        }
        return false;
    }

    /**
     * метод проверки победы по диагоналям
     * @param c символ игрока или компьютера
     * @param x координата
     * @param y координата
     * @return истину, если победная комбинация совпала, ложь - если нет
     */
    private static boolean winDiagonal(int count, char c, int x, int y) {
        int startY = y;
        int startX = x;

        int countUp = 0;
        int countDown = 0;
        // вправо вверх
//        for (; y < fieldSizeY && x > 0; y++) {
//            if (field[x][y] == c) {
//                countUp++;
//                x--;
//                if (countUp == count) return true;
//            } else break;
//        }
        //вправо вниз
//        y = startY;
        for (x = startX; x < fieldSizeX; x++) {
            if (field[x][y] == c) {
                countDown++;
                if (countDown == count) return true;
                if(y <= fieldSizeY-2){
                    y++;

                }else break;

            } else break;
        }
        // влево вниз
        x = startX;
        countUp = 0;
        for (y = startY; y >= 0 && x < fieldSizeX-1; y--) {
            if (field[x][y] == c) {

                countUp++;
                x++;
                if (countUp == count) return true;
            }
            else break;
        }
        //влево вверх
//        y = startY;
//        countUp = 0;
//        for (x = startX; x > 0 && countUp < count; x--) {
//            if (field[x][y] == c) {
//                countUp++;
//                if (y < fieldSizeY && y >= 1) {
//                    y--;
//                    if (countUp == count) return true;
//            }
//            }
//            else break;
//        }
        return false;
    }

//    private static boolean checkWin(char c){
//        // проверка по горизонтали
//        if(field[0][0] == c && field[0][1] == c && field[0][2] == c) return true;
//        if(field[1][0] == c && field[1][1] == c && field[1][2] == c) return true;
//        if(field[2][0] == c && field[2][1] == c && field[2][2] == c) return true;
//        // проверка по вертикали
//        if(field[0][0] == c && field[1][0] == c && field[2][0] == c) return true;
//        if(field[0][1] == c && field[1][1] == c && field[2][1] == c) return true;
//        if(field[0][2] == c && field[1][2] == c && field[2][2] == c) return true;
//        // проверка по диагонали
//        if(field[0][0] == c && field[1][1] == c && field[2][2] == c) return true;
//        if(field[0][2] == c && field[1][1] == c && field[2][0] == c) return true;
//
//        return false;
//    }

    /**
     * Метод проверки на ничью
     * @return true or false
     */
    private static boolean draw(){
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                if(isEmptyValue(x, y)) return false;
            }
        }
        return true;
    }

    /**
     * Проверка состояния игры (кто-то победил, ничья или продолжаем игру)
     * @return true or false
     */
    private static boolean checkStateGame(char c, String win){
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                if(field[x][y] == c) {
                    if(win(COUNT_WIN, c, x, y)){
                        System.out.println(win);
                        print();
                        return true;
                    }
                    if(winDiagonal(COUNT_WIN, c, x, y)){
                        System.out.println(win);
                        print();
                        return true;
                    }
                }

            }
        }
        if (draw()){
            System.out.println("Ничья! ");
            return true;
        }
        return false;   //игра продолжается

    }


}