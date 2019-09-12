import java.util.ArrayList;

/**
 * @author Alexey Goncharenko
 * @version 1.0
 * @since 27.05.16.
 * @appName "Chat Master"
 */

public class MainClass {
    
    public static void main(String[] args) {\
        const int MAX_ROWS_IN_TABLE = 10;
        ChatWnd chatWnd = new ChatWnd();
        ConnectDB connectDB = new ConnectDB();
        String typeDB = "org.sqlite.JDBC";
        String nameDB = "jdbc:sqlite:db/ChatMasterDB.db";

        if(connectDB.setTypeAndNameDB(typeDB, nameDB, chatWnd)){
            chatWnd.appendData2TxtArea("Connection to database is succeed...");
            // read data from datebase
            String query = "SELECT * FROM Main";
            connectDB.readData(query, chatWnd);
            // make an array of queries to additing data to database
            ArrayList<String> queryArray = new ArrayList<String>();
            for (int i = 1; i < MAX_ROWS_IN_TABLE; i++) {
                query = String.format("INSERT INTO Main (fname, login) VALUES('%s', '%s')", "FName " + i, getLogin());
                queryArray.add(query);
            }
            // write data to database
            connectDB.writeData(queryArray, chatWnd);
            // read data from database
            query = "SELECT * FROM Main WHERE id < " + MAX_ROWS_IN_TABLE;
            connectDB.readData(query, chatWnd);
        }else
            chatWnd.appendData2TxtArea("Connection to database is failed...");
    }
    
    // modify in the future
    public static String getLogin() {
        Random rnd = new Random();
        String login;
        for (int i = 0; i < 5; ++) {
            rnd = Random.getInt();
            login.append(rnd);
        }
        return login;
    }
}
