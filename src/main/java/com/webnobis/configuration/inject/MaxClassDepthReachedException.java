package com.webnobis.configuration.inject;

/**
 * The max depth of recursion is reached.
 * @author steffen
 *
 */
public class MaxClassDepthReachedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param maxDepth max depth
	 */
	public MaxClassDepthReachedException(int maxDepth) {
		super("max class depth reached: " + maxDepth);
	}

}
