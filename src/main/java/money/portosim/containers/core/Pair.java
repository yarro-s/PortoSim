/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim.containers.core;

/**
 *
 * @author yarro
 */
public class Pair<L, R> {
    private final L l;
    private final R r;

    public Pair(L l, R r) {
        this.l = l;
        this.r = r;
    }  
        
    public L left() {
        return l;
    }
    
    public R right() {
        return r;
    }
    
    public static <L, R> Pair<L, R> of(L leftValue, R rightValue) {
        return new Pair<>(leftValue, rightValue);
    }
    
    public static <L, R> Pair<L, R> of(L leftValue) {
        return new Pair<>(leftValue, (R) null);
    }

    @Override
    public String toString() {
        return "[" + l + " " + r + "]";
    }

    @Override
    public boolean equals(Object obj) {
        return l.equals(((Pair) obj).l) && r.equals(((Pair) obj).r);
    }

    @Override
    public int hashCode() {
        return l.hashCode() ^ r.hashCode();
    }
 }
