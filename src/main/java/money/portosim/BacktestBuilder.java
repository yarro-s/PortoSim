/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim;

import java.time.temporal.ChronoUnit;
import money.portosim.containers.PriceSeries;
import money.portosim.strategies.TimedStrategy;

/**
 *
 * @author yarro
 */
public class BacktestBuilder {
    private AbstractStrategy strategy;
    private TimedStrategy timedStrategy; 
    private Backtest backtest;
    private PriceSeries prices;
    
    public Result run() {
        backtest = getStrategy() == null || prices == null 
                ? null : new Backtest(getStrategy(), prices);
        return backtest.run();
    }
    
    public BacktestBuilder setPrices(PriceSeries prices) {
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