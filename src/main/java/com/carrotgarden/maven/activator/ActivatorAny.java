package com.carrotgarden.maven.activator;

/**
 * Shared interface of extension profile activators.
 */
public interface ActivatorAny {

	/**
	 * Script engine name required by this activator.
	 */
	String activatorEngine();

	/**
	 * Magic property name managed by this activator.
	 */
	String activatorName();

	/**
	 * Logger identity for this activator.
	 */
	default String prefix() {
		return "Activator " + activatorName();
	}

}
