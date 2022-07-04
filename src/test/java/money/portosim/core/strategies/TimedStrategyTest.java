package money.portosim.core.strategies;

import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import money.portosim.Portfolio;
import money.portosim.containers.NumericMap;
import money.portosim.strategies.FixedAllocation;
import money.portosim.strategies.TimedStrategy;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author yarrik
 */
public class TimedStrategyTest {
    
    private NumericMap<String> targetAlloc;
    private Map<String, Double> currentPrices;
    private Map<String, Double> updatedPrices1;
    private Map<String, Double> updatedPrices2;
    private Date initDate;
    private Date noUpdateDate;
    private Date updateDate;
    
    @BeforeMethod
    public void setUp() {
        targetAlloc = new NumericMap<>(Map.of("A", 0.4, "B", 0.6));
        
        currentPrices = Map.of("A", 10.0, "B", 60.0);
        updatedPrices1 = Map.of("A", 200.0, "B", 350.0);
        updatedPrices2 = Map.of("A", 500.0, "B", 100.0);
        
        initDate = new GregorianCalendar(2009, Calendar.DECEMBER, 10).getTime();
        noUpdateDate = new GregorianCalendar(2009, Calendar.DECEMBER, 21).getTime();
        updateDate = new GregorianCalendar(2010, Calendar.FEBRUARY, 20).getTime();
    }
    
    @Test
    public void timedUpdateTest() throws CloneNotSupportedException {
        var fixedAlloc = new FixedAllocation(targetAlloc);
        var timedAlloc = new TimedStrategy(ChronoUnit.MONTHS);
        
        timedAlloc.chainTo(fixedAlloc); 

        var pf1 = (Portfolio) timedAlloc.makePortfolio(initDate, currentPrices).clone();
        var pf2 = timedAlloc.makePortfolio(noUpdateDate, updatedPrices1);

        Assert.assertEquals(pf1.toString(), pf2.toString());

        var pf3 = timedAlloc.makePortfolio(updateDate, updatedPrices2);
        
        Assert.assertNotEquals(pf2.toString(), pf3.toString());
    }
}
