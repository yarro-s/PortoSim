package money.portosim;

import java.util.Date;
import java.util.Map;

public abstract class AbstractStrategy implements Strategy {
    private Portfolio portfolio;
    
    @Override
    public final Portfolio makePortfolio(Date date, Map<String, Double> prices) {
        var result = apply(date, prices);
        if (result == null) {
            result = portfolio;
        } else {
            portfolio = result;
        }
        return result;
    }
    
    protected abstract Portfolio apply(Date date, Map<String, Double> prices);
     
    protected Portfolio getPortfolio() {
        return portfolio;
    }
}
