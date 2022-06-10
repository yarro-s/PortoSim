/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim.core.containers;

import money.portosim.Metrics;
import money.portosim.containers.NumericSeries;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author yarro
 */
public class NumericSeriesTest { 
    
    @Test
    public void rollingWindowAverage() {
        var ns = new NumericSeries();
        
        ns.put("2010-01-01", 100.0);
        ns.put("2010-02-01", 120.0);
        ns.put("2010-03-01", 90.0);
        ns.put("2010-04-01", 15.5);
        ns.put("2010-05-01", 32.0);
        ns.put("2010-06-01", 60.0);
        ns.put("2010-07-01", 75.0);

        var nsAverage = ns.rolling(3, (s) -> Metrics.average(s.values()));

        var expAverage = new NumericSeries();
        
        expAverage.put("2010-03-01", (100.0 + 120.0 + 90.0) / 3);
        expAverage.put("2010-04-01", (120.0 + 90.0 + 15.5) / 3);
        expAverage.put("2010-05-01", (90.0 + 15.5 + 32.0) / 3);
        expAverage.put("2010-06-01", (15.5 + 32.0 + 60.0) / 3);
        expAverage.put("2010-07-01", (32.0 + 60.0 + 75.0) / 3);
        
        Assert.assertEquals(nsAverage, expAverage);
    }
    
    @Test
    public void volatilityCalc() {
        var ns = new NumericSeries();
        ns.put("2010-01-01", 100.0);
        ns.put("2010-02-01", 120.0);
        ns.put("2010-03-01", 90.0);
        ns.put("2010-04-01", 135.0);

        var actualVolatility = ns.volatility().orElse(0.0);

        var expAverage = (100.0 + 120.0 + 90.0 + 135.0) / 4.0;
        var expVolatility = Math.sqrt((Math.pow(100.0 - expAverage, 2.0) 
                + Math.pow(120.0 - expAverage, 2.0) + Math.pow(90.0 - expAverage, 2.0) 
                + Math.pow(135.0 - expAverage, 2.0)) / 4.0);

        Assert.assertEquals(actualVolatility, expVolatility);
    }
    
    @Test
    public void composeNumericSeries() {
        var ns1 = new NumericSeries();
        ns1.put("2010-01-01", 10.5);
        ns1.put("2011-01-01", 100.2);
               
        var ns2 = new NumericSeries();
        ns2.put("2010-01-01", 255.0);
        ns2.put("2011-01-01", 2050.0);
                
        var ns3 = new NumericSeries();
        ns3.put("2010-01-01", 5.0);
        ns3.put("2011-01-01", 12.5);
        
        var sum12 = ns1.add(ns2);
        
        Assert.assertEquals(sum12.size(), 2);
        Assert.assertEquals(sum12.get("2010-01-01"), 10.5 + 255.0);
        Assert.assertEquals(sum12.get("2011-01-01"), 100.2 + 2050.0);
        
        var res123 = ns1.add(ns2).mult(ns3);
        
        Assert.assertEquals(res123.get("2010-01-01"), (10.5 + 255.0) * 5.0);
        Assert.assertEquals(res123.get("2011-01-01"), (100.2 + 2050.0) * 12.5);
    }
                           
}
