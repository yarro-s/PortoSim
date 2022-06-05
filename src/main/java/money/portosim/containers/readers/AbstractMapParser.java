package money.portosim.containers.readers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public abstract class AbstractMapParser<R, K, V> {

    public Map<K, V> parseAll(List<R> rawRecords) {
        Map<K, V> m = new HashMap<>();

        IntStream.range(0, rawRecords.size()).forEach(recIdx ->
                m.put(recordToKey(recIdx), recordToVal(rawRecords.get(recIdx))));

        return m;
    }

    protected abstract K recordToKey(int recIdx);

    protected abstract V recordToVal(R rawRecord);
}
