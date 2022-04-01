package money.portosim.containers;

import money.portosim.containers.generic.TaggedOrderedMap;

import java.util.Date;
import java.util.Map;

public class PriceSeries extends TaggedOrderedMap<Date, PriceMap> {
    public PriceSeries(Map<Date, PriceMap> m) {
        super(m);
    }

    public PriceSeries() {
        super();
    }
}
