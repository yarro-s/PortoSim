package money.portosim;

import java.util.Date;
import money.portosim.containers.numeric.NumFrame;

/**
 *
 * @author yarrik
 */
public interface Strategy {
    Portfolio makePortfolio(Date date, NumFrame<String> prices);
}
