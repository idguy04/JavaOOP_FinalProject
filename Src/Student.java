import javax.swing.*;

/**
 * Student - this class extends the {@link Person} Class. It inherites all its
 * basic behaviour and adds another field to it- which are neccessary for the
 * Student.
 */
public class Student extends Person {
  /** studentID - actual studentID */
  private String studentID;
  /** studentIDfield - Textfield to hold the ID */
  private JTextField studentIDfield;
  /**
   * studentStrike - label for showing the error if the {@link #validateData}
   * failed.
   */
  private JLabel studentStrike;

  /**
   * empty constructor that uses the 5 parameter constructor for initialization of
   * a new empty entity.
   */
  public Student() {
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
   * @param id        the actual person id of the student
   * @param name      the name of the student
   * @param surname   the surname of the student
   * @param tel       the phone number of the student
   * @param studentID the studentid of the student
   */
  public Student(String id, String name, String surname, String tel, String studentID) {
    // call father constructor
    super(id, name, surname, tel);
    // init studentid
    this.studentID = studentID;
    // create row
    studentIDfield = createGenericTextField(studentID);
    studentStrike = createGenericStrikeLabel();
    addToCenter(createGenericRow("Student ID", studentIDfield, studentStrike));
    // set the title and size of the window.
    setSize(450, 250);
    setTitle("Student Clubber's Data");
  }

  /**
   * Match- will tell us if a given key matches this students actual ID or his
   * StudentID (starting from the 5th index).
   */
  public boolean match(String key) {
    return super.match(key) || key.equals(studentID.substring(4));
  }

  /**
   * This method is used to validate the fields of the students window. It calls
   * the super's {@link Person#validateData} for the common fields of it and its
   * father. If it went successfuly than we validate the last field - studetID. If
   * a validation of the format failed a red label will appear in the right side
   * of the textfield, if an existing validation failed a message will be shows
   * via {@link NightClubMgmtApp#existsValidate} method.
   * 
   * @return true if a validation went successfully and false otherwise.
   */
  protected boolean validateData() {
    studentStrike.setVisible(false);
    if (!super.validateData())
      return false;
    // additional pattern validation
    if (!(studentIDfield.getText().matches("[A-Z]{3}/[1-9]\\d{4}"))) {
      studentStrike.setVisible(true);
      return false;
    }
    // exists Validation. (last validation) if passed returns true - else false
    String insertedStudentID = studentIDfield.getText().substring(4);
    return NightClubMgmtApp.existsValidate("Student ID", insertedStudentID, this);
  }

  /**
   * commit simply calls the father's {@link Person#commit} which writes the text
   * fileds to the actual data base. than writes the student id to the data base.
   */
  protected void commit() {
    super.commit();
    studentID = studentIDfield.getText();
  }

  /**
   * This method is used for pressing cancel while changing a students information
   * if cancel was pressed nothing should be saved - therefore this method is
   * invoked and makes sure that that the valid data that is already in the
   * database will be set back to the text fields. It uses the supers
   * {@link Person#rollBack} method.
   */
  protected void rollBack() {
    super.rollBack();
    studentIDfield.setText(studentID);
  }
}
