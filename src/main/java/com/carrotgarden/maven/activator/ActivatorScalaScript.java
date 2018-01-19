package com.carrotgarden.maven.activator;

import org.codehaus.plexus.component.annotations.Component;

/**
 * Activate project profile based on Scala property expression.
 */
@Component( //
		role = ActivatorAny.class, //
		hint = Activator.SCALASCRIPT //
)
public class ActivatorScalaScript extends ActivatorBase {

	@Override
	public String activatorName() {
		return Activator.SCALASCRIPT;
	}

	/**
	 * @see scala.tools.nsc.interpreter.Scripted
	 */
	@Override
	public String activatorEngine() {
		return "scala";
	}

}
