package money.portosim;

import java.util.Date;
import money.portosim.containers.Quote;

public abstract class AbstractStrategy implements Strategy {
    private Portfolio portfolio;
    
    @Override
    public final Portfolio makePortfolio(Date date, Quote prices) {
        var result = apply(date, prices);
        if (result == null) {
            result = portfolio;
        } else {
            portfolio = result;
        }
        return result;
    }
    
    protected abstract Portfolio apply(Date date, Quote prices);
     
    protected Portfolio getPortfolio() {
        return portfolio;
    }
}
