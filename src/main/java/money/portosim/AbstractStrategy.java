package money.portosim;

import money.portosim.containers.PriceMap;

import java.util.Date;

public abstract class AbstractStrategy implements Strategy {
    private Portfolio portfolio;
    
    @Override
    public final Portfolio makePortfolio(Date date, PriceMap prices) {
        var result = apply(date, prices);
        if (result == null) {
            result = portfolio;
        } else {
            portfolio = result;
        }
        return result;
    }
    
    protected abstract Portfolio apply(Date date, PriceMap prices);
     
    protected Portfolio getPortfolio() {
        return portfolio;
    }
}
