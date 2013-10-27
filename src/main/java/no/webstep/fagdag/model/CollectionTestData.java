package no.webstep.fagdag.model;

import static com.google.common.collect.Lists.newArrayList;
import static no.webstep.fagdag.model.Gender.FEMALE;
import static no.webstep.fagdag.model.Gender.MALE;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CollectionTestData {

    /**
     * Contains:<br/>
     * - 8 Males and 7 females. Total of 15 records.<br/>
     * - 3 persons with lastname Nordmann<br/>
     * - 3 females with firstname Ida<br/>
     */
    public static Collection<Person> getPersons() {
        return newArrayList(
                new Person().setFirstname("Stine").setLastname("Bakk").setGender(FEMALE),
                new Person().setFirstname("Per").setLastname("Nordmann").setGender(MALE),
                new Person().setFirstname("Ida").setLastname("Pettersen").setGender(FEMALE),
                new Person().setFirstname("Kjell").setLastname("Hansen").setGender(MALE),
                new Person().setFirstname("Per").setLastname("Hansen").setGender(MALE),
                new Person().setFirstname("Ola").setLastname("Nordmann").setGender(MALE),
                new Person().setFirstname("Kjell").setLastname("Nordmann").setGender(MALE),
                new Person().setFirstname("Rolf").setLastname("Pettersen").setGender(MALE),
                new Person().setFirstname("Silje").setLastname("Andersen").setGender(FEMALE),
                new Person().setFirstname("Line").setLastname("Berdal").setGender(FEMALE),
                new Person().setFirstname("Terje").setLastname("Nordsen").setGender(MALE),
                new Person().setFirstname("Ola").setLastname("Hansen").setGender(MALE),
                new Person().setFirstname("Silje Marie").setLastname("Andersen").setGender(FEMALE),
                new Person().setFirstname("Ida").setLastname("Engen").setGender(FEMALE),
                new Person().setFirstname("Ida").setLastname("Pettersen").setGender(FEMALE)
        );
    }

    public static Map<Integer, Person> getPersonMapWithIds() {
        HashMap<Integer, Person> persionsWithIds = new HashMap<Integer, Person>();
        int id = 0;
        for (Person person : getPersons()) {
            persionsWithIds.put(id++, person);
        }
        return persionsWithIds;
    }
    
    public static Map<String, Person> getPersonMapByFirstname() {
    	Map<String, Person> person = new HashMap<String, Person>();
    	for(Person p : getPersons()) {
    		person.put(p.getFirstname(), p);
    	}
    	return person;
    }
}
