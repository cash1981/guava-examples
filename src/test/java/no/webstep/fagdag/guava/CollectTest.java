package no.webstep.fagdag.guava;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newLinkedList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Maps.newTreeMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static no.webstep.fagdag.model.CollectionTestData.getPersons;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import no.webstep.fagdag.model.CollectionTestData;
import no.webstep.fagdag.model.Gender;
import no.webstep.fagdag.model.Name;
import no.webstep.fagdag.model.Person;

import org.fest.assertions.Assertions;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;

public class CollectTest {
    List<String> navnList = Arrays.asList("Per", "Pål", "Stine",
            "Per", "Ida", "Kjell", "Per", "Ola", "Kjell", "Rolf", "Silje",
            "Line", "Terje", "Ola", "Silje", "Ida", "Ida");

    @Test
    public void finneAntallAvHverOgTotal() {
        // Hvor mange av hver
        Map<String, Integer> ant = new HashMap<String, Integer>();
        for (String s : navnList) {
            int antall = ant.containsKey(s) ? ant.get(s) : 0;
            ant.put(s, antall + 1);
        }
        // Distinct navn : ant.keySet()
        // Antall Ida : ant.containsKey("Ida) ? ant.get("Ida") : 0

        int antallIda = ant.get("Ida");
        assertEquals(3, antallIda);

        // Totale antall == Ehhhm

        // kjør gjennom mapen og tell alle antall
        int counter = 0;
        for (Entry<String, Integer> antall : ant.entrySet()) {
            counter += antall.getValue().intValue();
        }
        assertEquals(17, counter);
    }

    @Test
    public void finneAntallAvHverGuava() {
        // The guava way
        Multiset<String> multiAnt = HashMultiset.create(navnList);
        // Distinct navn : multiAnt.elementSet()

        // antall Ida :
        assertEquals(3, multiAnt.count("Ida"));

        // totale antall :
        assertEquals(17, multiAnt.size());

        // Andre multiset: ConcurrentHashMultiset, EnumMultiSet ....
    }

    @Test
    public void multimap() {
        /*
         * Vanlig map: Multimap
         * 
         * a -> 1 b -> 2 b -> 2 c -> 3 c -> 3 d -> 4 d -> 4 a -> 4
         * 
         * 
         * Ofte ønsker vi å se de slik:
         * 
         * a -> 1, 4 b -> 2 c -> 3 d -> 4
         */

        // Pleid å løse dette med
        Map<String, List<Integer>> gammelMap = Maps.newHashMap();
        putMyObject(gammelMap, "a", 1);
        putMyObject(gammelMap, "b", 2);
        putMyObject(gammelMap, "c", 3);
        putMyObject(gammelMap, "d", 4);
        putMyObject(gammelMap, "a", 4);

        // Guava
        Multimap<String, Integer> multiListMap = ArrayListMultimap.create(); // backed 
                                                                                // by
                                                                                // arraylist
         /*
          * Finnes mange versjoner av multimap - 
          * ArrayListMultimap, LinkedListMultimap, HashMultimap, TreeMultimap osv
         */
        
        multiListMap.putAll("a", ImmutableList.of(1, 4));
        multiListMap.put("b", 2);
        multiListMap.put("c", 3);
        multiListMap.put("d", 4);

        assertEquals(2, multiListMap.get("a").size());
        assertEquals(5, multiListMap.size());

        // Kan bruke asMap for å få den som vanlig map
        Map<String, Collection<Integer>> asMap = multiListMap.asMap();

        Multimap<String, String> myMultimap = ArrayListMultimap.create();

        // Legge til noen felter
        myMultimap.put("Fruits", "Banana");
        myMultimap.put("Fruits", "Apple");
        myMultimap.put("Fruits", "Pear");
        myMultimap.put("Vegetables", "Carrot");

        // Getting the size
        int size = myMultimap.size();
        assertEquals(4, size);
        System.out.println(size); // 4

        // Getting values
        Collection<String> fruits = myMultimap.get("Fruits");
        System.out.println(fruits); // [Banana, Apple, Pear] 

        Collection<String> vegetables = myMultimap.get("Vegetables");
        System.out.println(vegetables); // [Carrot]

        // Iterating over entire Mutlimap
        for (String value : myMultimap.values()) {
            System.out.println(value);
        }

        // Removing a single value
        myMultimap.remove("Fruits", "Pear");
        assertEquals(2, myMultimap.get("Fruits").size());
        System.out.println(myMultimap.get("Fruits")); // [Banana, Pear]

        // Remove all values for a key
        myMultimap.removeAll("Fruits");
        assertTrue(myMultimap.get("Fruits").isEmpty());
        System.out.println(myMultimap.get("Fruits")); // [] (Empty Collection!)
    }

