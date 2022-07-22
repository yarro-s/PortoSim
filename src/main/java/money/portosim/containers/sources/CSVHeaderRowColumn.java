package money.portosim.containers.sources;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author yarro
 */
public abstract class CSVHeaderRowColumn<K, V> extends MapParser<K, V> {

    private final List<String> header;
    private final Map<String, Map<String, String>> mappedRecords = new HashMap<>();

    private Map<K, V> records;

    public CSVHeaderRowColumn(FileReader fileReader) throws IOException {
        var reader = new BufferedReader(fileReader);

        header = Arrays.asList(reader.readLine().split(","));

        while (true) {
            try {
                String record;
                if ((record = reader.readLine()) == null) {
                    break;
                } else {
                    var parsedRecord = Arrays.asList(record.split(","));
                    Map<String, String> mappedRecord = IntStream.range(1, header.size()).boxed()
                            .collect(Collectors.toMap(header::get, i -> parsedRecord.get(i)));

                    mappedRecords.put(parsedRecord.get(0), mappedRecord);
                }
            } catch (IOException e) {
            }
        }
    }

    protected abstract K toKey(String rawKey);

    protected abstract V toValue(Map<String, String> mappedRecord);

    @Override
    public Set<K> keySet() {
        if (records == null) {
            records = mappedRecords.keySet().stream().collect(Collectors
                    .toMap(this::toKey, k -> toValue(mappedRecords.get(k))));
        }
        return records.keySet();
    }

    @Override
    public V get(Object key) {
        return records.get(key);     
    }
}
