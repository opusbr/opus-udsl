/**
 * 
 */
package br.com.opussoftware.udsl.model

import groovy.transform.Canonical

/**
 * @author Philippe
 *
 */
@Canonical
class MessageChannelSpec extends AbstractSpec {
	
	String role;
	
	/**
	 * <p>Modo de operação do canal. Os modos de operação definidos inicialmente são
	 * os seguintes:
	 * </p>
	 * 
	 * <ul>
	 * 	<li>p2p - "Point To Point"</li>
	 *  <li>broadcast - "Broadcast" </li>
	 * </ul>
	 * 
	 */
	String mode = "p2p"; 
	

}
