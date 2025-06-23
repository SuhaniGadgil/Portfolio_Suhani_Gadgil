import java.util.Scanner;

public class PrototypeSimpleCalculator {
    public static void main (String [] args) {
        System.out.println("Hi this is a simple calculator.");
        Scanner myScanner = new Scanner(System.in);
        System.out.println("Please enter your first number:");
        int inputOne = myScanner.nextInt();
        System.out.println("please enter your next number:");
        int inputTwo = myScanner.nextInt();
        int sum = inputOne + inputTwo;
        System.out.println("The result is: " + sum);
        System.out.println("WOAH what a cool number!!");
    }
}