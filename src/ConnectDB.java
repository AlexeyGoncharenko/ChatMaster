import java.sql.*;
import java.util.ArrayList;

public class ConnectDB {

    private Statement statement;    // позволяет отправлять запросы в БД
    private Connection connection;  // работа с соединением(связью) с БД
    private boolean isActive;

    public ConnectDB(){
        isActive = false;
    }

    public boolean setTypeAndNameDB(String typeDB, String nameDB, ChatWnd wnd) {
        // регистрируем подключаемый драйвер
        try {
            Class.forName(typeDB);
        } catch (ClassNotFoundException e) {
            wnd.appendData2TxtArea("ERROR:" + e.getMessage());
            return false;
        }
        // если регистрация драйвера прошла успешно, создаем соединение с БД
        try {
            connection = DriverManager.getConnection(nameDB);
            statement = connection.createStatement();
        } catch (SQLException e) {
            wnd.appendData2TxtArea("ERROR:" + e.getMessage());
            return false;
        }
        isActive = true;
        return isActive;
    }

    public boolean readData(String query, ChatWnd wnd){
        ResultSet resultSet;
        if (isActive){
            try {
                resultSet = statement.executeQuery(query);
                int colCount = resultSet.getMetaData().getColumnCount();
                StringBuilder row = new StringBuilder("");
                while(resultSet.next()){
                    for (int i = 1; i <= colCount ; i++)
                        row.append(resultSet.getString(i) + " ");
                    wnd.appendData2TxtArea(row.toString());
                    row.delete(0, row.capacity());
                }
            } catch (SQLException e) {
                wnd.appendData2TxtArea("ERROR:" + e.getMessage());
                return false;
            }
        } else
            return false;
        return true;
    }

    public boolean writeData(ArrayList<String> query, ChatWnd wnd){
        int qSize = query.size();
        if (isActive){
            try {
                connection.setAutoCommit(false);
                for (int i = 0; i < qSize; i++) {
                    statement.execute(query.get(i));
                }
                connection.commit();
            } catch (SQLException e) {
                wnd.appendData2TxtArea("ERROR:" + e.getMessage());
                return false;
            }
        } else
            return false;
        return true;
    }

    public Statement getStatement() {
        return statement;
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isActive() {
        return isActive;
    }


}
