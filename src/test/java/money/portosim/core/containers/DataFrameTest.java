/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/EmptyTestNGTest.java to edit this template
 */
package money.portosim.core.containers;

import java.util.Map;
import money.portosim.containers.core.DataMatrix;
import money.portosim.containers.numeric.NumDataMatrix;
import money.portosim.containers.core.Pair;
import money.portosim.containers.core.DataRecord;
import money.portosim.containers.core.Frame;
import money.portosim.containers.core.Matrix;
import money.portosim.containers.core.OrderedFrame;
import org.testng.Assert;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author yarro
 */
public class DataFrameTest {
    
    final Frame<Pair<Integer, String>, Double> tf;

    public DataFrameTest() {
        this.tf = new DataMatrix<>();
        
        tf.put(Pair.of(1, "A"), 100.5); tf.put(Pair.of(1, "B"), 6.2);
        tf.put(Pair.of(2, "A"), 245.0); tf.put(Pair.of(2, "B"), 8.1);
        tf.put(Pair.of(3, "A"), 307.4); tf.put(Pair.of(3, "B"), 2.8);
        tf.put(Pair.of(4, "A"), 843.2); tf.put(Pair.of(4, "B"), 5.0);
        tf.put(Pair.of(5, "A"), 367.4); tf.put(Pair.of(5, "B"), 1.5);
    }
    
    @Test
    public void sumAllFrame() {
        var nf = new NumDataMatrix<>(tf);
        
        System.out.println("\n >>>>" + nf.sum());
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
        
        System.out.println("\n >>>>" + nf.add(otherNM));
    }
       
    @Test
    public void addVal() {
        var nm = new NumDataMatrix<>(tf);
        System.out.println("\n >>>>" + nm.add(50000.6));
    }
    
    @Test
    public void matrixInit() {
        final Matrix<Integer, String, Double> mx = new DataMatrix<>(Map.of(
                Pair.of(1, "A"), 100.5, Pair.of(1, "B"), 6.2,
                Pair.of(2, "A"), 245.0, Pair.of(2, "B"), 8.1,
                Pair.of(3, "A"), 307.4, Pair.of(3, "B"), 2.8,
                Pair.of(4, "A"), 843.2, Pair.of(4, "B"), 5.0,
                Pair.of(5, "A"), 367.4, Pair.of(5, "B"), 1.5));     
        
//        mx.put(Pair.of(1000, "APMP"), 9087435.2);
//        mx.columns().get("A").put(50424, 12368534.64);
        
        
        mx.rows().get(4).put("A", -4243.25);
        
//        System.out.println("\n>>>>>> " + mx);
    }
    
    @Test
    public void dataFrameInit() {

//        System.out.println("\n>>>>> " + tf.get(Pair.of(3, "A")));
    }
}
