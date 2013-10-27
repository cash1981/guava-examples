package no.webstep.fagdag.guava;

import static org.junit.Assert.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import no.webstep.fagdag.model.Customer;

import org.junit.Test;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class CacheTest {
	
	Customer bob = new Customer(1, "Bob");
	
	//@Test
	public void byggCache() {
		LoadingCache<Integer, Customer> cache = CacheBuilder.newBuilder()
		        .weakKeys()
		        .softValues()
		        .maximumSize(10000)
		        .expireAfterWrite(10, TimeUnit.MINUTES)
		        .build(new CacheLoader<Integer, Customer>() {
		          @Override
		          public Customer load(Integer key) throws Exception {
		            return retreveCustomerForKey(key); //noe som koster mye å compute
		          }
		        });

		    // .removalListener(MY_LISTENER) //legge på listener som kjøres når elementer blir fjernet fra mappen
		    cache.size();
		    
		    cache.getIfPresent(1); //Vil ikke hente fra mapen om den ikke er der
		    
		    try {
		    	assertEquals(bob, cache.get(1)); //Henter fra mappen om den ikke finnes
			} catch (ExecutionException e) {
				e.printStackTrace();
			} 
		    
		    assertEquals(bob, cache.getUnchecked(1)); //Dersom man ikke bryr seg om exception
	}

	
	private Customer retreveCustomerForKey(Integer key) {
		return bob;
	}

}
