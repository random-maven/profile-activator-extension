package com.carrotgarden.maven.activator;

import org.codehaus.plexus.component.annotations.Component;

/**
 * Activate project profile based on Groovy property expression.
 */
@Component( //
		role = ActivatorAny.class, //
		hint = Activator.GROOVYSCRIPT //
)
public class ActivatorGroovyScript extends ActivatorBase {

	@Override
	public String activatorName() {
		return Activator.GROOVYSCRIPT;
	}

	/**
	 * @see org.codehaus.groovy.jsr223.GroovyScriptEngineFactory
	 */
	@Override
	public String activatorEngine() {
		return "groovy";
	}

}
