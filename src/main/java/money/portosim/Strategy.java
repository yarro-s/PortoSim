package money.portosim;

import money.portosim.containers.PriceMap;

import java.util.Date;

public abstract class Strategy {
    private Result result;

    void setResult(Result result) {
        this.result = result;
    }

    public abstract Portfolio apply(Date date, PriceMap prices);
}
