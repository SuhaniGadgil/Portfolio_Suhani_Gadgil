import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Santorini!");
        System.out.print("Player 1 name: ");
        String p1 = scanner.nextLine();
        System.out.print("Player 2 name: ");
        String p2 = scanner.nextLine();

        Game game = new Game(p1, p2);
        game.run();
    }
}