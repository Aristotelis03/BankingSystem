import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("--Banking System--");
        Menu();
    }
    public static void Menu() {
        Scanner option = new Scanner(System.in);
        boolean exit = false;

        while(!exit) {
            System.out.println("--Menu-- \n0 -> Quit \n1 -> Login \n2 -> Register \n3 -> Database Check");
            String options = option.nextLine();
            switch (options) {
                case "0":
                    System.out.println("Terminating...");
                    exit = true;
                    break;
                case "1":
                    User user = new User();
                    Bank bank = new Bank();
                    int user_id = user.login();
                    if(user_id != -1) {
                        bank.LoggedInMenu(user_id);
                    } else {
                        System.out.println("Invalid Card Number or PIN");
                    }

                    break;
                case "2":
                    user = new User();
                    user.register();
                    break;
                case "3":
                    OracleConnection.main();
                    break;
                default:
                    System.out.println("Invalid entry");
                    break;
            }
            System.out.println();
        }
        option.close();
    }
}