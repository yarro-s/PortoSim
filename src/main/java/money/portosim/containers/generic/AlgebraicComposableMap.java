package money.portosim.containers.generic;

import java.util.Map;

public interface AlgebraicComposableMap<K, V> extends Composable<K, V>, Map<K, V> {
    AlgebraicComposableMap<K, V> multiply(AlgebraicComposableMap<K, V> ntm);

    AlgebraicComposableMap<K, V> multiply(V value);

    AlgebraicComposableMap<K, V> divide(AlgebraicComposableMap<K, V> ntm);

    AlgebraicComposableMap<K, V> divide(V value);

    AlgebraicComposableMap<K, V> subtract(AlgebraicComposableMap<K, V> ntm);

    AlgebraicComposableMap<K, V> subtract(V value);

    Double sum();

    AlgebraicComposableMap<K, V> add(AlgebraicComposableMap<K, V> tm);

    AlgebraicComposableMap<K, V> add(V value);
}
