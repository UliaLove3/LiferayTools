package fraise.db.LiferayRepo;

import fraise.db.Repo;
import fraise.db.Values;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

public abstract class LiferayRepo<V> implements Repo<Values, V>{
    public void setAttribute(HttpServletRequest HttpServletRequest, Values[] values) throws SQLException {
        for(Values i:values){
            HttpServletRequest.setAttribute(i.prefix() +"_"+ i.getKey(), read(i));
        }
    }
}
