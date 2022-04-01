package money.portosim.containers.generic;

import java.util.Map;
import java.util.function.BiFunction;

public interface Composable<K, V> {
    <T, R> Map<K, R> compose(Map<K, T> tm, BiFunction<V, T, R> f);
}
