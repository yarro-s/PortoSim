/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package money.portosim.containers.sources;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import money.portosim.containers.core.Series;

/**
 *
 * @author yarro
 */
public class QuoteSeriesSQLSource extends SQLKeyCategoryData<Date, Map<String, Double>> {

    public QuoteSeriesSQLSource(String connectionString) throws SQLException {
        
        this(connectionString, "quotes", "date", "ticker", "price");
    }
    
    public QuoteSeriesSQLSource(String connectionString, String table, String keyColumn, 
            String categoryColumn, String dataColumn) throws SQLException {
        
        super(connectionString, table, keyColumn, categoryColumn, dataColumn);
    }

    @Override
    protected Date toKey(String rawKey) {
        return Series.isoStringToDate(rawKey);
    }

    @Override
    protected String toRawKey(Date key) {
        return Series.dateToIsoString(key);
    }

    @Override
    protected Map<String, Double> toValue(Map<String, Double> mappedRecord) {
        return mappedRecord;
    }
    
}
