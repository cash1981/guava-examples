package no.webstep.fagdag.model;

public final class Person {

    private String firstname;
    private String lastname;
    private Gender gender;

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public Gender getGender() {
        return gender;
    }

    public Person setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public Person setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public Person setGender(Gender gender) {
        this.gender = gender;
        return this;
    }
}
