package com.carrotgarden.maven.activator;

import org.codehaus.plexus.component.annotations.Component;

/**
 * Activate project profile based on JavaScript property expression.
 */
@Component( //
		role = ActivatorAny.class, //
		hint = Activator.JAVASCRIPT //
)
public class ActivatorJavaScript extends ActivatorBase {

	@Override
	public String activatorName() {
		return Activator.JAVASCRIPT;
	}

	/**
	 * @see com.sun.phobos.script.javascript.RhinoScriptEngineFactory
	 */
	@Override
	public String activatorEngine() {
		return "rhino";
	}

}
