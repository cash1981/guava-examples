package no.webstep.fagdag.guava;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.google.common.base.Optional;

import org.junit.Test;

/**
 * Java 8 have a similar implementation.
 */
public class OptionalTest {

	@Test
	public void absent() throws Exception {
		assertThat(getAbsent(), is(Optional.<String> absent()));
		assertThat(getOptional(null), is(Optional.<String> absent()));

		assertThat(getAbsent().isPresent(), is(false));
	}

	@Test
	public void present() throws Exception {
		assertThat(getOptionalOf(true).isPresent(), is(true));

		assertThat(getOptional("aValue").get(), is("aValue"));
	}

	@Test
	public void presentOrGetValue() throws Exception {
		assertThat(getOptional("aValue").or("anotherValue"), is("aValue"));

		assertThat(getOptional(null).or("anotherValue"), is("anotherValue"));
	}

	private Optional<String> getOptionalOf(boolean hasValue) {
		if (hasValue) {
			return Optional.of("aValue"); // throws NPE if value is null
		}
		return Optional.absent();
	}

	private Optional<String> getAbsent() {
		return Optional.absent();
	}

	private Optional<String> getOptional(String value) {
		return Optional.fromNullable(value); // returns absent if value is null
	}

}
