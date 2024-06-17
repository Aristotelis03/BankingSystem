import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class Card {

    public static String generateCardNumber() {
        Random random = new Random();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i< 8; i++) {
            int digit = random.nextInt(10);
            result.append(digit);
        }
        return result.toString();
    }

    public static String generateCardPin() {
        Random random = new Random();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i< 4; i++) {
            int digit = random.nextInt(10);
            result.append(digit);
        }
        return result.toString();
    }

    public static void createCard(int user_id) {
        System.out.println("Creating card...");
        double balance = 1000.00;

        String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:XE"; // Note the "XE" SID
        String username = "SYSTEM";
        String password = "000000";

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl,username,password);
            String sql = "INSERT INTO Bank_Card (card_id, user_id, card_number, pin, balance) VALUES (? ,? ,? ,? ,? )";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            Random randomPin = new Random();
            int card_id = randomPin.nextInt(100000);

            preparedStatement.setInt(1, card_id);
            preparedStatement.setInt(2, user_id);
            preparedStatement.setString(3, generateCardNumber());
            preparedStatement.setString(4, generateCardPin());
            preparedStatement.setDouble(5, balance);

            int rowsAffected = preparedStatement.executeUpdate();
//            System.out.println(rowsAffected + " row(s) inserted.");
            System.out.println("\n-------------------------------------");
            System.out.println("Your card is ready");
            System.out.println("-------------------------------------\n");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

