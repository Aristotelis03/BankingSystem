import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Bank {
    private Connection connection;
    public void LoggedInMenu(int user_id) {
        Scanner option = new Scanner(System.in);
        boolean exit = false;

        while(!exit) {
            System.out.println("--Options-- \n0 -> Log Out \n1 -> Deposit \n2 -> Withdraw \n3 -> Check Balance \n4 -> Transfer");
            String options = option.nextLine();
            switch (options) {
                case "0":
                    System.out.println("Logging Out...");
                    exit = true;
                    break;
                case "1":
                    deposit(user_id);
                    break;
                case "2":
                    withdraw(user_id);
                    break;
                case "3":
                    checkingBalance(user_id);
                    break;
                case "4":
                    transfer(user_id);
                    break;
                default:
                    System.out.println("Invalid entry");
                    break;
            }
//            System.out.println();
        }

    }

    public Bank() {
        try {
            String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:XE"; // Note the "XE" SID
            String username = "SYSTEM";
            String password = "21372003";
            connection = DriverManager.getConnection(jdbcUrl, username, password);
//            System.out.println("Connected to database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deposit(int user_id) {
        System.out.println("--Deposit--");
        double depositValue = 0;
        Scanner input = new Scanner(System.in);



        try {
//            System.out.println("Connecting to database...");
            String sql = "SELECT * FROM bank_user INNER JOIN bank_card ON bank_user.user_id = bank_card.user_id";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery(sql);

            while (depositValue <= 0) {
                System.out.println("Enter the amount you want to deposit:");
                depositValue = input.nextDouble();
            }

            while(rs.next()) {
                if(rs.getInt("user_id") == user_id) {
//                    System.out.println("yes");
                    double old_balance = rs.getDouble("balance");
                    sql = "UPDATE Bank_Card SET balance = ? WHERE user_id = ?";
                    preparedStatement = connection.prepareStatement(sql);

                    preparedStatement.setDouble(1, (old_balance + depositValue));
                    preparedStatement.setInt(2, user_id);

                    // Execute the insert statement
                    int rowsAffected = preparedStatement.executeUpdate();
//                    System.out.println(rowsAffected + " row(s) inserted.");
                    System.out.println("\n-------------------------------------");
                    System.out.println("*Deposit was succesful*");
                    System.out.println("-------------------------------------\n");

                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void withdraw(int user_id) {
        System.out.println("--Withdraw--");
        double depositValue = 0;
        Scanner input = new Scanner(System.in);

        try {
//            System.out.println("Connecting to database...");
            String sql = "SELECT * FROM bank_user INNER JOIN bank_card ON bank_user.user_id = bank_card.user_id";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery(sql);

            while(rs.next()) {
                if(rs.getInt("user_id") == user_id) {
//                    System.out.println("yes");
                    double old_balance = rs.getDouble("balance");

                    while (depositValue <= 0 || depositValue > old_balance) {
                        System.out.println("Enter the amount you want to withdraw:");
                        depositValue = input.nextDouble();

                        if (depositValue > old_balance) {
                            System.out.println("Insufficient balance");
                        }
                    }

                    sql = "UPDATE Bank_Card SET balance = ? WHERE user_id = ?";
                    preparedStatement = connection.prepareStatement(sql);

                    preparedStatement.setDouble(1, (old_balance - depositValue));
                    preparedStatement.setInt(2, user_id);

                    // Execute the insert statement
                    int rowsAffected = preparedStatement.executeUpdate();
//                    System.out.println(rowsAffected + " row(s) inserted.");
                    System.out.println("\n-------------------------------------");
                    System.out.println("*Withdraw was succesful*");
                    System.out.println("-------------------------------------\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void checkingBalance(int user_id) {
        System.out.println("--Checking Balance--");
        try {
//            System.out.println("Connecting to database...");
            String sql = "SELECT * FROM bank_user INNER JOIN bank_card ON bank_user.user_id = bank_card.user_id";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery(sql);
            while(rs.next()) {
                if(rs.getInt("user_id") == user_id) {
                    System.out.println("\n-------------------------------------");
                    System.out.println("Balance: " + rs.getDouble("balance"));
                    System.out.println("-------------------------------------\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void transfer(int user_id) {
        System.out.println("--Transfer--");
        double transferValue = 0;
        String receiverCard = "null";
        List<String> validCardNumbers = new ArrayList<>();
        Scanner input = new Scanner(System.in);

        try {

            String sql = "SELECT * FROM bank_user INNER JOIN bank_card ON bank_user.user_id = bank_card.user_id";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery(sql);

            String sql2 = "SELECT card_number FROM bank_card";
            PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
            ResultSet rs2 = preparedStatement2.executeQuery(sql2);

            while(rs2.next()) {
                validCardNumbers.add(rs2.getString("card_number"));
            }

            System.out.println(validCardNumbers);

            while(!validCardNumbers.contains(receiverCard)) {
                System.out.println("Enter the receiver's card number:");
                receiverCard = input.nextLine();
            }
            while(rs.next()) {
                if(rs.getInt("user_id") == user_id) {
                    double old_balance = rs.getDouble("balance");

                    while (transferValue <= 0 || transferValue > old_balance) {
                        System.out.println("Enter the amount you want to send:");
                        transferValue = input.nextDouble();

                        if (transferValue > old_balance) {
                            System.out.println("Insufficient balance");
                        }
                    }
                    sql = "UPDATE Bank_Card SET balance = CASE WHEN user_id = ? THEN balance - ? WHEN card_number = ? THEN balance + ? ELSE 0 END WHERE user_id = ? OR card_number = ?";
                    preparedStatement = connection.prepareStatement(sql);


                    preparedStatement.setInt(1, user_id);
                    preparedStatement.setDouble(2, (transferValue));

                    preparedStatement.setString(3, receiverCard);
                    preparedStatement.setDouble(4, (transferValue));

                    preparedStatement.setInt(5, user_id);
                    preparedStatement.setString(6, receiverCard);
                    // Execute the insert statement
                    int rowsAffected = preparedStatement.executeUpdate();
//                    System.out.println(rowsAffected + " row(s) inserted.");
                    System.out.println("\n-------------------------------------");
                    System.out.println("*Transfer was succesful*");
                    System.out.println("-------------------------------------\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
