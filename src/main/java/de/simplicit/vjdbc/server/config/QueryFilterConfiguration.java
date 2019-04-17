// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package de.simplicit.vjdbc.server.config;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

public class QueryFilterConfiguration {
    private static Logger _logger = Logger.getLogger(QueryFilterConfiguration.class.getName());
    private List _filters = new ArrayList();
    private Perl5Matcher _matcher = new Perl5Matcher();

    private static PatternCompiler s_patternCompiler = new Perl5Compiler();

    private static class Filter {
        Filter(boolean isDenyFilter, String regExp, Pattern pattern, boolean containsType) {
            _isDenyFilter = isDenyFilter;
            _regExp = regExp;
            _pattern = pattern;
            _containsType = containsType;
        }

        boolean _isDenyFilter;
        String _regExp;
        Pattern _pattern;
        boolean _containsType;
    }

    public void addDenyEntry(String regexp, String type) throws ConfigurationException {
        addEntry(true, regexp, type);
    }

    public void addAllowEntry(String regexp, String type) throws ConfigurationException {
        addEntry(false, regexp, type);
    }

    private void addEntry(boolean isDenyFilter, String regexp, String type) throws ConfigurationException {
        try {
            Pattern pattern = s_patternCompiler.compile(regexp, Perl5Compiler.CASE_INSENSITIVE_MASK);
            _filters.add(new Filter(isDenyFilter, regexp, pattern, type != null && type.equals("contains")));
        } catch (MalformedPatternException e) {
            throw new ConfigurationException("Malformed RegEx-Pattern", e);
        }
    }

    public void checkAgainstFilters(String sql) throws SQLException {
        if(!_filters.isEmpty()) {
            for(int i = 0, n = _filters.size(); i < n; ++i) {
                Filter filter = (Filter) _filters.get(i);
                boolean matched = filter._containsType ? _matcher.contains(sql, filter._pattern) : _matcher.matches(sql,
                        filter._pattern);
    
                if(matched) {
                    if(filter._isDenyFilter) {
                        String msg = "SQL [" + sql + "] is denied due to Deny-Filter [" + filter._regExp + "]";
                        _logger.warning(msg);
                        throw new SQLException(msg);
                    } else {
                        if(_logger.isLoggable(Level.FINE)) {
                            String msg = "SQL [" + sql + "] is allowed due to Allow-Filter [" + filter._regExp + "]";
                            _logger.fine(msg);
                        }
                        return;
                    }
                }
            }
            
            String msg = "SQL [" + sql + "] didn't match any query filter and won't be executed";
            _logger.severe(msg);
            throw new SQLException(msg);
        }
    }

    void log() {
        _logger.info("  Query Filter-Configuration:");

        for(Iterator it = _filters.iterator(); it.hasNext();) {
            Filter filter = (Filter) it.next();
            if(filter._isDenyFilter) {
                _logger.info("    Deny  : [" + filter._regExp + "]");
            } else {
                _logger.info("    Allow : [" + filter._regExp + "]");
            }
        }
    }
}
