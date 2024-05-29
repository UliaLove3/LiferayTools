package fraise.db;

public interface Repo<K, V> {
    V read(K key);
    void save(K key, V value);
}
