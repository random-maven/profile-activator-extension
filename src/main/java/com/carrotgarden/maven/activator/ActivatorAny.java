package com.carrotgarden.maven.activator;

import static com.carrotgarden.maven.activator.SupportFunction.*;

import javax.script.Bindings;
import javax.script.ScriptEngine;

import org.apache.maven.model.Profile;
import org.apache.maven.model.building.ModelProblemCollector;
import org.apache.maven.model.profile.ProfileActivationContext;
import org.codehaus.plexus.logging.Logger;

/**
 * Shared interface for custom profile activators.
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
	 * Evaluate magic property script expression.
	 */
	default EvaluationResult evaluateResult( //
			Profile profile, //
			ProfileActivationContext context, //
			ModelProblemCollector problems //
	) {
		try {
			ScriptEngine engine = scriptEngine(activatorEngine());
			if (engine == null) {
				Exception error = new Exception("Missing Engine");
				reportProblem(error.getMessage(), error, profile, context, problems);
				logger().error("Engine factory:\n" + renderEngineList());
				return EvaluationResult.failure(error);
			}
			String script = propertyValue(profile);
			if (script == null) {
				Exception error = new Exception("Missing Script");
				reportProblem(error.getMessage(), error, profile, context, problems);
				return EvaluationResult.failure(error);
			}
			Bindings bindings = engine.createBindings();
			bindings.putAll(variablesFrom(context));
			if (logger().isDebugEnabled()) {
				logger().debug(String.format("%s bindings:\n%s", prefix(), renderBindings(bindings)));
			}
			Object result = engine.eval(script, bindings);
			if (logger().isDebugEnabled()) {
				logger().debug(String.format("%s result='%s'", prefix(), result));
			}
			boolean value = Boolean.parseBoolean("" + result);
			return EvaluationResult.success(value);
		} catch (Exception error) {
			String script = propertyValue(profile);
			String message = "Evaluation failure:\n" + script + "\n";
			reportProblem(message, error, profile, context, problems);
			return EvaluationResult.failure(error);
		}
	}

	/**
	 * Detect if profile should be activated as a result of script evaluation.
	 */
	default boolean hasActivationAction( //
			Profile profile, //
			ProfileActivationContext context, //
			ModelProblemCollector problems //
	) {
		if (!hasActivationProperty(profile, context, problems)) {
			return false;
		}
		EvaluationResult result = evaluateResult(profile, context, problems);
		String report = String.format( //
				"%s project='%s' profile='%s' result='%s'", //
				prefix(), projectName(context), profileId(profile), result.render());
		logger().info(report);
		if (result.valid) {
			return result.value;
		} else {
			return false;
		}
	}

	/**
	 * Detect if profile contains magic property name managed by this activator.
	 */
	default boolean hasActivationProperty( //
			Profile profile, //
			ProfileActivationContext context, //
			ModelProblemCollector problems //
	) {
		return hasPropertyName(profile) && activatorName().equals(propertyName(profile));
	}

	/**
	 * Logger injected by Maven runtime.
	 */
	Logger logger();

	/**
	 * Logger identity for this activator.
	 */
	default String prefix() {
		return "Activator " + activatorName();
	}

	/**
	 * Report titled activator problem.
	 */
	default void reportProblem( //
			String title, //
			Exception error, //
			Profile profile, //
			ProfileActivationContext context, //
			ModelProblemCollector problems //
	) {
		String message = String.format( //
				"%s: project='%s' profile='%s' engine='%s'", //
				title, projectName(context), profileId(profile), activatorEngine());
		registerProblem(message, error, profile, problems);
	}

}
