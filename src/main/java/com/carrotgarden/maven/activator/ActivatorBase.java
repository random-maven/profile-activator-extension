package com.carrotgarden.maven.activator;

import static com.carrotgarden.maven.activator.SupportFunction.*;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.script.Bindings;
import javax.script.ScriptEngine;

import org.apache.maven.model.Model;
import org.apache.maven.model.Profile;
import org.apache.maven.model.building.ModelBuilder;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.model.building.ModelBuildingResult;
import org.apache.maven.model.building.ModelProblemCollector;
import org.apache.maven.model.profile.ProfileActivationContext;
import org.apache.maven.model.profile.activation.ProfileActivator;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.eclipse.aether.impl.RemoteRepositoryManager;

import com.carrotgarden.maven.activator.SupportFunction.Identity;

/**
 * Shared implementation of extension profile activators. Singleton component.
 */
public abstract class ActivatorBase implements ProfileActivator, ActivatorAny {

	/**
	 * Logger provided by Maven runtime.
	 */
	@Requirement
	protected Logger logger;

	/**
	 * Builder provided by Maven runtime.
	 */
	@Requirement
	protected ModelBuilder builder;

	/**
	 * Manager provided by Maven runtime.
	 */
	@Requirement
	protected RemoteRepositoryManager remoteManager;

	/**
	 * Remember processed projects/profiles.
	 */
	protected Set<Identity> processGuard = new HashSet<>();

	/**
	 * Remember processed projects/profiles.
	 */
	protected Map<Identity, EvaluationResult> processCache = new HashMap<>();

	/**
	 * Remember processed projects/profiles.
	 */
	protected EvaluationResult processResult(Profile profile, //
			ProfileActivationContext context, //
			ModelProblemCollector problems //
	) {
		Identity identity = identityFrom(profile, context);
		if (processCache.containsKey(identity)) {
			return processCache.get(identity);
		} else {
			EvaluationResult result = evaluateResult(profile, context, problems);
			processCache.put(identity, result);
			String report = String.format( //
					"%s project='%s' profile='%s' result='%s'", //
					prefix(), projectName(context), profileId(profile), result.render());
			logger.info(report);
			return result;
		}
	}

	protected ThreadLocal<Identity> localIdentity = new ThreadLocal<>();

	/**
	 * Evaluate script expression provided by this activator property.
	 */
	protected EvaluationResult evaluateResult( //
			Profile profile, //
			ProfileActivationContext context, //
			ModelProblemCollector problems //
	) {
		try {

			// Required engine.
			ScriptEngine engine = scriptEngine(activatorEngine());
			if (engine == null) {
				Exception error = new Exception("Missing Engine");
				reportProblem(error.getMessage(), error, profile, context, problems);
				logger.error("Engine factory:\n" + renderEngineList());
				return EvaluationResult.failure(error);
			}

			// Required script.
			String script = propertyValue(profile);
			if (script == null) {
				Exception error = new Exception("Missing Script");
				reportProblem(error.getMessage(), error, profile, context, problems);
				return EvaluationResult.failure(error);
			}

			// Required project.
			Model project = resolveModel(context);
			if (project == null) {
				Exception error = new Exception("Invalid Project");
				reportProblem(error.getMessage(), error, profile, context, problems);
				return EvaluationResult.failure(error);
			}

			// Evaluation proper.
			Bindings bindings = engine.createBindings();
			bindings.putAll(bindingsFrom(context, project));
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("%s bindings:\n%s", prefix(), renderBindings(bindings)));
			}
			Object result = engine.eval(script, bindings);
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("%s result='%s'", prefix(), result));
			}

			// Convert result.
			boolean value = Boolean.parseBoolean("" + result);
			return EvaluationResult.success(value);

		} catch (Exception error) {

			// Report errors.
			String script = propertyValue(profile);
			String message = "Evaluation failure:\n" + script + "\n";
			reportProblem(message, error, profile, context, problems);
			return EvaluationResult.failure(error);

		}
	}

	/**
	 * Detect if profile should be activated as a result of script evaluation.
	 */
	@Override
	public boolean isActive( //
			Profile profile, //
			ProfileActivationContext context, //
			ModelProblemCollector problems //
	) {
		if (!presentInConfig(profile, context, problems)) {
			return false;
		}

		Identity identity = identityFrom(profile, context);

		processGuard.add(identity);

		EvaluationResult result = processResult(profile, context, problems);

		processGuard.remove(identity);

		if (result.valid) {
			return result.value;
		} else {
			return false;
		}
	}

	/**
	 * Detect if profile contains magic property name managed by this activator.
	 */
	@Override
	public boolean presentInConfig( //
			Profile profile, //
			ProfileActivationContext context, //
			ModelProblemCollector problems //
	) {

		Identity identity = identityFrom(profile, context);

		if (processGuard.contains(identity)) {
			return false;
		}

		return hasPropertyName(profile) && activatorName().equals(propertyName(profile));
	}

	/**
	 * Report titled activator problem.
	 */
	protected void reportProblem( //
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

	/**
	 * Resolve project pom.xml model: interpolate properties and fields.
	 * 
	 * @param profile
	 */
	protected Model resolveModel( //
			ProfileActivationContext context //
	) {
		try {
			ModelBuildingRequest request = buildRequest(context);
			ModelBuildingResult result = builder.build(request);
			Model model = result.getEffectiveModel();
			return model;
		} catch (Exception error) {
			logger.error(String.format("%s model build error: ", prefix(), error.getMessage()), error);
			return null;
		}
	}

}
