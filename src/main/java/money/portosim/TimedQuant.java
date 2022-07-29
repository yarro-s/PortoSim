/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package money.portosim;

import java.time.temporal.ChronoUnit;
import java.util.Date;


/**
 *
 * @author yarro
 */
public abstract class TimedQuant implements Quantifiable<Date> {

    private ChronoUnit timeFrame = ChronoUnit.DAYS;
    private double riskFreeRate = 0.0;
    
    public TimedQuant setTimeFrame(ChronoUnit timeFrame) {
        this.timeFrame = timeFrame;
        return this;
    }
    
    public ChronoUnit getTimeFrame() {
        return timeFrame;
    }
    
    public TimedQuant setRiskFreeRate(double riskFreeRate) {
        this.riskFreeRate = riskFreeRate;
        return this;
    }
    
    public double getRiskFreeRate() {
        return riskFreeRate;
    }
}
