/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim.containers.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Date;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;

/**
 *
 * @author yarro
 */
public class Series<V> extends AbstractMap<Date, V> implements OrderedMap<Date, V> {
    
    private final NavigableMap<Date, V> nm;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    
    @Override
    public Series<V> rolling(int n, Function<SortedMap<Date, V>, V> f) {
        return new Series<>(OrderedMap.super.rolling(n, f)); 
    }
    
    public V put(String isoDate, V val) {
        Date date = isoStringToDate(isoDate);
        if (date != null)
            return put(date, val);
        return null;
    }

    public V get(String isoDate) {  
        Date date = isoStringToDate(isoDate);
        if (date != null)
            return nm.get(date);
        return null;
    }  
    
    public Series<V> from(String isoDate) {
        Date startDate = isoStringToDate(isoDate);
        if (startDate == null) 
            startDate = firstKey();
        
        return new Series<>(subMap(startDate, true, lastKey(), true)); 
    }
    
    public Series<V> to(String isoDate) {
        Date endDate = isoStringToDate(isoDate);
        if (endDate == null) 
            endDate = lastKey();
        
        return new Series<>(subMap(firstKey(), true, endDate, true)); 
    }

    public Series(Map<Date, V> m) {
        this.nm = new TreeMap<>(m);
    }
    
    public Series() {
        this.nm = new TreeMap<>();
    }

    @Override
    public Set<Entry<Date, V>> entrySet() {
        return nm.entrySet();
    }
    
    @Override
    public V put(Date date, V val) {
        return nm.put(date, val); 
    }

    @Override
    public NavigableMap<Date, V> ordered() {
        return nm;
    }

    public static Date isoStringToDate(String isoDate) {
        Date date = null;
        try {
            date = formatter.parse(isoDate);
        } catch (ParseException ex) {}
        
        return date;
    }  
    
    public static String dateToIsoString(Date date) {
        return formatter.format(date);
    }  
}
