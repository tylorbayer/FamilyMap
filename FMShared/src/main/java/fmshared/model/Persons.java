package fmshared.model;


import java.io.Serializable;
import java.util.Arrays;

/** A class to store Person information from the FMS database and use in memory
 *
 * @author TylorBayer
 */
public class Persons extends FMSModel implements Serializable {
    /** Unique identifier for this person */
    private String personID;
    /** User (username) to which this person belongs */
    private String descendant;
    /** Person's first name */
    private String firstName;
    /** Person's last name */
    private String lastName;
    /** Person's gender */
    private String gender;
    /** ID of person's father */
    private String father = "";
    /** ID of person's mother */
    private String mother = "";
    /** ID of person's spouse */
    private String spouse = "";


    /** Constructor for use within FMS
     *
     * @param personID Unique identifier for this person (non-empty string)
     * @param descendant User (username) to which this person belongs (non-empty string)
     * @param firstName Person's first name (non-empty string)
     * @param lastName Person's last name (non-empty string)
     * @param gender Person's gender ('m' or 'f')
     * @param father ID of person's father (possibly null)
     * @param mother ID of person's mother (possibly null)
     * @param spouse ID of person's spouse (possibly null)
     */
    public Persons(String personID, String descendant, String firstName, String lastName, String gender, String father, String mother, String spouse) {
        this.personID = personID;
        this.descendant = descendant;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.father = father;
        this.mother = mother;
        this.spouse = spouse;

        values.add(personID);
        values.add(descendant);
        values.add(firstName);
        values.add(lastName);
        values.add(gender);
        values.add(father);
        values.add(mother);
        values.add(spouse);

    }

    public Persons(String[] values) {
        this.values.addAll(Arrays.asList(values));
    }

    public String getPersonID() {
        return personID;
    }

    /** Constructor for GSON */
    public Persons() {}

    public String getFatherID() {
        return father;
    }

    public String getMotherID() {
        return mother;
    }

    public String getSpouseID() {
        return spouse;
    }

    public String getDescendant() {
        return descendant;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getGender() {
        return gender;
    }

    public void populateValues() {
        values.add(personID);
        values.add(descendant);
        values.add(firstName);
        values.add(lastName);
        values.add(gender);
        values.add(father);
        values.add(mother);
        values.add(spouse);
    }
}
