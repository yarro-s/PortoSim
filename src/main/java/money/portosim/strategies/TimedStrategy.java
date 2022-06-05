package money.portosim.strategies;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import money.portosim.Portfolio;
import money.portosim.containers.Quote;

/**
 *
 * @author yarrik
 */
public class TimedStrategy extends StrategyChain {

    private final ChronoUnit rebalanceFrame;
    private LocalDate lastRebalanced;
    
    public TimedStrategy(ChronoUnit rebalancePeriod) {
        this.rebalanceFrame = rebalancePeriod; 
    } 

    @Override
    public Portfolio apply(Date date, Quote prices) {
        if (needsRebalance(date)) {
            return runNextStrategy(date, prices);
        }
        return null;
    }
    
    private boolean needsRebalance(Date date) {
        var localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        if (lastRebalanced == null) {
            lastRebalanced = localDate;
            return true;
        } 
        if (rebalanceFrame.between(lastRebalanced, localDate) >= 1) {
            lastRebalanced = localDate;
            return true;
        }
        return false;
    }
}
