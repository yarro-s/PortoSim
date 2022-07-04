package money.portosim;

import java.util.Date;
import java.util.Map;

/**
 *
 * @author yarrik
 */
public interface Strategy {
    Portfolio makePortfolio(Date date, Map<String, Double> prices);
}
