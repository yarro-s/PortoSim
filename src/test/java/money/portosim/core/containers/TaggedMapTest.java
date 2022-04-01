package money.portosim.core.containers;

import money.portosim.containers.generic.TaggedMap;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;


/**
 * Unit test for simple App.
 */
public class TaggedMapTest {

    @Test
    public void multipleMatchingKeysComposed() {
        var tm1 = new TaggedMap<String, Integer>();
        var tm2 = new TaggedMap<String, Integer>();

        tm1.put("K1", 5);
        tm1.put("K2", 20);
        tm1.put("L", 400);

        tm2.put("K1", 10);
        tm2.put("K2", -25);
        tm2.put("D", 2500);

        TaggedMap<String, Integer> c12 = tm1.compose(tm2, (x, y) -> x + y);

        Assert.assertEquals(c12.size(), 2);
        Assert.assertEquals(c12.get("K1"), 5 + 10);
        Assert.assertEquals(c12.get("K2"), 20 + -25);
    }

    @Test
    public void matchingKeyComposed() {
        var tm1 = new TaggedMap<String, Integer>();
        var tm2 = new TaggedMap<String, Integer>();

        tm1.put("K", 5);
        tm1.put("L", 400);

        tm2.put("K", 10);
        tm2.put("D", 2500);

        TaggedMap<String, Integer> c12 = tm1.compose(tm2, (x, y) -> x * y);

        Assert.assertEquals(c12.size(), 1);
        Assert.assertEquals(c12.get("K"), 5*10);
    }
}
