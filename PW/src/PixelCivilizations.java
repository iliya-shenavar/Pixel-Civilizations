import java.util.Scanner;

public class PixelCivilizations {
    private static final int WIDTH = 18;
    private static final int HEIGHT = 27;
    private static final int EMPTY = 0;
    private static final int OBSTACLE = 1;
    private static final int CITY = 2;
    private static final int KNIGHT = 3;

    private static final String PLAYER_1 = "Player 1";
    private static final String PLAYER_2 = "Player 2";

    private static final String OWNERSHIP_PLAYER_1 = "🟩";
    private static final String OWNERSHIP_PLAYER_2 = "🟨";

    private static int[][] gameMap = new int[HEIGHT][WIDTH];

    public static void main(String[] args) {
        createMap();
        placeCities();
        placeRandomPlayer(KNIGHT, PLAYER_1);
        placeRandomPlayer(KNIGHT, PLAYER_2);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMap();

            // نوبت بازیکن 1
            System.out.println(PLAYER_1 + "'s turn:");
            String directionPlayer1 = scanner.nextLine();
            moveKnight(PLAYER_1, directionPlayer1);

            // نوبت بازیکن 2
            System.out.println(PLAYER_2 + "'s turn:");
            // در انتظار ورود حرکت از بازیکن 2
            scanner.nextLine();
            moveKnight(PLAYER_2, "down"); // حرکت تصادفی
        }
    }

    private static void createMap() {
        // ساخت نقشه خالی
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                gameMap[i][j] = EMPTY;
            }
        }

        // اضافه کردن موانع طبیعی
        for (int i = 0; i < 10; i++) {
            int x = (int) (Math.random() * WIDTH);
            int y = (int) (Math.random() * HEIGHT);
            gameMap[y][x] = OBSTACLE;
        }
    }

    private static void placeCities() {
        // قرار دادن شهرها بصورت تصادفی
        for (int i = 0; i < 2; i++) {
            int x = (int) (Math.random() * WIDTH);
            int y = (int) (Math.random() * (HEIGHT / 2) + HEIGHT / 2);
            gameMap[y][x] = CITY;
        }
    }

    private static void placeRandomPlayer(int playerType, String playerName) {
        int x = (int) (Math.random() * WIDTH);
        int y = (int) (Math.random() * HEIGHT);
        gameMap[y][x] = playerType;

        System.out.println(playerName + " has appeared at: (" + x + ", " + y + ")");
    }

    private static void printMap() {
        // نمایش نقشه
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (gameMap[i][j] == EMPTY) {
                    System.out.print("⬛️ ");
                } else if (gameMap[i][j] == OBSTACLE) {
                    System.out.print("🌲 ");
                } else if (gameMap[i][j] == CITY) {
                    System.out.print("🌆 ");
                } else if (gameMap[i][j] == KNIGHT) {
                    if (i == HEIGHT - 1 && j == WIDTH - 1) {
                        System.out.print(OWNERSHIP_PLAYER_1 + " ");
                    } else if (i == 0 && j == 0) {
                        System.out.print(OWNERSHIP_PLAYER_2 + " ");
                    } else {
                        System.out.print("🏇 ");
                    }
                }
            }
            System.out.println();
        }
    }

    private static void moveKnight(String player, String direction) {
        // حرکت شوالیه در یک جهت خاص
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (gameMap[i][j] == KNIGHT) {
                    if (player.equals(PLAYER_1) && gameMap[i][j] != EMPTY) {
                        if (direction.equals("W") && i > 0 && gameMap[i - 1][j] == EMPTY) {
                            gameMap[i - 1][j] = KNIGHT;
                            gameMap[i][j] = EMPTY;
                        } else if (direction.equals("S") && i < HEIGHT - 1 && gameMap[i + 1][j] == EMPTY) {
                            gameMap[i + 1][j] = KNIGHT;
                            gameMap[i][j] = EMPTY;
                        } else if (direction.equals("A") && j > 0 && gameMap[i][j - 1] == EMPTY) {
                            gameMap[i][j - 1] = KNIGHT;
                            gameMap[i][j] = EMPTY;
                        } else if (direction.equals("D") && j < WIDTH - 1 && gameMap[i][j + 1] == EMPTY) {
                            gameMap[i][j + 1] = KNIGHT;
                            gameMap[i][j] = EMPTY;
                        }
                        return;
                    } else if (player.equals(PLAYER_2) && gameMap[i][j] != EMPTY) {
                        // حرکت بازیکن 2 بر اساس دکمه‌های Arrow Keys
                        // به عنوان مثال، اینجا از "down" برای حرکت استفاده شده است
                        if (direction.equals("down") && i < HEIGHT - 1 && gameMap[i + 1][j] == EMPTY) {
                            gameMap[i + 1][j] = KNIGHT;
                            gameMap[i][j] = EMPTY;
                        }
                        return;
                    }
                }
            }
        }
    }
}
