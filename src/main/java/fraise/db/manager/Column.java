package fraise.db.manager;

public class Column {
    private String name;
    private String type;

    private Column(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public static Column createPrimaryKey(String name, DataBaseType type){
        switch (type){
            case MYSQL:{
                return new Column(name,"INT AUTO_INCREMENT PRIMARY KEY");
            }
            case POSTGRESQL:{
                return new Column(name, "SERIAL PRIMARY KEY");
            }
        }
        throw new RuntimeException("Not supported DB");
    }

    public static Column createLong(String name, DataBaseType type){
        switch (type){
            case MYSQL:{
                return new Column(name,"LONG");
            }
            case POSTGRESQL:{
                return new Column(name, "BIGINT");
            }
        }
        throw new RuntimeException("Not supported DB");
    }

    public static Column createText(String name, DataBaseType type){
        return new Column(name, "TEXT");
    }
}
