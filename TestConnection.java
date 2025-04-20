public class TestConnection {
    public static void main(String[] args) {
        try {
            var conn = DatabaseUtil.getConnection();
            System.out.println("Connection successful!");
            conn.close();
        } catch (Exception e) {
            System.out.println("Connection failed:");
            e.printStackTrace();
        }
    }
}
