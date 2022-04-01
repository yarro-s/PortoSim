package money.portosim.containers.generic;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class TaggedMap<K, V> extends AbstractTaggedMap<K, V> {
    private final Map<K, V> m;

    public TaggedMap(Map<K, V> m) {
        this.m = new HashMap<>(m);
    }

    public TaggedMap() {
        m = new HashMap<>();
    }

    @Override
    protected Map<K, V> asMap() {
        return m;
    }

    @Override
    public <T, R> TaggedMap<K, R> compose(Map<K, T> tm, BiFunction<V, T, R> f) {
        return new TaggedMap<>(super.compose(tm, f));
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new TaggedMap<>(asMap());
    }
}
