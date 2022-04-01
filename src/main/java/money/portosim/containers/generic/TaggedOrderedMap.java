package money.portosim.containers.generic;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class TaggedOrderedMap<K, V> extends AbstractTaggedMap<K, V> {
    private final NavigableMap<K, V> m;

    public TaggedOrderedMap<K, V> from(K startTag) {
        var endTag = ordered().lastKey();
        return new TaggedOrderedMap<>(ordered().subMap(startTag, true, endTag, true));
    }

    public TaggedOrderedMap<K, V> to(K endTag) {
        var startTag = ordered().firstKey();
        return new TaggedOrderedMap<>(ordered().subMap(startTag, true, endTag, true));
    }

    public TaggedOrderedMap<K, V> rolling(int nWindow, Function<Collection<V>, V> calc) {
        var resultMap = new TaggedOrderedMap<K, V>();
        var keySet = this.ordered().navigableKeySet().stream().toList();

        for (int windowStart = 0; windowStart < ordered().size() - nWindow + 1; windowStart++) {
            var calcRes = calc.apply(this.slice(windowStart, nWindow).values());

            resultMap.put(keySet.get(windowStart + nWindow - 1), calcRes);
        }
        return resultMap;
    }

    public TaggedOrderedMap<K, V> slice(int start, int n) {
        var orderedKeys = new ArrayList<>(ordered().navigableKeySet()).subList(start, start + n);
        var startTag = orderedKeys.get(0);
        var endTag = orderedKeys.get(orderedKeys.size() - 1);

        return from(startTag).to(endTag);
    }

    public TaggedOrderedMap(Map<K, V> m) {
        this.m = new TreeMap<>(m);
    }

    public TaggedOrderedMap() {
        m = new TreeMap<>();
    }

    public NavigableMap<K, V> ordered() {
        return m;
    }

    @Override
    protected Map<K, V> asMap() {
        return m;
    }

    @Override
    public <T, R> TaggedOrderedMap<K, R> compose(Map<K, T> tm, BiFunction<V, T, R> f) {
        return new TaggedOrderedMap<>(super.compose(tm, f));
    }
}
