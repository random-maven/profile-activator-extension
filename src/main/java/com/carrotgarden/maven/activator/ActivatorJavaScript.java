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
	 * @see jdk.nashorn.api.scripting.NashornScriptEngineFactory
	 */
	@Override
	public String activatorEngine() {
		return "javascript";
	}

}
