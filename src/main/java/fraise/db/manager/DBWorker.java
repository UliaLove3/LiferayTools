package fraise.db.manager;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class DBWorker {
    private static final Log log = LogFactoryUtil.getLog(DBWorker.class);
    private final Connection connection;
    private DataBaseType databaseType;

    public DBWorker(Connection connection) throws SQLException {
        this.connection = connection;
//        log.info(connection.getMetaData().getDatabaseProductName());
        setDBType(connection.getMetaData().getDatabaseProductName());
    }

    private void setDBType(String dbType){
        if(dbType.equals(DataBaseType.MYSQL.toString())){
            databaseType = DataBaseType.MYSQL;
        }else if(dbType.equals(DataBaseType.POSTGRESQL.toString())){
            databaseType = DataBaseType.POSTGRESQL;
        }
    }

    public DataBaseType getDatabaseType() {
        return databaseType;
    }

    public void createTableIfNotExists(String tableName, Column... columns) throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS " + tableName;
        query += " (";
        for(Column i:columns){
            query+=i.getName() +" "+ i.getType()+", ";
        }
        query = query.substring(0, query.length()-2)+");";
        Statement statement = connection.createStatement();
        statement.execute(query);
    }
}

