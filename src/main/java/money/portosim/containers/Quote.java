/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim.containers;

import money.portosim.containers.core.AlgebraicMap;
import java.util.Map;
import money.portosim.containers.core.AlgebraicMap;

/**
 *
 * @author yarro
 */
public class Quote extends NumericMap<String> {

    public Quote() { 
        super();
    }
    
    public Quote(Map<String, Double> m) {
        super(m);
    }   
    
    @Override
    public Quote add(AlgebraicMap<String, Double> arg) {
        return new Quote(super.add(arg));
    }

    @Override
    public Quote mult(AlgebraicMap<String, Double> arg) {
        return new Quote(super.mult(arg));
    }

    @Override
    public Quote div(AlgebraicMap<String, Double> arg) {
        return new Quote(super.div(arg));
    }
    
    @Override
    public Quote sub(AlgebraicMap<String, Double> arg) {
        return new Quote(super.sub(arg));
    }
}
