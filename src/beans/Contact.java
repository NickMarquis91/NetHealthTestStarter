package beans;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import enums.ContactLocation;
import enums.ContactStatus;
import enums.ContactTeam;

import java.util.HashMap;
import java.util.Objects;

// The response has some metadata we don't care about, so just ignore it
@JsonIgnoreProperties({"etag","partitionKey","rowKey","timestamp"})
public class Contact {

    private String firstName;
    private String lastName;
    private String title;
    private String age;
    private ContactTeam team;
    private ContactLocation location;
    private ContactStatus status;
    private HashMap<String,Object> extraProperties = new HashMap<>(); // use this to serialize/deserialize any extra properties like powers, abilities, birthday etc

    public Contact() {
    }

    /**
     * Create a Contact using just the title (used for fetching)
     * @param title
     */
    public Contact(String title) {
        this.title = title;
    }


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

    // Jackson will automatically map unknown properties not defined in @JsonIgnoreProperties to this, and reserialize them appropriately
    @JsonAnyGetter
    public HashMap<String, Object> getExtraProperties() {
        return extraProperties;
    }

    @JsonAnySetter
    public void addProperties(String property, Object value) {
        this.extraProperties.put(property, value);
    }

    /**
     * Treat title as identifier for equality
     * @param other
     * @return
     */
    @Override
    public boolean equals(Object other) {

        if (other == this) {
            return true;
        }

        if (!(other instanceof Contact)) {
            return false;
        }

        return this.title.equals(((Contact) other).getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(title);
    }

    @Override
    public String toString() {
        return "Contact{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", title='" + title + '\'' +
                ", age='" + age + '\'' +
                ", team=" + team +
                ", location=" + location +
                ", status=" + status +
                ", extraProperties=" + extraProperties +
                '}';
    }
}
