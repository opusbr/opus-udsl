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
	 * <p>Modo de opera��o do canal. Os modos de opera��o definidos inicialmente s�o
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
