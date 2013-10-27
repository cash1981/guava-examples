package no.webstep.fagdag.guava;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.nio.charset.Charset;
import java.util.Arrays;

import no.webstep.fagdag.model.Customer;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.base.Defaults;
import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

public class BaseTest {

	@Test
	public void charsetOgDefaults() {
		// Here's some charsets
		Charset utf8 = Charsets.UTF_8;
		
		assertTrue(utf8.canEncode());

		// Veldig kjekk dersom du driver med reflection
		// Primitive defaults:
		Integer defaultValue = Defaults.defaultValue(int.class);
		assertEquals(0, defaultValue.intValue());
	}

	@Test
	public void strings() {
		assertNull(Strings.emptyToNull(""));
		assertEquals("", Strings.nullToEmpty(null));
		assertTrue(Strings.isNullOrEmpty("")); // Eneste vi brukte i commons-lang? 
		assertEquals("oioioi", Strings.repeat("oi", 3));
	}

	// Some customers
	final Customer bob = new Customer(1, "Bob");
	final Customer lisa = new Customer(2, "Lisa");
	final Customer stephen = new Customer(3, "Stephen");
	final Customer ken = new Customer(null, "Ken");

	@Test
	public void toStringsAndHashcodes() {
		Object[] bobAndLisa = new Object[] { bob, lisa };

		// Make some hashcode!
		int hashCode = Objects.hashCode(bob, lisa);

		assertEquals(Arrays.hashCode(bobAndLisa), hashCode);

		// Build toString method
		String string = Objects.toStringHelper(bob).add("name", bob.getName())
				.add("id", bob.getId()).toString();
		assertEquals("Customer{name=Bob, id=1}", string);
	}
	
	@Test
	  public void joinSomeStrings() {
	    ImmutableSet<String> strings = ImmutableSet.of("A", "B", "C", null, null, "D");
	    String joined = Joiner.on(":")
	                        .skipNulls()
	                        //.useForNull("null")
	            .join(strings); //Tar array, iterable, iterator
	    assertEquals("A:B:C:D", joined);
	  }

	  @Test
	  public void splitSomeStrings() {
	    String string = "A:B:C";

	    String[] parts = string.split(":"); // den gamle måten
	    String backTogether = Joiner.on(":").join(parts);
	    assertEquals(string, backTogether);

	    String gorbleString = "   : A::: B : C :::";
	    Iterable<String> gorbleParts = Splitter.on(":")
	        .omitEmptyStrings()
	        .trimResults()
	        //limit(4)
	        .split(gorbleString);
	        //splitToList(string)
	    String gorbleBackTogether = Joiner.on(":").join(gorbleParts);
	    assertEquals(string, gorbleBackTogether); // A:B:C
	  }

	@Test(expected = NullPointerException.class)
	public void needAnIntegerWhichIsNeverNull() {
		Integer defaultId = null;
		Integer kensId = ken.getId() != null ? ken.getId() : defaultId;
		// this one does not throw!

		//Kaster null dersom begge er null
		int kensId2 = Objects.firstNonNull(ken.getId(), defaultId);
		assertEquals(0, kensId2);
		// But the above does!
	}

	@Test(expected = IllegalArgumentException.class)
	public void somePreconditions() {
		// Pretend this is a constructor:
		Preconditions.checkNotNull(lisa.getId()); // Will throw NPE
		Preconditions.checkState(!lisa.isSick()); // Will throw
													// IllegalStateException
		Preconditions.checkArgument(lisa.getAddress() != null,
				"We couldn't find the description for customer with id %s",
				lisa.getId());
	}

	@Test
	public void fancierFunctions() {
		Function<Customer, Boolean> isCustomerWithOddId = new Function<Customer, Boolean>() {

			public Boolean apply(Customer customer) {
				return customer.getId().intValue() % 2 != 0;
			}
		};

		assertTrue(isCustomerWithOddId.apply(bob));
		assertFalse(isCustomerWithOddId.apply(lisa));

		// Functions are great for higher-order functions, like
		// project/transform, and fold
	}

	@Test
	public void somePredicates() {
		ImmutableSet<Customer> customers = ImmutableSet.of(bob, lisa, stephen);

		Predicate<Customer> itsBob = Predicates.equalTo(bob);
		Predicate<Customer> itsLisa = Predicates.equalTo(lisa);
		Predicate<Customer> bobOrLisa = Predicates.or(itsBob, itsLisa);
		// Predicates are great to pass in to higher-order functions like
		// filter/search
		Iterable<Customer> filtered = Iterables.filter(customers, bobOrLisa);
		assertEquals(2, ImmutableSet.copyOf(filtered).size());
	}

	@Test
	public void someSuppliers() {
		// Imagine we have Supplier that produces ingredients
		IngredientsFactory ingredientsFactory = new IngredientsFactory();

		// A function 'bake' that transforms ingredients into cakes
		bake();

		// Then it's pretty easy to get a Factory that bakes cakes :)
		Supplier<Cake> cakeFactory = Suppliers.compose(bake(),
				ingredientsFactory);
		cakeFactory.get();
		cakeFactory.get();
		cakeFactory.get();

		assertEquals(3, ingredientsFactory.getNumberOfIngredientsUsed());

	}

	@Test
	public void someThrowables() {
		try {
			try {
				Integer.parseInt("abc");
			} catch (RuntimeException e) {
				if (e instanceof ClassCastException)
					throw e; // old-style
				Throwables.propagateIfInstanceOf(e, NumberFormatException.class); // the
																				// same
				Throwables.propagateIfPossible(e); // Propagates if it is Error
													// or RuntimeException
			}
		} catch (RuntimeException e) {
			Throwables.getCausalChain(e);
			Throwables.getRootCause(e);
			Throwables.getStackTraceAsString(e);
		}
	}

	private Function<Ingredients, Cake> bake() {
		return new Function<Ingredients, Cake>() {

			public Cake apply(Ingredients ingredients) {
				return new Cake(ingredients);
			}
		};
	}

}

class Ingredients {

}

class Cake {

	Cake(Ingredients ingredients) {

	}
}

class IngredientsFactory implements Supplier<Ingredients> {

	private int counter;

	public Ingredients get() {
		counter++;
		return new Ingredients();
	}

	int getNumberOfIngredientsUsed() {
		return counter;
	}
}
