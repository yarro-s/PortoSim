/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim.strategies;

import java.util.Date;
import money.portosim.AbstractStrategy;
import money.portosim.Portfolio;
import money.portosim.Strategy;
import money.portosim.containers.Quote;

/**
 *
 * @author yarro
 */
public abstract class StrategyChain extends AbstractStrategy {
    private Strategy nextStrategy;
    
    public Portfolio runNextStrategy(Date date, Quote prices) {
       return nextStrategy.makePortfolio(date, prices);
    }

    public AbstractStrategy chainTo(Strategy nextStrategy) {
        this.nextStrategy = nextStrategy;
        return this;
    }
}
