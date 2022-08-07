package money.portosim.metrics;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author yarro
 */
class QuantWindow<K> implements Quant<K> {  
    
    private final List<Entry<K, QuantSpan>> qs;

    QuantWindow(QuantContext<K> context, int n) {      
        qs = IntStream.range(0, context.entries().size() - n + 1).boxed()
                .map(i -> {
                    var windowContext = context.range(i, i + n - 1);
                    var q = QuantSpan.of(windowContext);

                    var key = windowContext.entries().get(n - 1).getKey();
                    return Map.entry(key, q);
                }).toList();
    }
    
    QuantWindow(QuantContext<K> context) {
        this(context, 1);
    }

    @Override
    public Map<K, Double> apply(Function<QuantSpan, Double> f) {
        return qs.stream().map(e -> Map.entry(e.getKey(), f.apply(e.getValue())))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }
}
