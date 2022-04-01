package money.portosim.containers.generic;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractTaggedMap<K, V> extends AbstractMap<K,V>
        implements Map<K,V>, Cloneable, Serializable, Composable<K, V> {

    protected abstract Map<K, V> asMap();

    @Override
    public <T, R> Map<K, R> compose(Map<K, T> tm, BiFunction<V, T, R> f) {
        var intersection = new HashSet<>(this.keySet());
        intersection.retainAll(tm.keySet());

        return intersection.stream().collect(Collectors.toMap(Function.identity(),
                k -> f.apply(this.get(k), tm.get(k))));
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return asMap().entrySet();
    }

    @Override
    public V put(K key, V value) {
        return asMap().put(key, value);
    }
}