    void putMyObject(Map<String, List<Integer>> gammelMap, String key, Integer value) {
        List<Integer> list = gammelMap.get(key);
        if (list == null) {
            list = new ArrayList<Integer>();
            gammelMap.put(key, list);
        }
        list.add(value);
    }
    
    /**
     * You find filters in:
     * <code> Iterables and Collections2 </code>, there's a diverant of it in <code>Maps</code> for keys and values
     */
    @Test
    public void filterOutFemales() throws Exception {
        Collection<Person> females = Collections2.filter(getPersons(),
                new Predicate<Person>() {
                    public boolean apply(Person person) {
                        return person.getGender() == Gender.FEMALE;
                    }
                });

        Assertions.assertThat(females).hasSize(7);
    }

    /**
     * You find transform in:
     * <code>Iterables, Lists andCollections2</code> , there's a diverant of it in <code>Maps</code> for keys and values
     */
    @Test
    public void transformPersonToDisplayStrings() throws Exception {
        Collection<String> lastnameFirstnameList = Collections2.transform(getPersons(),
                new Function<Person, String>() {
                    public String apply(Person input) {
                        return input.getLastname().toUpperCase()
                                + " - " + input.getFirstname().toLowerCase();
                    }
                });

        Assertions.assertThat(lastnameFirstnameList).hasSize(15);
        assertTrue(lastnameFirstnameList.contains("PETTERSEN"));
        assertTrue(lastnameFirstnameList.contains("ida"));
    }

    @Test
    public void transformMapOfPersionsToMapOfNames() throws Exception {
        Map<Integer, Name> names = Maps.transformEntries(CollectionTestData.getPersonMapWithIds(),
                new Maps.EntryTransformer<Integer, Person, Name>() {
                    public Name transformEntry(Integer key, Person value) {
                        return new Name(value.getFirstname(), value.getLastname());
                    }
                });

        Name name = names.get(1);
        assertEquals(name, new Name("Per", "Nordmann"));
    }

    @Test
    public void fluentInterface() throws Exception {
        List<Name> names = FluentIterable.from(getPersons())
                .filter(new Predicate<Person>() {
                    @Override
                    public boolean apply(Person input) {
                        return input.getLastname().equals("Nordmann");
                    }
                }).transform(new Function<Person, Name>() {
                    @Override
                    public Name apply(Person input) {
                        return new Name(input.getFirstname(), input.getLastname());
                    }
                }).skip(1)
                .limit(1)
                .toList();
        
        Assertions.assertThat(names).hasSize(1);
    }

    @Test
    @SuppressWarnings("unused")
    public void creatingCollections() throws Exception {
        List<String> strList = newArrayList("value1", "value2", "value3");
        List<Integer> intList = newLinkedList();

        Map<Integer, BigDecimal> hashmap = newHashMap();
        Map<Integer, BigDecimal> treemap = newTreeMap();

        Map<Integer, String> imMap = ImmutableMap.of(1, "v1", 2, "v2", 3, "v3");
    }

    @Test
    public void groupToMultiMap() throws Exception {
        Multimap<String, Person> groupedByFirstname = Multimaps.index(getPersons(),
                new Function<Person, String>() {
                    @Override
                    public String apply(Person person) {
                        return person.getFirstname();
                    }
                });

        Assertions.assertThat(groupedByFirstname.get("Ida")).hasSize(3);

    }
    
    
    @Test
    public void fluentIterable() throws Exception {
        //Tar en Collection<Person>, og filtrerer ut kun de med etternavn Nordmann
        //Hopp over den første
        //Så transformerer du det til Name objektet
        //limit resultatet til å bare ha 1 
        //Returner som List
        
        ImmutableList<Name> names = FluentIterable.from(getPersons())
                .filter(new Predicate<Person>() {
                    @Override
                    public boolean apply(Person input) {
                        return input.getLastname().equals("Nordmann");
                    }
                }).transform(new Function<Person, Name>() {
                    @Override
                    public Name apply(Person input) {
                        return new Name(input.getFirstname(), input.getLastname());
                    }
                }).skip(1)
                .limit(1)
                .toList();
        Assertions.assertThat(names).hasSize(1);
    }
    
