package fraise.db;

public interface Values extends Command{
    String getKey();
    String getDefaultValue();
    String prefix();
}
