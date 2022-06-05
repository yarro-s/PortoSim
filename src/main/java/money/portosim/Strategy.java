package money.portosim;

import java.util.Date;
import money.portosim.containers.Quote;

/**
 *
 * @author yarrik
 */
public interface Strategy {
    Portfolio makePortfolio(Date date, Quote prices);
}
