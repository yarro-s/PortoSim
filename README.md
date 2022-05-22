# PortoSim - a lightweight backtesting framework for Java
PortoSim allows you to backtest asset portfolios with pre-built or custom strategies

## Quickstart

Find the full sample [here](https://github.com/yarro-s/PortoSim/blob/master/samples/PortoSimSampleApp.zip).

First load prices from a CSV file

```java
var priceReader = new CSVPriceSeriesReader(new FileReader("sp500_gold_3yr_monthly.csv"));
var prices = priceReader.readPrices();
```

Then define a fixed allocation 70% stocks / 30% gold portfolio
```java
var myStrategy = new FixedAllocation(Map.of("SP500TR", 0.7, "GOLD", 0.3));
```

Build a backtest with a rebalancing period of one year
```java
var result = new BacktestBuilder(myStrategy)
        .setRebalancePeriod(ChronoUnit.YEARS)   
        .run(prices);
```

Get the total return (120.26%)
```java
System.out.println("Total return is " + result.totalReturn().orElse(0)); 
```

