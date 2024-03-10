package not.mepipe.meconomy.utils;

import java.util.ArrayList;

public class IndexedHashmap<K, V> {

    private final ArrayList<Integer> keys = new ArrayList<>();
    private final ArrayList<K> firsts = new ArrayList<>();
    private final ArrayList<V> seconds = new ArrayList<>();

    public void add(K first, V second) {
        keys.add(keys.size() + 1);
        firsts.add(first);
        seconds.add(second);
    }

    public void clear() {
        keys.clear();
        firsts.clear();
        seconds.clear();
    }

    public K getFirst(int key) {
        return firsts.get(key);
    }

    public V getSecond(int key) {
        return seconds.get(key);
    }

    public int size() {
        return keys.size();
    }

}
