package money.portosim;

import java.util.Date;
import money.portosim.containers.PriceMap;

/**
 *
 * @author yarrik
 */
public interface Strategy {
    Portfolio makePortfolio(Date date, PriceMap prices);
}
