/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package money.portosim;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author yarro
 */
public interface Backtest {
    
    static Backtest create() {
        return new BacktestBuilder();
    }
    
    static Backtest withStrategy(Strategy strategy) {
        return new BacktestBuilder(strategy);
    }
    
    static Backtest forPrices(Map<Date, Map<String, Double>> prices) {
        return new BacktestBuilder(prices);
    }
    
    static Backtest rebalanceEvery(ChronoUnit rebalancePeriod) {
        return new BacktestBuilder(rebalancePeriod);
    }
    
    default Result run(Strategy strategy) {
        setStrategy(strategy);
        return run();
    }
    
    default Result run(Map<Date, Map<String, Double>> prices) {
        setPrices(prices);
        return run();
    }
    
    default Result run(ChronoUnit rebalancePeriod) {
        setRebalancePeriod(rebalancePeriod);
        return run();
    }
    
    Backtest setPrices(Map<Date, Map<String, Double>> prices);
    
    Backtest setRebalancePeriod(ChronoUnit rebalancePeriod);
    
    Backtest setStrategy(Strategy strategy);
    
    Backtest setStart(Date startDate);
    
    Backtest setEnd(Date endDate);
    
    default Backtest setPeriod(Date startDate, Date endDate) {
        return setStart(startDate).setEnd(endDate);
    }
    
    Result getResult();
    
    Result run();
}
