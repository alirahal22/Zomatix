package sample.Model.Database;

import java.sql.Connection;
import java.sql.DriverManager;

//Singleton

public class DatabaseConnection {
    private static DatabaseConnection databaseConnection;
    private Connection connection;

    public static DatabaseConnection getDatabaseConnection(){
        if (databaseConnection == null)
            databaseConnection = new DatabaseConnection();
        return databaseConnection;
    }

    private DatabaseConnection() {
        this.connection = createConnection();
    }

    private Connection createConnection(){
        connection = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Restaurant", "root", "root");
        }catch(Exception e){
            e.printStackTrace();
        }
        return connection;
    }

    public Connection getConnection(){
        return this.connection;
    }
}
