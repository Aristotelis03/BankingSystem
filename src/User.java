import java.sql.*;
import java.util.Scanner;

public class User {
    int logedInUserId = -1;
    public int login() {
        System.out.println("--Log In--");
//        System.out.println("Under maintenance");

        // Ask for  users card number and pin
        Scanner input = new Scanner(System.in);

        System.out.println("Enter your Card Number: ");
        String card_number = input.nextLine();

        System.out.println("Enter your PIN: ");
        String card_pin = input.nextLine();

        // Retrieve the corresponding data from the database compare them with the user's entries

        String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:XE"; // Note the "XE" SID
        String username = "SYSTEM";
        String password = "21372003";

        try {
//            System.out.println("Connecting to database...");
            Connection connection = DriverManager.getConnection(jdbcUrl,username,password);
            String sql = "SELECT * FROM bank_user INNER JOIN bank_card ON bank_user.user_id = bank_card.user_id";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery(sql);
            while(rs.next()) {
//                String card_number_db = rs.getString(card_number);
                if (card_number.equals(rs.getString("card_number")) && card_pin.equals(rs.getString("pin"))) {
                    logedInUserId = rs.getInt("user_id");
                    System.out.println("\n-------------------------------------");
                    System.out.println("Welcome " + rs.getString("name") + " " + rs.getString("surname")) ;
                    System.out.println("-------------------------------------\n");
                }
            }
            } catch (SQLException e) {
            e.printStackTrace();
        }


        return logedInUserId;
    }

    public void register() {
        System.out.println("--Register--");
        Scanner input = new Scanner(System.in);

        System.out.println("Enter your name: ");
        String name = input.nextLine();

        System.out.println("Enter your surname: ");
        String surname = input.nextLine();

        String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:XE";
        String username = "SYSTEM";
        String password = "21372003";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {

            String sql = "INSERT INTO Bank_User (name, surname) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[] {"user_id"});

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);

            // Execute the insert statement
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted.");

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int user_id = generatedKeys.getInt(1); // Assuming ID is of type NUMBER
                System.out.println("Your account have been setup");
                 Card.createCard(user_id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