    //Litt goodies
    
    @Test
    public void concat() {
        Iterable<Integer> concatenated = Iterables.concat(
                Ints.asList(1, 2, 3),
                Ints.asList(4, 5, 6));
        // concatenated har elementene 1, 2, 3, 4, 5, 6
        
        int siste = Iterables.getLast(concatenated);
        assertEquals(6, siste);
        
        //Iterables har mange av metodene som finnes i java.util.Collections men som funker på en Iterable
        assertFalse(Iterables.isEmpty(concatenated));
        
        int indexTo = Iterables.get(concatenated, 2);
        assertEquals(3, indexTo);
    }
    
    @Test
    public void hvemErKjappest() {
        List<RedigerbareFelterPaaAvtalebytteMedlemDto> avtalebytteMedlemmer = Lists.newArrayList();
        Set<Foedselsnummer> medlemmerMedPensjonsmelding = Sets.newLinkedHashSet();
        
        for(int i = 0; i<100000;i++) {
            avtalebytteMedlemmer.add(new RedigerbareFelterPaaAvtalebytteMedlemDto());
            medlemmerMedPensjonsmelding.add(new Foedselsnummer("" +i));
        }
        
        Stopwatch stopwatch = Stopwatch.createStarted();
        vanlig(avtalebytteMedlemmer, medlemmerMedPensjonsmelding, new NyttAvtalebytteMedMedlemUttrekk(), new Avtalebytte());
        stopwatch.stop();
        long millis = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        System.out.println("vanlig time: " + stopwatch); // formatted string like "12.3 ms"
        
        
        stopwatch.start();      
        overkill2(avtalebytteMedlemmer, medlemmerMedPensjonsmelding, new NyttAvtalebytteMedMedlemUttrekk(), new Avtalebytte());
        stopwatch.stop();
        millis = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        System.out.println("overkill time: " + stopwatch); // formatted string like "12.3 ms"
    }
    
    private void vanlig(
            List<RedigerbareFelterPaaAvtalebytteMedlemDto> avtalebytteMedlemmer,
            Set<Foedselsnummer> medlemmerMedPensjonsmelding,
            NyttAvtalebytteMedMedlemUttrekk uttrekk,
            Avtalebytte avtalebytte) {
        
        Set<Foedselsnummer> utelukk = new HashSet<Foedselsnummer>();
        for (RedigerbareFelterPaaAvtalebytteMedlemDto medlem : avtalebytteMedlemmer) {
            utelukk.add(medlem.getMedlemId());
        }
        for (Foedselsnummer foedselsnummer : medlemmerMedPensjonsmelding) {
            if (!utelukk.contains(foedselsnummer)
                    && erFoedselsnummerGyldigForPeriode(foedselsnummer, uttrekk.getFoedselsdatoFraOgMed(), uttrekk.getFoedselsdatoTilOgMed())) {
                RedigerbareFelterPaaAvtalebytteMedlemDto dto = new RedigerbareFelterPaaAvtalebytteMedlemDto()
                        .setAvtalebytteId(avtalebytte.getAvtalebytteId())
                        .setMedlemId(foedselsnummer)
                        .setPensjonertMedlem(true);
                avtalebytteMedlemmer.add(dto);
            }
        }
    }
    
