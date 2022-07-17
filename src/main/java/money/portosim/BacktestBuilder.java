/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import money.portosim.strategies.TimedStrategy;

/**
 *
 * @author yarro
 */
class BacktestBuilder implements Backtest {
    
    private Strategy strategy;
    private TimedStrategy timedStrategy; 
    private BacktestRun backtest;
    private NavigableMap<Date, Map<String, Double>> prices;
    private Date startDate, endDate;

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

    @Override
    public Backtest setStart(Date startDate) {
        this.startDate = startDate;
        return this;
    }

    @Override
    public Backtest setEnd(Date endDate) {
        this.endDate = endDate;
        return this;
    }
     
    @Override
    public Result run() {
        if (startDate == null) startDate = prices.firstKey();
        if (endDate == null) endDate = prices.lastKey();
        
        var strategy = getStrategy();
        if (prices != null && strategy != null) {
            var prices4Run = prices.subMap(startDate, true, endDate, true);
                
            backtest = new BacktestRun(strategy, prices4Run);
            return backtest.run();
        } else {
            return null;
        }
    }
    
    @Override
    public Backtest setPrices(Map<Date, Map<String, Double>> prices) {
        this.prices = new TreeMap<>(prices);
        return this;
    }
    
    @Override
    public Backtest setRebalancePeriod(ChronoUnit rebalancePeriod) {
        timedStrategy = new TimedStrategy(rebalancePeriod);
        return this;
    }
    
    @Override
    public Backtest setStrategy(Strategy strategy) {
        this.strategy = strategy;
        return this;
    }

    private Strategy getStrategy() {
        return timedStrategy == null ? strategy : timedStrategy.chainTo(strategy);
    }

    @Override
    public Result getResult() {
        return backtest.getResult();
    }

    
}
