/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import money.portosim.strategies.TimedStrategy;

/**
 *
 * @author yarro
 */
public class BacktestBuilder {
    private Strategy strategy;
    private TimedStrategy timedStrategy; 
    private Backtest backtest;
    private Map<Date, Map<String, Double>> prices;

    public BacktestBuilder() { }
    
    public BacktestBuilder(Strategy strategy) {
        setStrategy(strategy);
    }    

    public BacktestBuilder(Map<Date, Map<String, Double>> prices) {
        setPrices(prices);
    }

    public BacktestBuilder(ChronoUnit rebalancePeriod) {
        setRebalancePeriod(rebalancePeriod);
    }
    
    public Result run(Strategy strategy) {
        setStrategy(strategy);
        return run();
    }
    
    public Result run(Map<Date, Map<String, Double>> prices) {
        setPrices(prices);
        return run();
    }
    
    public Result run(ChronoUnit rebalancePeriod) {
        setRebalancePeriod(rebalancePeriod);
        return run();
    }
    
    public Result run() {
        backtest = getStrategy() == null || prices == null 
                ? null : new Backtest(getStrategy(), prices);
        return backtest.run();
    }
    
    public BacktestBuilder setPrices(Map<Date, Map<String, Double>> prices) {
        this.prices = prices;
        return this;
    }
    
    public BacktestBuilder setRebalancePeriod(ChronoUnit rebalancePeriod) {
        timedStrategy = new TimedStrategy(rebalancePeriod);
        return this;
    }
    
    public BacktestBuilder setStrategy(Strategy strategy) {
        this.strategy = strategy;
        return this;
    }

    public Strategy getStrategy() {
        return timedStrategy == null ? strategy : timedStrategy.chainTo(strategy);
    }

    public Backtest getBacktest() {
        return backtest;
    }
}
