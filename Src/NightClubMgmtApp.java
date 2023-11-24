import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 * NightClubMgmtApp - this class contains the main. its job is to control the
 * first user interaction with the program(the main program window); it extends
 * {@link javax.swing.JFrame} and iniitializes the neccessary gui components for
 * the frame [{@link javax.swing.JComboBox}- the entity choices,
 * {@link java.util.ArrayList}- the array list of the clubbers] this class will
 * also be responsible for reading and writing the clubbers from and into the
 * file via the {@link #loadClubbersDBFromFile} and
 * {@link #writeClubbersDBtoFile} methods.
 */
public class NightClubMgmtApp extends JFrame {
  // Night-Club Regular Customers Repository
  /** clubbers - The ArrayList of the clubbers */
  private static ArrayList<ClubAbstractEntity> clubbers;

  /**
   * empty constructor - will initialize the clubbers array afterward it will load
   * the clubbers from the file via {@link #loadClubbersDBFromFile} method. It
   * will then create a {@link javax.swing.JPanel} container and add new
   * {@link javax.swing.JButton}'s to it ["add"- creates a new entity using
   * {@link #createEntity}, "search"- uses {@link #manipulateDB}], in this
   * proccess it will also wire an "anonymous ActionListener" handler to the
   * buttons that tell it what to do when it is pressed. then it will initialize
   * the entity choices combobox. In the end it takes care of the rest of the gui
   * initializaton needed for this window.
   */
  public NightClubMgmtApp() {
    clubbers = new ArrayList<>();
    // read the clubbers saved in the file
    loadClubbersDBFromFile();
    // string for user choices
    String[] choices = { "Person", "Student", "Soldier" };
    // create the gui components.
    JButton searchbtn = new JButton("SEARCH");
    JButton addbtn = new JButton("Add New Entity");
    JComboBox<String> entityChoices = new JComboBox<>(choices);
    // set the handlers wiring
    wireHandlers(searchbtn, addbtn, entityChoices);
    // add components to containers
    JPanel searchContainer = new JPanel();
    searchContainer.add(searchbtn);
    JPanel addChoicesContainer = new JPanel();
    addChoicesContainer.add(addbtn);
    addChoicesContainer.add(entityChoices);
    // add containers to the frame
    add(addChoicesContainer);
    add(searchContainer, BorderLayout.SOUTH);
    // set frame settings
    setTitle("Night Club Management");
    setSize(380, 200);
    setLocationRelativeTo(null);
    setResizable(false);
    setVisible(true);
  }

  /**
   * This private method wires all the handlers to their gui components using
   * anonymous listeners: the add (invokes {@link #createEntity}) and search
   * (invokes {@link #manipulateDB}) buttons and the window itself (using
   * {@link java.awt.event.WindowAdapter}, invokes
   * {@link #writeClubbersDBtoFile}).
   * 
   * @param search        The "search" button.
   * @param add           The "add" button.
   * @param entityChoices The combo box with the choices to wire to the "add"
   *                      button.
   */
  private void wireHandlers(JButton search, JButton add, JComboBox<String> entityChoices) {
    // wire search button
    search.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        manipulateDB();
      }
    });
    // wire add button
    add.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ClubAbstractEntity entity = createEntity(entityChoices.getSelectedIndex());
        // check if entity was created
        if (entity != null) {
          clubbers.add(entity);
          // entity.enableCancelBtn(false);
          entity.setVisible(true);
        }
      }
    });
    // wire a window listener for writing clubbers to file.
    // when closing the window
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        writeClubbersDBtoFile();
        System.exit(0);
      }
    });
  }

  /**
   * This public static method is used to validate the existance of an entitys
   * id/studentid/personalnum using the existing implemented abstract method
   * {@link ClubAbstractEntity#match} of each inheriter of @{link
   * ClubAbstractEntity}
   * [{@link Person#match},{@link Student#match},{@link Soldier#match}]
   * 
   * @param keyName    The key name to be concatinated to the error message string
   * @param key        a key to match by.
   * @param newClubber a new clubber to skip his match so we find another one
   *                   except his (also prevents
   *                   {@link StringIndexOutOfBoundsException} that
   *                   {@link String#substring} throws - occures when trying to
   *                   match studentID to the new empty entity that we are adding)
   * @return true if validation went successfully, false otherwise.
   */
  public static boolean existsValidate(String keyName, String key, ClubAbstractEntity newClubber) {
    for (ClubAbstractEntity clubber : clubbers)
      if (clubber != newClubber && clubber.match(key)) {
        String str = "The " + keyName + " '" + key + "' already exists in the system!\nPlease enter another...";
        JOptionPane.showMessageDialog(newClubber, str, "Notice", JOptionPane.INFORMATION_MESSAGE);
        return false;
      }
    return true;
  }

  /**
   * This method is used to find a clubber by a key given to it it uses each
   * clubbers {@link ClubAbstractEntity#match} method.
   * 
   * @param key - A String given to it to check by
   * @return The clubber itself, if was found. otherwise returns null
   */
  private ClubAbstractEntity findClubber(String key) {
    for (ClubAbstractEntity clubber : clubbers)
      if (clubber.match(key)) {
        return clubber;
      }
    return null;
  }

  /**
   * Private mehtod used to create a new entity.
   * 
   * @param idx The index that was given by the user choice in the combobox.
   * @return A new entity based on the index that was given to it, if failed
   *         returns null.
   */
  private ClubAbstractEntity createEntity(int idx) {
    switch (idx) {
      case 0:
        return new Person();
      case 1:
        return new Student();
      case 2:
        return new Soldier();
      default:
        return null;
    }
  }

  /**
   * manipulateDB - used to control the searching mechanism. it asks the user of
   * an id and looks for it inside the clubbers array using the
   * {@link #findClubber} method that uses each clubbers match function
   * [{@link Person#match}, {@link Student#match}, {@link Soldier#match}]
   * polymorphicly
   */
  private void manipulateDB() {
    String input;
    while (true) {
      input = JOptionPane.showInputDialog(this, "Please Enter The Clubber's Key ");
      // break if cancel button was pressed
      if (input == null)
        break;
      ClubAbstractEntity clubber = findClubber(input);
      if (clubber != null) {
        clubber.setVisible(true);
        break;
      } else
        JOptionPane.showMessageDialog(this, "Clubber with key " + input + " does not exist", "Notice",
            JOptionPane.INFORMATION_MESSAGE);
    }
  }// End of method manipulateDB

  /**
   * Loads the clubbers from file via {@link java.io.ObjectInputStream} and
   * {@link java.io.FileInputStream}, will cast the readen object to arraylist of
   * {@link ClubAbstractEntity}. Afterwards it will search for empty clubbers via
   * {@link #findClubber} method and deletes them (this can happen by mistake when
   * closing the main window while adding a new entity (or several at once)).
   * Notice that it throws {@link java.io.FileNotFoundException} and
   * {@link ClassNotFoundException}
   */
  private void loadClubbersDBFromFile() {
    // Read data from file, create the corresponding objects and put them
    // into clubbers ArrayList.
    try {
      ObjectInputStream ois = new ObjectInputStream(new FileInputStream("BKCustomers.dat"));
      clubbers = (ArrayList<ClubAbstractEntity>) ois.readObject();
      ois.close();
    } catch (FileNotFoundException e) {
      JOptionPane.showMessageDialog(this,
          "Something went wrong...\nNo data base file was found.\nAll your new data will be stored in a new file.",
          "DB File wasn't found", JOptionPane.INFORMATION_MESSAGE);
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    // removes any empty entity if there is any...
    ClubAbstractEntity toRemove;
    while (true) {
      toRemove = findClubber("");
      if (toRemove != null)
        clubbers.remove(toRemove);
      else
        break;
    }
  }

  /**
   * Writes the clubbers arraylist to file using
   * {@link java.io.ObjectOutputStream} and {@link java.io.FileOutputStream}.
   * Notice that it could throw a {@link java.io.FileNotFoundException}
   */
  private void writeClubbersDBtoFile() {
    // Write all the objects’ data in clubbers ArrayList into the file
    try {
      ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(("BKCustomers.dat")));
      o.writeObject(clubbers);
      o.flush();
      o.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * main function simply creates a new NightClubMgmtApp to get the application
   * start running.
   * 
   * @param args
   */
  public static void main(String[] args) {
    NightClubMgmtApp application = new NightClubMgmtApp();
  }
}// End of class NightClubMgmtApp