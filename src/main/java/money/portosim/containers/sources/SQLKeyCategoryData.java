package money.portosim.containers.sources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yarro
 */
public abstract class SQLKeyCategoryData<K, V> extends MapParser<K, V> {

    private final Connection con;
    private final Set<K> ks = new HashSet<>();
    
    private final String table, keyColumn, categoryColumn, dataColumn;  
    
    public SQLKeyCategoryData(String connectionString, String table, String keyColumn, 
            String categoryColumn, String dataColumn) throws SQLException {
        
        con = DriverManager.getConnection(connectionString);  
        
        this.table = table;
        this.keyColumn = keyColumn;
        this.categoryColumn = categoryColumn;
        this.dataColumn = dataColumn;
    } 
    
    protected abstract K toKey(String rawKey);
    
    protected abstract String toRawKey(K key);

    protected abstract V toValue(Map<String, Double> mappedRecord);

    @Override
    public Set<K> keySet() {
        try {
            var statement = (Statement) con.createStatement();
            String sql = "SELECT DISTINCT " + keyColumn + " FROM " + table;
            
            ResultSet resultSet = statement.executeQuery(sql);
            
            while (resultSet.next()) {
                ks.add(toKey(resultSet.getString(keyColumn)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLKeyCategoryData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ks;
    }

    @Override
    public V get(Object key) {
        Map<String, Double> hm = null;
        try {
            var statement = (Statement) con.createStatement();
            var rawKey = toRawKey((K) key);
            
            String sql = "SELECT " + categoryColumn + ", " + dataColumn + " FROM " + table 
                    + " WHERE " + keyColumn + " = '" + rawKey + "'";
            
            ResultSet resultSet = statement.executeQuery(sql);
            
            hm = new HashMap<>();
            
            while (resultSet.next()) {
                var t = resultSet.getString(categoryColumn);
                var v = resultSet.getDouble(dataColumn);
                hm.put(t, v);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLKeyCategoryData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return toValue(hm);
    } 
}