    //Bruk hodet og sunn fornuft
    @SuppressWarnings("unused")
    private void overkill(
            final List<RedigerbareFelterPaaAvtalebytteMedlemDto> avtalebytteMedlemmer,
            Set<Foedselsnummer> medlemmerMedPensjonsmelding,
            final NyttAvtalebytteMedMedlemUttrekk uttrekk,
            final Avtalebytte avtalebytte) {
        
        avtalebytteMedlemmer.addAll(FluentIterable.from(medlemmerMedPensjonsmelding)
                .filter(new Predicate<Foedselsnummer>() {
                    @Override
                    public boolean apply(final Foedselsnummer foedselsnummer) {
                        return !Iterables.any(avtalebytteMedlemmer, new Predicate<RedigerbareFelterPaaAvtalebytteMedlemDto>() {
                            @Override
                            public boolean apply(RedigerbareFelterPaaAvtalebytteMedlemDto dto) {
                                return dto.getMedlemId().equals(foedselsnummer);
                            }
                        })
                                &&  erFoedselsnummerGyldigForPeriode(foedselsnummer, uttrekk.getFoedselsdatoFraOgMed(), uttrekk.getFoedselsdatoTilOgMed());
                    }
                })
                .transform(new Function<Foedselsnummer, RedigerbareFelterPaaAvtalebytteMedlemDto>() {
                    @Override
                    public RedigerbareFelterPaaAvtalebytteMedlemDto apply(Foedselsnummer foedselsnummer) {
                        RedigerbareFelterPaaAvtalebytteMedlemDto dto = new RedigerbareFelterPaaAvtalebytteMedlemDto()
                                .setAvtalebytteId(avtalebytte.getAvtalebytteId())
                                .setMedlemId(foedselsnummer)
                                .setPensjonertMedlem(true);
                        return dto;
                    }
                })
                .toList());
    }
    
    private void overkill2(
            final List<RedigerbareFelterPaaAvtalebytteMedlemDto> avtalebytteMedlemmer,
            Set<Foedselsnummer> medlemmerMedPensjonsmelding,
            final NyttAvtalebytteMedMedlemUttrekk uttrekk,
            final Avtalebytte avtalebytte) {
        
         
        final Set<Foedselsnummer> utelukk = new HashSet<Foedselsnummer>();
        for (RedigerbareFelterPaaAvtalebytteMedlemDto medlem : avtalebytteMedlemmer) {
            utelukk.add(medlem.getMedlemId());
        }
        
        ImmutableList<RedigerbareFelterPaaAvtalebytteMedlemDto> list = FluentIterable.from(medlemmerMedPensjonsmelding)
        .filter(new Predicate<Foedselsnummer>() {
            @Override
            public boolean apply(final Foedselsnummer foedselsnummer) {
                return !utelukk.contains(foedselsnummer) &&
                        erFoedselsnummerGyldigForPeriode(foedselsnummer, uttrekk.getFoedselsdatoFraOgMed(), uttrekk.getFoedselsdatoTilOgMed());
            }
        })
        .transform(new Function<Foedselsnummer, RedigerbareFelterPaaAvtalebytteMedlemDto>() {
            @Override
            public RedigerbareFelterPaaAvtalebytteMedlemDto apply(Foedselsnummer foedselsnummer) {
                RedigerbareFelterPaaAvtalebytteMedlemDto dto = new RedigerbareFelterPaaAvtalebytteMedlemDto()
                        .setAvtalebytteId(avtalebytte.getAvtalebytteId())
                        .setMedlemId(foedselsnummer)
                        .setPensjonertMedlem(true);
                return dto;
            }
        })
        .toList();
        
        avtalebytteMedlemmer.addAll(list);
    }
    
    private boolean erFoedselsnummerGyldigForPeriode(
            Foedselsnummer foedselsnummer, boolean foedselsdatoFraOgMed,
            boolean foedselsdatoTilOgMed) {
        return true;
    }

    private class RedigerbareFelterPaaAvtalebytteMedlemDto {
        Foedselsnummer getMedlemId() {
            return new Foedselsnummer("0");
        }

        public RedigerbareFelterPaaAvtalebytteMedlemDto setAvtalebytteId(int avtalebytteId) {
            return this;
        }
        
        public RedigerbareFelterPaaAvtalebytteMedlemDto setMedlemId(Foedselsnummer f) {
            return this;
        }
        
        public RedigerbareFelterPaaAvtalebytteMedlemDto setPensjonertMedlem(boolean b) {
            return this;
        }
    }
    
    private class NyttAvtalebytteMedMedlemUttrekk {
        boolean getFoedselsdatoFraOgMed() { return true; }
        boolean getFoedselsdatoTilOgMed() { return true; }
    }
    
    private class Foedselsnummer {
        String fnr;

        public Foedselsnummer(String fnr) {
            this.fnr = fnr;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(fnr);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Foedselsnummer other = (Foedselsnummer) obj;
            return Objects.equal(fnr, other.fnr);
        }
        
    }
    
    private class Avtalebytte {
        int getAvtalebytteId() { return 0; }
    }
}
