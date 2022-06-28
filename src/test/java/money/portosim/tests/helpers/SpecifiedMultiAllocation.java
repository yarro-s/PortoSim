package money.portosim.tests.helpers;

import java.util.Date;
import java.util.List;
import java.util.Map;
import money.portosim.AbstractStrategy;
import money.portosim.Portfolio;
import money.portosim.containers.numeric.NumFrame;

/**
 *
 * @author yarro
 */
public class SpecifiedMultiAllocation extends AbstractStrategy {

    int step = 0;
    Map<String, List<Double>> allocs;

    @Override
    protected Portfolio apply(Date date, NumFrame<String> prices) {
        var portfolio = new Portfolio();
        
        for (var entry : allocs.entrySet()) {
            var assetID = entry.getKey();
            var allocT = entry.getValue();

            if (step >= allocT.size()) {
                step = 0;
            }
            double w = allocT.get(step);
            portfolio.positions().put(assetID, w);
        }
        step++;
        return portfolio;
    }

    public SpecifiedMultiAllocation(Map<String, List<Double>> allocs) {
        this.allocs = allocs;
    }
}
