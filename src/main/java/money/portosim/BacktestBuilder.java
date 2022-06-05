/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim;

import java.time.temporal.ChronoUnit;
import money.portosim.containers.QuoteSeries;
import money.portosim.strategies.TimedStrategy;

/**
 *
 * @author yarro
 */
public class BacktestBuilder {
    private AbstractStrategy strategy;
    private TimedStrategy timedStrategy; 
    private Backtest backtest;
    private QuoteSeries prices;

    public BacktestBuilder() { }
    
    public BacktestBuilder(AbstractStrategy strategy) {
        setStrategy(strategy);
    }    

    public BacktestBuilder(QuoteSeries prices) {
        setPrices(prices);
    }

    public BacktestBuilder(ChronoUnit rebalancePeriod) {
        setRebalancePeriod(rebalancePeriod);
    }
    
    public Result run(AbstractStrategy strategy) {
        setStrategy(strategy);
        return run();
    }
    
    public Result run(QuoteSeries prices) {
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
    
    public BacktestBuilder setPrices(QuoteSeries prices) {
        this.prices = prices;
        return this;
    }
    
    public BacktestBuilder setRebalancePeriod(ChronoUnit rebalancePeriod) {
        timedStrategy = new TimedStrategy(rebalancePeriod);
        return this;
    }
    
    public BacktestBuilder setStrategy(AbstractStrategy strategy) {
        this.strategy = strategy;
        return this;
    }

    public AbstractStrategy getStrategy() {
        return timedStrategy == null ? strategy : timedStrategy.chainTo(strategy);
    }

    public Backtest getBacktest() {
        return backtest;
    }
}
