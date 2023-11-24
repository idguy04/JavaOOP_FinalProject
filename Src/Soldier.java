import javax.swing.*;

/**
 * This class extends {@link Person} and inherits all of the basic fundementals
 * of a person. In addition it will take care of every other Soldier aspect that
 * the Person does not have.
 * 
 */
public class Soldier extends Person {
  /** personalNum - The Soldiers personal number */
  private String personalNum;
  /** personalNumField - The text field that holds the personal number */
  private JTextField personalNumField;
  /** soldierStrike - the label that was premade for the strikes */
  private JLabel soldierStrike;

  /**
   * Empty constructor - uses the 5 parameter costructor to initialize an empty
   * entity.
   */
  public Soldier() {
    this("", "", "", "", "");
  }

  /**
   * This 5 parameter constructor calls the parent constructor for initializing
   * the basic {@link Person} field, than it initializes the rest (using:
   * {@link ClubAbstractEntity#addToCenter},
   * {@link ClubAbstractEntity#createGenericStrikeLabel},
   * {@link ClubAbstractEntity#createGenericTextField},
   * {@link ClubAbstractEntity#createGenericRow})
   * 
   * @param id          The actual soldier personal id.
   * @param name        The name of the soldier
   * @param surname     The last name of the soldier
   * @param tel         The phone number of the soldier
   * @param personalNum The personal number of the soldier.
   */
  public Soldier(String id, String name, String surname, String tel, String personalNum) {
    // call father constructor
    super(id, name, surname, tel);
    // init personal num
    this.personalNum = personalNum;
    // create row
    personalNumField = createGenericTextField(personalNum);
    soldierStrike = createGenericStrikeLabel();
    // add the container to the center of the frame.
    addToCenter(createGenericRow("Personal No.", personalNumField, soldierStrike));
    // other frame initialization
    setSize(450, 250);
    setTitle("Soldier Clubber's Data");
  }

  /**
   * This method is responsible for the searching and matching (id and personal
   * num)
   * 
   * @param key the key to match to
   * @return true if the id or personalnum was matched to the key, else returns
   *         false.
   */
  public boolean match(String key) {
    return super.match(key) || personalNum.equals(key);
  }

  /**
   * This method is used to validate the fields of the soldiers window. It calls
   * the super's {@link Person#validateData} for the common fields of it and its
   * father. If it went successfuly than we validate the last field - personalNum.
   * if a validation of the format failed a red label will appear in the right
   * side of the textfield, if an existing validation failed a message will be
   * shows via {@link NightClubMgmtApp#existsValidate} method.
   * 
   * @return true if a validation went successfully and false otherwise.
   */
  protected boolean validateData() {
    soldierStrike.setVisible(false);
    if (!super.validateData())
      return false;
    String insertedPersonalNum = personalNumField.getText();
    // additional Check
    if (!(insertedPersonalNum.matches("[ROC]/[1-9]\\d{6}"))) {
      soldierStrike.setVisible(true);
      return false;
    }
    // exists Validation. (last validation) if passed returns true - else false
    return NightClubMgmtApp.existsValidate("Personal Number", insertedPersonalNum, this);
  }

  /**
   * commit simply calls the father's {@link Person#commit} which writes the text
   * fileds to the actual data base. than writes the soldier id to the data base.
   */
  protected void commit() {
    super.commit();
    personalNum = personalNumField.getText();
  }

  /**
   * This method is used for pressing cancel while changing a soldiers information
   * if cancel was pressed nothing should be saved - therefore this method is
   * invoked and makes sure that that the valid data that is already in the
   * database will be set back to the text fields. It uses the supers
   * {@link Person#rollBack} method.
   */
  protected void rollBack() {
    super.rollBack();
    personalNumField.setText(personalNum);
  }
}
