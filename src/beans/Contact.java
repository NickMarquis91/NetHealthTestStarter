package beans;

import enums.ContactLocation;
import enums.ContactStatus;
import enums.ContactTeam;

public class Contact {

    String firstName;
    String lastName;
    String title;
    String age;

    public ContactStatus getStatus() {
        return status;
    }

    public void setStatus(ContactStatus status) {
        this.status = status;
    }

    public ContactLocation getLocation() {
        return location;
    }

    public void setLocation(ContactLocation location) {
        this.location = location;
    }

    public ContactTeam getTeam() {
        return team;
    }

    public void setTeam(ContactTeam team) {
        this.team = team;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    ContactTeam team;
    ContactLocation location;
    ContactStatus status;

    public Contact () {
    }


}
