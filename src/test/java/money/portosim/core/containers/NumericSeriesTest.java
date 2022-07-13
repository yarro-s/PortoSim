/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim.core.containers;

import money.portosim.containers.NumericSeries;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author yarro
 */
public class NumericSeriesTest { 
    
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
