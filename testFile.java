import java.util.Scanner;

public static void main(String[] args) {

  Scanner reader = new Scanner(System.in);
  System.out.println("Please, type your name:");
  String name = reader.nextLine();
  
  System.out.println("Hello " + name + "! You are Welcome!");
}
