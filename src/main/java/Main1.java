import java.sql.*;
import java.time.LocalDateTime;

public class Main1 {


    private static final String SQL = "SELECT COUNT(*) FROM dq_rule";
    private static final String SQL2 = "INSERT INTO log (date, count) VALUES (?, ?)";


    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException {
        if (args.length != 3) {
            System.out.println("Incorrect number of args");
            return;
        }

        String login = args[0];
        String password = args[1];
        String url = args[2];

        Class.forName("org.postgresql.Driver");
        try(
                Connection connection = DriverManager.getConnection(url, login, password);
                PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                PreparedStatement preparedStatement2 = connection.prepareStatement(SQL2);
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();
            long count = 0;
            if (resultSet.next()) {
                count = resultSet.getLong(1);
            }
            preparedStatement2.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement2.setLong(2, count);
            preparedStatement2.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
