//VJDBC - Virtual JDBC
//Written by Michael Link
//Website: http://vjdbc.sourceforge.net

package de.simplicit.vjdbc.server.config;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * This class inherits from the GenericObjectPool and provides a little bit
 * of logging when eviction happens.
 * @author Mike
 */
public class LoggingGenericObjectPool extends GenericObjectPool {
    private static Logger _logger = Logger.getLogger(LoggingGenericObjectPool.class.getName());
    
    private String _idOfConnection;

    public LoggingGenericObjectPool(String nameOfConnection) {
        super(null);
        _idOfConnection = nameOfConnection;
    }
    
    public LoggingGenericObjectPool(String nameOfConnection, GenericObjectPool.Config config) {
        super(null, config);
        _idOfConnection = nameOfConnection;
    }
        
    public synchronized void evict() throws Exception {
        super.evict();
        if(_logger.isLoggable(Level.FINE)) {
            _logger.fine("DBCP-Evictor: number of idle connections in '" + _idOfConnection + "' = " + getNumIdle());
        }
    }
}
