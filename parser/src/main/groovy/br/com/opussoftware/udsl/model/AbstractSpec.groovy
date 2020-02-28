/**
 * 
 */
package br.com.opussoftware.udsl.model

import java.util.function.Consumer

/**
 * Base class used for all xxxSpec
 * @author Philippe
 *
 */
abstract class AbstractSpec {
	
	String name;
	String[] tags = []
	
	abstract List<AbstractSpec> getChildren();
	
	/**
	 * Traverse all children of this element (depth-first), calling
	 * the given visitor for each one. After that, calls the visitor a last time
	 * passing this spec
	 * @param visitor A java.util.function.Consumer instance that will be called with a single argument corresponding to
	 *                the current item
	 */
	public void traverse(Consumer<AbstractSpec> visitor ) {
		def children = getChildren()
		children.each { child -> 
			child.traverse(visitor)
		}
		
		visitor.accept(this)
	}

	/**
	 * Traverse all children of this element (depth-first), calling
	 * the given visitor for each one. After that, calls the visitor a last time
	 * passing this spec
	 * @param visitor a Closure that will be called with a single argument correspondig to
	 *                the current item
	 */
	public void traverse(Closure visitor ) {
		def children = getChildren()
		children.each { child ->
			child.traverse(visitor)
		}
		
		visitor(this)
	}

}
