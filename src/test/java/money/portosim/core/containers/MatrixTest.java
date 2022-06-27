/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/EmptyTestNGTest.java to edit this template
 */
package money.portosim.core.containers;

import java.util.Map;
import money.portosim.containers.core.DataMatrix;
import money.portosim.containers.numeric.NumDataMatrix;
import money.portosim.containers.core.Pair;
import money.portosim.containers.core.Frame;
import money.portosim.containers.core.Matrix;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author yarro
 */
public class MatrixTest {
    
    final Matrix<Integer, String, Double> tf = new DataMatrix<>();

    @BeforeClass
    public void setUp() {
        tf.put(Pair.of(1, "A"), 100.5); tf.put(Pair.of(1, "B"), 6.2);
        tf.put(Pair.of(2, "A"), 245.0); tf.put(Pair.of(2, "B"), 8.1);
        tf.put(Pair.of(3, "A"), 307.4); tf.put(Pair.of(3, "B"), 2.8);
        tf.put(Pair.of(4, "A"), 843.2); tf.put(Pair.of(4, "B"), 5.0);
        tf.put(Pair.of(5, "A"), 367.4); tf.put(Pair.of(5, "B"), 1.5);
    }
    
    @Test
    public void sumAllFrame() {
        var nf = new NumDataMatrix<>(tf);
        double frameSum = nf.sum();
        
        Assert.assertEquals(frameSum, 100.5 + 245.0 + 307.4 + 843.2 + 367.4 + 
                6.2 + 8.1 + 2.8 + 5.0 + 1.5, 10e-6);
    }
    
    @Test
    public void addFrame() {
        var nf = new NumDataMatrix<>(tf);
        var otherNM = new NumDataMatrix<>(tf);
        
        otherNM.put(Pair.of(1, "A"), 64.1); otherNM.put(Pair.of(1, "B"), 15.9);
        otherNM.put(Pair.of(2, "A"), 15.0); otherNM.put(Pair.of(2, "B"), 65.1);
        otherNM.put(Pair.of(3, "A"), 30.5); otherNM.put(Pair.of(3, "B"), 32.2);
        otherNM.put(Pair.of(4, "A"), 43.0); otherNM.put(Pair.of(4, "B"), 15.0);
        otherNM.put(Pair.of(5, "A"), 32.4); otherNM.put(Pair.of(5, "B"), 125.5);
        
        var addedFrame = new NumDataMatrix<>(nf.add(otherNM));
        
        Assert.assertEquals(addedFrame.columns().get("A").get(1), 100.5 + 64.1);
        Assert.assertEquals(addedFrame.rows().get(5).get("B"), 1.5 + 125.5);
    }
       
    @Test
    public void addVal() {
        var nm = new NumDataMatrix<>(tf);
        var addedVal = new NumDataMatrix<>(nm.add(50000.6));
        
        Assert.assertEquals(addedVal.columns().get("A").get(1), 100.5 + 50000.6);
        Assert.assertEquals(addedVal.rows().get(5).get("B"), 1.5 + 50000.6);
    }
    
    @Test
    public void matrixDataAccess() {
        final Matrix<Integer, String, Double> mx = new DataMatrix<>(Map.of(
                Pair.of(1, "A"), 100.5, Pair.of(1, "B"), 6.2,
                Pair.of(2, "A"), 245.0, Pair.of(2, "B"), 8.1,
                Pair.of(3, "A"), 307.4, Pair.of(3, "B"), 2.8,
                Pair.of(4, "A"), 843.2, Pair.of(4, "B"), 5.0,
                Pair.of(5, "A"), 367.4, Pair.of(5, "B"), 1.5));            
        
        mx.rows().get(4).put("A", -4243.25);
        
        Assert.assertEquals(mx.columns().get("A").get(4), mx.rows().get(4).get("A"));
        Assert.assertEquals(mx.rows().get(4).get("A"), -4243.25);
        
        mx.columns().get("B").put(3, 35.56);
        
        Assert.assertEquals(mx.columns().get("B").get(3), mx.rows().get(3).get("B"));
        Assert.assertEquals(mx.columns().get("B").get(3), 3, 35.56);
    }
    
    @Test
    public void matrixInit() {     
        Assert.assertEquals(tf.columns().size(), 2);
        Assert.assertEquals(tf.rows().size(), 5);
        Assert.assertEquals(tf.size(), 10);
    }
}
