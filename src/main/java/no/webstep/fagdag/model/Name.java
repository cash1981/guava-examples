package no.webstep.fagdag.model;

import com.google.common.base.Objects;

public final class Name {

    private final String firstname;
    private final String lastname;

    public Name(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("firstname", firstname)
                .add("lastname", lastname)
                .omitNullValues()
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(firstname, lastname);
        // Java 7:
//        return java.util.Objects.hash(firstname, lastname);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Name other = (Name) obj;
        return Objects.equal(firstname, other.firstname) &&
                Objects.equal(lastname, other.lastname);
        // Java 7:
//        return java.util.Objects.equals(firstname, other.firstname) &&
//                java.util.Objects.equals(lastname, other.lastname)
    }
}
