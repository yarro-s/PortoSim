package money.portosim.core.containers;

import money.portosim.containers.generic.TaggedOrderedMap;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class TaggedOrderedMapTest {

    @Test
    public void rollingWindowCalc() {
        var orderedMap = new TaggedOrderedMap<>(Map.of(1, "A", 2, "B", 3, "C", 4,
                "D", 5, "E", 6, "F", 7, "G", 8, "H", 9, "I"));

        Function<Collection<String>, String> concat = (var cs) -> cs.stream().reduce(String::concat).orElse("");

        var rollRes = orderedMap.rolling(3, concat);
        var expRes = new TaggedOrderedMap<>(Map.of(3, "ABC", 4, "BCD", 5, "CDE",
                6, "DEF", 7, "EFG", 8, "FGH", 9, "GHI"));

        Assert.assertEquals(rollRes.size(), orderedMap.size() - 3 + 1);
        Assert.assertEquals(rollRes, expRes);
    }

    @Test
    public void orderedSlice() {
        var orderedMap = new TaggedOrderedMap<>(Map.of("A", 100, "B", 20, "C", 3000,
                "D", 4, "E", 50, "F", 600));

        var slicedMap = orderedMap.slice(1, 3);

        var expSlicedMap = Map.of("B", 20, "C", 3000, "D", 4);
        Assert.assertEquals(slicedMap, expSlicedMap);
    }
}
