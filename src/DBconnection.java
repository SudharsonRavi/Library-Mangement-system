import java.sql.Connection;
import java.sql.DriverManager;

public class DBconnection{
    static final String jdbc = "jdbc:mysql://localhost:3306/library";
    static final String user="root";
    static final String pass ="sudha@2004";

    public static Connection getConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(jdbc,user,pass);
        }
        catch (Exception e){
          e.printStackTrace();
          return null;
        }
    }
}
