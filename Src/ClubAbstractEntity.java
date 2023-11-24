import java.awt.event.*;
import java.io.Serializable;
import javax.swing.*;
import java.awt.*;

/**
 * ClubAbstractEntity is an abstract class needed for the abstract entity of the
 * program. every entity will inherit from this class [Examples: {@link Person},
 * {@link Student}, {@link Soldier}]. It also declares some abstract methods to
 * be implemented in each entity of the program [{@link #match},
 * {@link #validateData}, {@link #commit}, {@link #rollBack}]. It will also
 * handle the basic gui initialization using {@link javax.swing.JFrame}- the
 * frame itself, {@link javax.swing.JPanel}- the center panel and buttons
 * container, {@link javax.swing.JButton} - the "OK"/"Cancel" buttons. Will also
 * disable the cancel button until validate and commit when we hit ok -
 * {@link ButtonsHandler}
 */
public abstract class ClubAbstractEntity extends JFrame implements Serializable {
  /**
   * okButton - if pressed it will validate the data via {@link #validateData}
   */
  private JButton okButton;
  /** cancelButton - if pressed it will perfom a rollback */
  private JButton cancelButton;
  /**
   * centerPanel - a {@link javax.swing.JPanel} to contain the elements in the
   * center
   */
  private JPanel centerPanel;

  /**
   * Parameterless constructor - initializes the gui container component which is
   * needed for each new entity in the system. It will also initialize the buttons and
   * will wire them to the handler. {@link ClubAbstractEntity.ButtonsHandler} hanler -
   * this is a handler to control the buttons pressing and its behavior
   */
  public ClubAbstractEntity() {
    // create the center panel- set its layout to right allignment.
    centerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    okButton = new JButton("OK");
    cancelButton = new JButton("Cancel");
    // set cancel button disabled for a new entity
    cancelButton.setEnabled(false);
    // add the components to their positions.
    JPanel buttonsPanel = new JPanel();
    buttonsPanel.add(okButton);
    buttonsPanel.add(cancelButton);
    add(buttonsPanel, BorderLayout.SOUTH);
    add(centerPanel);
    // create a new buttons handler and register the buttons
    ButtonsHandler handler = new ButtonsHandler();
    okButton.addActionListener(handler);
    cancelButton.addActionListener(handler);
    // other frame initialization
    setResizable(false);
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
  }

  /**
   * abstract method to control the matching machanism after Searching for an
   * entity - returns true if found and false if wasnt found.
   * 
   * @param key a key to match to
   * @return retuns true if a match was found - false otherwise
   */
  public abstract boolean match(String key);

  /**
   * abstract method to be implemented in the inheriters of this class. Used to
   * validate the new data that is inserted.
   * 
   * @return true if the validation went succesffully, false otherwise.
   */
  protected abstract boolean validateData();

  /** Used after the validation process ended succesfuly. */
  protected abstract void commit();

  /** rollBack is invoked if we started to add an entity and wanted to go back. */
  protected abstract void rollBack();

  /**
   * a protected void method to allow the inheriters to add gui components to the
   * center it recieves a gui component and adds it to the center of itself.
   * 
   * @param guiComponent A component to add to the center
   */
  protected void addToCenter(Component guiComponent) {
    centerPanel.add(guiComponent);
  }

  /**
   * This protected method is used to create a row within a container. It should
   * be used in every constructor that inherites from this class (that uses rows -
   * - meaning every new entity in the system that has the rows in it.
   * Makes less duplicated code and keeps the arrangement consistant.
   * 
   * @param labelName   the label name in the left side of the row
   * @param textfield   the textfield
   * @param strikeLabel the premade strike label - by
   *                    {@link #createGenericStrikeLabel}
   * @return A container that contains a valid row for our gui.
   */
  protected JPanel createGenericRow(String labelName, JTextField textfield, JLabel strikeLabel) {
    JPanel container = new JPanel();
    container.add(new JLabel(labelName));
    container.add(textfield);
    container.add(strikeLabel);
    return container;
  }

  /**
   * Each and every generic text field is defined the same for all the inheriters,
   * therefore this method was created, to make it easy to add new textfield that
   * will stay consistent throughout the system.
   * 
   * @param value the value to be written in the text field
   * @return a new textfield with the value and size 30
   */
  protected JTextField createGenericTextField(String value) {
    return new JTextField(value, 30);
  }

  /**
   * Protected method to create a new strike label, paint it to red and sets its
   * visible to be false.
   * Each and every strikeLabel is the same throughout every row in the inheriters
   * of this class.
   * 
   * @return a new strike label
   */
  protected JLabel createGenericStrikeLabel() {
    JLabel strike = new JLabel("*");
    strike.setForeground(Color.red);
    strike.setVisible(false);
    return strike;
  }

  /**
   * This is the ButtonsHandler - used to wire the "okButton" and "cancelButton".
   * if ok was pressed it calls {@link #validateData} - if the validation ended
   * successfuly then the {@link #commit} method is invoked and the cancelButton
   * will be enabled again (needed for new entitys). otherwise the otherwise the
   * "cancelButton" was pressed and a {@link #rollBack} will be made. these
   * methods will be invoked for each of the inheriters of this class.
   * 
   */
  private class ButtonsHandler implements ActionListener, Serializable {
    public void actionPerformed(ActionEvent event) {
      if (event.getSource() == okButton) {
        if (validateData()) {
          commit();
          // set cancel button enabled after commiting a new entity
          cancelButton.setEnabled(true);
          setVisible(false);
        }
      } else {
        rollBack();
        setVisible(false);
      }
    }
  }
}
