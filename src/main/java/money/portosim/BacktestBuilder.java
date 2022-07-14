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
class BacktestBuilder implements Backtest {
    private Strategy strategy;
    private TimedStrategy timedStrategy; 
    private BacktestRun backtest;
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
     
    @Override
    public Result run() {
        backtest = getStrategy() == null || prices == null 
                ? null : new BacktestRun(getStrategy(), prices);
        return backtest.run();
    }
    
    @Override
    public Backtest setPrices(Map<Date, Map<String, Double>> prices) {
        this.prices = prices;
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
