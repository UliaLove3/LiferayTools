package fraise.db.manager;

public enum DataBaseType{
    MYSQL("MySQL"),
    POSTGRESQL("PostgreSQL"),;
    private String name;

    DataBaseType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

