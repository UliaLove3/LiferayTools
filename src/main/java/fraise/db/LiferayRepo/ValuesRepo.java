package fraise.db.LiferayRepo;

import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import fraise.db.Repo;
import fraise.db.Values;
import fraise.db.manager.Column;
import fraise.db.manager.DBWorker;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

public class ValuesRepo extends LiferayRepo<String> {
    private final static String TABLE_NAME = ValuesRepo.class.getName().replace(".", "_");
    private final ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
    private final Connection connection;
    private static ValuesRepo valuesRepo;

    protected ValuesRepo(){connection =null;}

    private ValuesRepo(Connection connection) throws SQLException {
        this.connection = connection;
        DBWorker dbWorker = new DBWorker(connection);
        Column columns[] = {
                Column.createText("KEYWORD", dbWorker.getDatabaseType()),
                Column.createText("VALUE_U", dbWorker.getDatabaseType()),
        };
        dbWorker.createTableIfNotExists(TABLE_NAME, columns);
        valuesRepo = this;
    }

    public String hashRead(Values key) throws SQLException {
        String dbKey = key.prefix() +key.getKey();
        if(map.containsKey(dbKey)){
            return map.get(dbKey);
        }
        String value = valuesRepo.read(key);
        map.put(dbKey, value);
        return value;
    }

    public String hashRead(Values key, String defaultValue) throws SQLException {
        String dbKey = key.prefix() +key.getKey();
        if(map.containsKey(dbKey)){
            return map.get(dbKey);
        }
        String value = valuesRepo.readOrDefault(key, defaultValue);
        map.put(dbKey, value);
        return value;
    }


    @Deprecated
    public String read(Values key){
        try {
            String query = "SELECT * FROM "+TABLE_NAME + " WHERE KEYWORD = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, key.prefix() +key.getKey());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getString("VALUE_U");
            }
            return key.getDefaultValue();
        }catch (SQLException ex){
            return key.getDefaultValue();
        }
    }

    @Deprecated
    public String readOrDefault(Values key, String defaultValue) throws SQLException {
        String query = "SELECT * FROM "+TABLE_NAME + " WHERE KEYWORD = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, key.prefix() +key.getKey());
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            return resultSet.getString("VALUE_U");
        }else{
            return defaultValue;
        }
    }

    public void save(Values key, String value){
        try {
            String dbKey = key.prefix() + key.getKey();
            map.put(dbKey, value);
            PreparedStatement preparedStatement;
            if(read(key)==null){
                String query = "INSERT INTO " + TABLE_NAME + " (KEYWORD, VALUE_U) VALUES (?, ?);";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, key.prefix() + key.getKey());
                preparedStatement.setString(2, value);
            }else{
                String query = "UPDATE "+TABLE_NAME+" SET VALUE_U = ?" +
                        "WHERE KEYWORD = ?;";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, value);
                preparedStatement.setString(2, key.prefix() + key.getKey());
            }
            preparedStatement.execute();
        }catch (SQLException ex){
            ex.printStackTrace();
        }



    }



    public static ValuesRepo getInstance() throws SQLException {
        if(valuesRepo==null){
            Connection connection = DataAccess.getConnection();
            valuesRepo = new ValuesRepo(connection);
        }
        return valuesRepo;
    }
}
