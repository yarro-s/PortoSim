/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim.containers;

import money.portosim.containers.core.Series;
import money.portosim.containers.core.AlgebraicMap;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Date;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import money.portosim.containers.core.AlgebraicMap;
import money.portosim.containers.core.Series;

/**
 *
 * @author yarro
 */
public class QuoteSeries extends Series<Quote> implements AlgebraicMap<Date, Quote> {

    public QuoteSeries() { 
        super();
    }
    
    public QuoteSeries(Map<Date, Quote> m) {
        super(m);
    }
    
    @Override
    public QuoteSeries add(AlgebraicMap<Date, Quote> arg) {
        return new QuoteSeries(AlgebraicMap.super.add(arg));
    }

    @Override
    public QuoteSeries mult(AlgebraicMap<Date, Quote> arg) {
        return new QuoteSeries(AlgebraicMap.super.mult(arg));
    }   
    
    @Override
    public QuoteSeries div(AlgebraicMap<Date, Quote> arg) {
        return new QuoteSeries(AlgebraicMap.super.div(arg));
    }
    
    @Override
    public QuoteSeries sub(AlgebraicMap<Date, Quote> arg) {
        return new QuoteSeries(AlgebraicMap.super.sub(arg));
    }
          
    @Override
    public Quote add(Quote arg1, Quote arg2) {
        return arg1.add(arg2);
    }

    @Override
    public Quote mult(Quote arg1, Quote arg2) {
        return arg1.mult(arg2);
    }

    @Override
    public Quote sub(Quote arg1, Quote arg2) {
        return arg1.sub(arg2);
    }

    @Override
    public Quote div(Quote arg1, Quote arg2) {
        return arg1.div(arg2);
    }
}
