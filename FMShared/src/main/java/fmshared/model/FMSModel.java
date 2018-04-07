package fmshared.model;

import java.util.ArrayList;

/** A base class for all model objects
 *
 * @author TylorBayer
 */
public class FMSModel {

    /** Array of strings representing values for each model class */
    transient ArrayList<String> values = new ArrayList<>();


    /** Returns an array of values to be inserted with FMSDao insert method call on all model classes
     *
     * @return Array of strings representing values to be inserted into database
     */
    public ArrayList<String> getValues() {

        return values;
    }
}
