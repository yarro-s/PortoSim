# PortoSim - a lightweight backtesting framework for Java
PortoSim allows you to backtest asset portfolios with pre-built or custom strategies

## Quickstart

Find the full sample [here](https://github.com/yarro-s/PortoSim/blob/master/samples/PortoSimSampleApp.zip).

First load prices from a CSV file

```java
var priceReader = new CSVQuoteSeriesReader(new FileReader(sp500GoldMonthlyCSV));
var prices = priceReader.readPrices();
```

Then define a fixed allocation 70% stocks / 30% gold portfolio
```java
var myStrategy = new FixedAllocation(Map.of("SP500TR", 0.7, "GOLD", 0.3));
```

Build a backtest with a rebalancing period of one year
```java
var result = new BacktestBuilder(myStrategy)
        .setRebalancePeriod(ChronoUnit.YEARS)   // rebalance every year
        .run(prices);    // test on the historic prices
```

Get the total return (120.26%)
```java
System.out.println("Total return is " + result.totalReturn().orElse(0)); 
```

## Features

A TreeMap-based container with some additional features is used to store price data as maps

```java
var priceReader = new CSVQuoteSeriesReader(new FileReader(spyGoldDailyCSV));
var prices = priceReader.readPrices();
        
var priceSlice = prices.from("2015-01-02").to("2018-11-30");   // also accepts Date

System.out.println("First: " + priceSlice.ordered().firstEntry());  // 2015-01-02={SPY=205.4, GLD=114.1}
System.out.println("Last: " + priceSlice.ordered().lastEntry());    // 2018-11-30={SPY=275.7, GLD=115.5}
```

