package money.portosim;

import java.util.Date;
import money.portosim.containers.numeric.NumFrame;

public abstract class AbstractStrategy implements Strategy {
    private Portfolio portfolio;
    
    @Override
    public final Portfolio makePortfolio(Date date, NumFrame<String> prices) {
        var result = apply(date, prices);
        if (result == null) {
            result = portfolio;
        } else {
            portfolio = result;
        }
        return result;
    }
    
    protected abstract Portfolio apply(Date date, NumFrame<String> prices);
     
    protected Portfolio getPortfolio() {
        return portfolio;
    }
}
