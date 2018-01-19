package com.carrotgarden.maven.activator;

import org.codehaus.plexus.component.annotations.Component;

/**
 * Activate project profile based on MVFLEX property expression.
 */
@Component( //
		role = ActivatorAny.class, //
		hint = Activator.MVELSCRIPT //
)
public class ActivatorMvelScript extends ActivatorBase {

	@Override
	public String activatorName() {
		return Activator.MVELSCRIPT;
	}

	/**
	 * @see org.mvel2.jsr223.MvelScriptEngineFactory
	 */
	@Override
	public String activatorEngine() {
		return "mvel";
	}

}
