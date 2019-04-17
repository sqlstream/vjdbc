// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package de.simplicit.vjdbc.server.config;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

public class NamedQueryConfiguration {
    private static Logger _logger = Logger.getLogger(NamedQueryConfiguration.class.getName());
    private Map _queryMap = new HashMap();

    public Map getQueryMap() {
        return _queryMap;
    }

    public void addEntry(String id, String sql) {
        _queryMap.put(id, sql);
    }

    public String getSqlForId(String id) throws SQLException {
        String result = (String)_queryMap.get(id);
        if(result != null) {
            return result;
        }
        else {
            String msg = "Named-Query for key '" + id + "' not found";
            _logger.severe(msg);
            throw new SQLException(msg);
        }
    }

    void log() {
        _logger.info("  Named Query-Configuration:");

        for (Iterator it = _queryMap.keySet().iterator(); it.hasNext();) {
            String id = (String) it.next();
            _logger.info("    [" + id + "] = [" + _queryMap.get(id) + "]");
        }
    }
}
