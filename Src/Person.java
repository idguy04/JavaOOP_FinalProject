import javax.swing.*;

/**
 * The Person class - this class represents the ordinary person's fields. It
 * extends {@link ClubAbstractEntity} and therefore it inherits its
 * {@link javax.swing.JFrame} abilities, and becomes a frame of its own, which
 * hold all the neccessery information and methods of a person clubber.
 */
public class Person extends ClubAbstractEntity {
  /**
   * Values - an array that holds all the values of the person:
   *  values[0] - id, valued[1] - name, values[2] - surName, values[3] - phone number.
   */
  protected String values[];
  /**
   * textFields - this field is used for the fields which are indexed in the same
   * order of the values.
   */
  protected JTextField[] textFields;
  /**
   * strike - an array for storing the strikes labels ("*") indexed the same as
   * the values as well
   */
  protected JLabel[] strikes;

  /**
   * empty constructor that call the actual 4 parameters constructor with empty
   * Strings so that all the gui components will be made. used for making a new
   * empty entity for the system.
   */
  public Person() {
    this("", "", "", "");
  }

  /**
   * The constructors job is to initialize every gui component of the persons data
   * and sends them to the center of the parents window via
   * {@link ClubAbstractEntity#addToCenter} It also uses the
   * {@link ClubAbstractEntity#createGenericStrikeLabel},
   * {@link ClubAbstractEntity#createGenericTextField} and
   * {@link ClubAbstractEntity#createGenericRow} method to make the constructor
   * more organized and keep the consistency of the design. (also prevents
   * duplicating code).
   * 
   * @param id      the actual id value of the person
   * @param name    the actual name value of the person
   * @param surname the actual surname value of the person
   * @param tel     the actual telephone value of the person
   */
  public Person(String id, String name, String surname, String tel) {
  	// assign given values to the values array
    values = new String[] { id, name, surname, tel };
    // labels names array
    String[] labelNames = { "ID", "Name", "Surname", "Tel" };
    // strikes array
    strikes = new JLabel[values.length];
    // textfields array
    textFields = new JTextField[values.length];
    // start initialization loop
    for (int i = 0; i < values.length; i++) {
      // add the gui components to the arrays
      textFields[i] = createGenericTextField(values[i]);
      strikes[i] = createGenericStrikeLabel();
      // create a generic row and add it to the center
      addToCenter(createGenericRow(labelNames[i], textFields[i], strikes[i]));
    }
    // set the frames size and title
    setSize(450, 220);
    setLocationRelativeTo(null);
    setTitle("Person Clubber's Data");
  }

  /**
   * A function that tells us if a given string is equal to the id of the person
   * which tells us if he is in our database
   * 
   * @return true if the key that was given is equal to the id.
   */
  public boolean match(String key) {
    return (values[0].equals(key));
  }

  /**
   * a function to control the validation if the database values, it helps us to
   * maintain a consistent database where every value inside it is valid(using
   * {@link String#matches} and {@link java.util.regex.Pattern}-regular expression
   * patterns). if a values is not valid- a red mark will be shown next to it. In
   * addition this function will check if this persons id is already in the
   * database, if it is it will return false.
   * 
   * @return true if the validation went successfully, returns false otherwise
   */
  protected boolean validateData() {
    // Sets the labels to visible false -- used to hide the strike that was found in
    // the last validation.
    for (JLabel strike : strikes) {
      strike.setVisible(false);
    }
    // patterns Validation
    String[] patterns = { "\\d-\\d{7}\\|[1-9]", "[A-Z][a-z]+", "([A-Z][a-z]*('|-)?)+",
        "\\+\\([1-9]\\d{0,2}\\)[1-9]\\d{0,2}-[1-9]\\d{6}" };
    for (int i = 0; i < textFields.length; i++) {
      if (!(textFields[i].getText().matches(patterns[i]))) {
        strikes[i].setVisible(true);
        return false;
      }
    }
    // exists Validation. (last validation) if passed returns true - else false
    String insertedID = textFields[0].getText();
    return NightClubMgmtApp.existsValidate("ID", insertedID, this);
  }

  /**
   * this function writes the information into our database after the validation
   * was successful. 
   */
  protected void commit() {
    for (int i = 0; i < values.length; i++) {
      values[i] = textFields[i].getText();
    }
  }

  /**
   * this function controls what happens when we hit cancel it writes the previous
   * valid values into the textfields
   */
  protected void rollBack() {
    for (int i = 0; i < textFields.length; i++) {
      textFields[i].setText(values[i]);
    }
  }
}
