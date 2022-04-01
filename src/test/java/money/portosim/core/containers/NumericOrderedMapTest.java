package money.portosim.core.containers;

import money.portosim.containers.generic.NumericOrderedMap;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class NumericOrderedMapTest {

    @Test
    public void volatilityCalc() {
        var numericMap = new NumericOrderedMap<>(Map.of("A", 100.0, "B", 120.0, "C", 90.0,
                "D", 135.0));

        var actualVolatility = numericMap.volatility().orElse(0.0);

        var expAverage = (100.0 + 120.0 + 90.0 + 135.0) / 4.0;
        var expVolatility = Math.sqrt((Math.pow(100.0 - expAverage, 2.0) + Math.pow(120.0 - expAverage, 2.0)
                + Math.pow(90.0 - expAverage, 2.0) + Math.pow(135.0 - expAverage, 2.0)) / 4.0);

        Assert.assertEquals(actualVolatility, expVolatility);
    }
}
