package com.carrotgarden.maven.activator;

/**
 * Script evaluation result.
 */
public class EvaluationResult {

	public static EvaluationResult failure() {
		return new EvaluationResult(false, false, null);
	}

	public static EvaluationResult failure(Exception error) {
		return new EvaluationResult(false, false, error);
	}

	public static EvaluationResult success(boolean value) {
		return new EvaluationResult(true, value, null);
	}

	public final boolean valid;

	public final boolean value;

	public final Exception error;

	public EvaluationResult(boolean valid, boolean value, Exception error) {
		this.valid = valid;
		this.value = value;
		this.error = error;
	}

	public String render() {
		if (valid) {
			return Boolean.toString(value);
		} else {
			return toString();
		}
	}

	@Override
	public String toString() {
		return "( valid=" + valid + ", value=" + value + ", error=" + error + " )";
	}

}
