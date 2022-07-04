/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim.core.containers;

import java.util.Map;
import java.util.Set;
import money.portosim.containers.NumericMap;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author yarro
 */
public class NumericMapTest {
    
    @Test
    public void addQuoteToNumericMaps() {
        var nm1 = new NumericMap<String>(Map.of("A", 100.5, "B", 800.0));
        var quote2 = NumericMap.of(Map.of("A", 5.0, "B", 100.0));       

        var sum12 = nm1.add(quote2);

        Assert.assertEquals(sum12.keySet(), Set.of("A", "B"));
        Assert.assertEquals(sum12.get("A"), 100.5 + 5.0);
        Assert.assertEquals(sum12.get("B"), 800.0 + 100.0);
        
        
        var nm3 = new NumericMap<String>(Map.of("A", 5.0, "B", 2.5));
        
        var res123 = nm1.add(quote2).mult(nm3);

        Assert.assertEquals(res123.get("A"), (100.5 + 5.0) * 5.0);
        Assert.assertEquals(res123.get("B"), (800.0 + 100.0) * 2.5);
    }
    
    @Test
    public void chainOpNumericMaps() {
        var nm1 = new NumericMap<String>(Map.of("A", 10.0, "B", 9000.0));
        var nm2 = new NumericMap<String>(Map.of("A", -100.0, "B", 50000.0));
        var nm3 = new NumericMap<String>(Map.of("A", 7.0, "B", 30.0));       

        var res123 = nm1.add(nm2).mult(nm3);

        Assert.assertEquals(res123.keySet(), Set.of("A", "B"));
        Assert.assertEquals(res123.get("A"), (10.0 + -100.0) * 7.0);
        Assert.assertEquals(res123.get("B"), (9000.0 + 50000.0) * 30.0);
    }
    
    @Test
    public void sumNumericMaps() {
        var nm1 = new NumericMap<String>(Map.of("A", 10.0, "B", 9000.0));
        var nm2 = new NumericMap<String>(Map.of("A", -100.0, "B", 50000.0));

        var sum12 = nm1.add(nm2);

        Assert.assertEquals(sum12.keySet(), Set.of("A", "B"));
        Assert.assertEquals(sum12.get("A"), 10.0 + -100.0);
        Assert.assertEquals(sum12.get("B"), 9000.0 + 50000.0);
    }
}
