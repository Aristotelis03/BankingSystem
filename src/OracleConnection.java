import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleConnection {
    public static void main() {
        String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:XE"; // Note the "XE" SID
        String username = "SYSTEM";
        String password = "21372003";


        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Connected to Oracle database!");
            // You can proceed with executing queries here
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}