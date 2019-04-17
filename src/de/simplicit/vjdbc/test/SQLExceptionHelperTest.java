// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package de.simplicit.vjdbc.test;

import java.sql.SQLException;

import de.simplicit.vjdbc.util.SQLExceptionHelper;

import junit.framework.TestCase;

public class SQLExceptionHelperTest extends TestCase {
    public static class DerivedSQLException extends SQLException
    {
        private static final long serialVersionUID = -938598334895735877L;
    }
    
    public void testDerivedSQLException() throws Exception
    {
        SQLException derivedEx = new DerivedSQLException();
        SQLException originalEx = new SQLException();
        Exception otherEx = new UnsupportedOperationException("Bla");
        
        SQLException wex1 = SQLExceptionHelper.wrap(derivedEx);
        assertNotSame(derivedEx, wex1);
        
        SQLException wex2 = SQLExceptionHelper.wrap(originalEx);
        assertSame(originalEx, wex2);
        
        SQLException wex3 = SQLExceptionHelper.wrap(otherEx);
        assertNotSame(otherEx, wex3);
    }
}
