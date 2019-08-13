import java.util.ArrayList;

/**
 * @author Alexey Goncharenko
 * @version 1.0
 * @since 27.05.16.
 * @appName "Chat Master"
 */

public class MainClass {
    public static void main(String[] args) {
        ChatWnd chatWnd = new ChatWnd();

        ConnectDB connectDB = new ConnectDB();
        String typeDB = "org.sqlite.JDBC";
        String nameDB = "jdbc:sqlite:db/ChatMasterDB.db";

        if(connectDB.setTypeAndNameDB(typeDB, nameDB, chatWnd)){
            chatWnd.appendData2TxtArea("Connection to database is succeed...");
            // чтение данных из БД
            String query = "SELECT * FROM Main";
            connectDB.readData(query, chatWnd);
            // формируем массив запросов на добавление данных
            ArrayList<String> queryArr = new ArrayList<String>();
            for (int i = 1; i < 11; i++) {
                query = String.format("INSERT INTO Main (fname, login) VALUES('%s', '%s')", "FName " + i, "Login " + i);
                queryArr.add(query);
            }
            // запись данных в БД
            connectDB.writeData(queryArr, chatWnd);
            // вновь читаем всё содержимое из таблицы
            query = "SELECT * FROM Main WHERE id < 11";
            connectDB.readData(query, chatWnd);
        }else
            chatWnd.appendData2TxtArea("Connection to database is failed...");
    }
}
