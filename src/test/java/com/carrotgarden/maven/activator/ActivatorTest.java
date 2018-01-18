package com.carrotgarden.maven.activator;

import static org.junit.Assert.*;

import javax.script.ScriptEngine;

import org.junit.Ignore;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ActivatorTest implements Activator {

	@Test
	public void engineGroovyScript() {
		ActivatorAny activator = new ActivatorGroovyScript();
		String name = activator.activatorEngine();
		ScriptEngine engine = SupportFunction.scriptEngine(name);
		assertNotNull(engine);
	}

	@Test
	public void engineJavaScript() {
		ActivatorAny activator = new ActivatorJavaScript();
		String name = activator.activatorEngine();
		ScriptEngine engine = SupportFunction.scriptEngine(name);
		assertNotNull(engine);
	}

	@Test
	public void engineMvelScript() {
		ActivatorAny activator = new ActivatorMvelScript();
		String name = activator.activatorEngine();
		ScriptEngine engine = SupportFunction.scriptEngine(name);
		assertNotNull(engine);
	}

	// FIXME slow/unstable initialization
	@Ignore
	@Test
	public void engineScalaScript() {
		ActivatorAny activator = new ActivatorScalaScript();
		String name = activator.activatorEngine();
		ScriptEngine engine = SupportFunction.scriptEngine(name);
		assertNotNull(engine);
	}

}
