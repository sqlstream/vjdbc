// VJDBC - Virtual JDBC
// Written by Hunter Payne
// Website: http://vjdbc.sourceforge.net

package de.simplicit.vjdbc;

import de.simplicit.vjdbc.serial.UIDEx;

/**
 * An interface for a JDBC object that can be reconstructed by the client from
 * a network proxy.  The client must override the client code to use the
 * proxy to recreate the original object including the UIDEx registration
 * object.
 */
public interface ProxiedObject {

    public UIDEx getUID();
}
