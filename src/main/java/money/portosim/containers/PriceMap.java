package money.portosim.containers;

import money.portosim.containers.generic.NumericMap;

import java.util.Map;

public class PriceMap extends NumericMap<String> {
    public PriceMap(Map<String, Double> m) {
        super(m);
    }

    public PriceMap() {
        super();
    }
}
