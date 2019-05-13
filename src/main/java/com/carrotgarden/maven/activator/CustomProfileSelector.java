package com.carrotgarden.maven.activator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.maven.model.Profile;
import org.apache.maven.model.building.ModelProblemCollector;
import org.apache.maven.model.profile.DefaultProfileSelector;
import org.apache.maven.model.profile.ProfileActivationContext;
import org.apache.maven.model.profile.ProfileSelector;
import org.apache.maven.model.profile.activation.ProfileActivator;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

/**
 * Profile selector which combines profiles activated by custom and default
 * activators. Overrides "default" provider.
 */
@Component( //
		role = ProfileSelector.class, //
		hint = "default" //
)
public class CustomProfileSelector extends DefaultProfileSelector {

	@Requirement
	protected Logger logger;

	/**
	 * Collect only custom activators. Default activators are in super.
	 */
	// Note: keep field name different from super to ensure proper injection.
	@Requirement(role = ActivatorAny.class)
	protected List<ProfileActivator> activatorList = new ArrayList<>();

	/**
	 * Profiles activated by both custom and default activators.
	 */
	@Override
	public List<Profile> getActiveProfiles( //
			Collection<Profile> profiles, //
			ProfileActivationContext context, //
			ModelProblemCollector problems //
	) {
		List<Profile> customList = new ArrayList<>();
		for (Profile profile : profiles) {
			if (hasActive(profile, context, problems)) {
				customList.add(profile);
			}
		}
		List<Profile> defaultList = super.getActiveProfiles(profiles, context, problems);
		ArrayList<Profile> resolvedList = new ArrayList<>();
		resolvedList.addAll(customList);
		resolvedList.addAll(defaultList);
		if (logger.isDebugEnabled() && resolvedList.size() > 0) {
			logger.info("Activator SELECT: " + Arrays.toString(resolvedList.toArray()));
		}
		return resolvedList;
	}

	/**
	 * Applies only to custom activators.
	 * 
	 * Note: "AND" for custom activators. See super.
	 */
	protected boolean hasActive( //
			Profile profile, //
			ProfileActivationContext context, //
			ModelProblemCollector problems //
	) {
		int countActive = 0;
		int countPresent = 0;
		for (ProfileActivator activator : activatorList) {
			if (activator.presentInConfig(profile, context, problems)) {
				countPresent++;
				if (activator.isActive(profile, context, problems)) {
					countActive++;
				}
			}
		}
		return (countActive > 0) && (countActive == countPresent);
	}

}
