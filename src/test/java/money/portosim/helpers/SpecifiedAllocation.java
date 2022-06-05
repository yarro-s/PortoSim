package money.portosim.helpers;

import java.util.Date;
import java.util.Map;
import money.portosim.AbstractStrategy;
import money.portosim.Portfolio;
import money.portosim.containers.Quote;

/**
 *
 * @author yarro
 */
public class SpecifiedAllocation extends AbstractStrategy {

    int step = 0;
    double[] allocation;
    private final String assetID;

    @Override
    protected Portfolio apply(Date date, Quote prices) {
        if (step >= allocation.length) {
            step = 0;
        }
        double w = allocation[step++];
        return new Portfolio(Map.of(assetID, w));
    }

    public SpecifiedAllocation(String assetID, double[] allocation) {
        this.assetID = assetID;
        this.allocation = allocation;
    }

}
