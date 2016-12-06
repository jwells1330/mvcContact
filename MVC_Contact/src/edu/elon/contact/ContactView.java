package edu.elon.contact;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Creates the view for the application. Does not do any
 * functional work with the database.
 * 
 * @author mthompson31
 * @author jwells8
 * 
 * @version 1.0
 * 
 * Copyright (c) 2016 by Jacob Wells and Mitchell Thompson
 *
 */
public class ContactView extends JFrame implements ContactObserver {

  ContactModelInterface model;
  ContactControllerInterface controller;

  private ArrayList<JLabel> labels = new ArrayList<JLabel>();
  private ArrayList<JTextField> fields = new ArrayList<JTextField>();

  private JFrame mainFrame;
  private JPanel buttonPanel;
  private JMenuBar bar;

  /**
   * 
   * Creates a new ContactView with a new instance of Model and 
   * a ControllerInterface
   * 
   * @param model the object representing 
   * the model for the application
   * @param controller the object representing 
   * the controller for the application
   */
  public ContactView(ContactModelInterface model, ContactControllerInterface controller) {
    this.model = model;
    this.controller = controller;
    model.registerObserver((ContactObserver) this);
  }

  /**
   * creates the main frame of the application
   */
  public void createFrame() {
    mainFrame = new JFrame("Contact Display View");
    mainFrame.setSize(400, 200);
    mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    mainFrame.getContentPane().setBackground(Color.LIGHT_GRAY);

    JPanel labelPanel = new JPanel();
    labelPanel.setLayout(new GridLayout(5, 1));

    for (int i = 0; i < 5; i++) {
      JLabel label = new JLabel();
      labels.add(label);
      labelPanel.add(label);
    }

    JPanel textPanel = new JPanel();
    textPanel.setLayout(new GridLayout(5, 1));
    for (int i = 0; i < 5; i++) {
      JTextField textField = new JTextField();
      fields.add(textField);
      textPanel.add(textField);
    }

    buttonPanel = new JPanel();

    mainFrame.add(textPanel, BorderLayout.CENTER);
    mainFrame.add(labelPanel, BorderLayout.LINE_START);
    mainFrame.setVisible(true);
  }

  


  /**
   * Makes buttons for the application view. Allows dynamic
   * button creation for updating
   * based on which screen we are on. 
   * 
   * @param text the text for the button
   * @param methodName the method the button it should call
   * @param constraints an object representing constraints for layout work
   */
  public void makeButton(String text, String methodName, Object constraints) {
    JButton button = new JButton(text);
    button.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        Class c = controller.getClass();
        try {
          Method method = c.getMethod(methodName, null);
          method.invoke(controller, null);
        } catch (Exception e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }

      }
    });
    buttonPanel.add(button, constraints);

    mainFrame.add(buttonPanel, BorderLayout.PAGE_END);
    mainFrame.setVisible(true);
  }

  /**
   * makes a menu on the menu bar
   * @param enabled whether or not the menu should be visible
   */
  public void makeMenu(boolean enabled) {
    bar = new JMenuBar();
    JMenu menu = new JMenu("File");
    menu.setMnemonic(KeyEvent.VK_F);

    JMenuItem menuItem = new JMenuItem("Clear DB", KeyEvent.VK_C);
    menuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        controller.clearDB();
      }
    });
    menuItem.setEnabled(enabled);
    menu.add(menuItem);
    menuItem = new JMenuItem("Connect", KeyEvent.VK_T);
    menuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        controller.connect();
      }
    });
    menu.add(menuItem);
    menuItem = new JMenuItem("Exit", KeyEvent.VK_X);
    menuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        controller.closeUI();
      }
    });
    menu.add(menuItem);

    bar.add(menu, BorderLayout.WEST);

    menu = new JMenu("Edit");
    menu.setMnemonic(KeyEvent.VK_E);

    menuItem = new JMenuItem("Add", KeyEvent.VK_A);
    menuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        controller.add();
      }
    });
    menuItem.setEnabled(enabled);
    menu.add(menuItem);
    menuItem = new JMenuItem("Remove", KeyEvent.VK_R);
    menuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        controller.remove();
      }
    });
    menuItem.setEnabled(enabled);
    menu.add(menuItem);
    menuItem = new JMenuItem("Update", KeyEvent.VK_U);
    menuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        controller.edit();
      }
    });
    menuItem.setEnabled(enabled);
    menu.add(menuItem);

    bar.setEnabled(true);
    bar.add(menu, BorderLayout.CENTER);

    mainFrame.add(bar, BorderLayout.PAGE_START);
    mainFrame.setVisible(true);
  }

  /**
   * opens a pane with error message should the connection fail
   */
  public void connectionToDBFailed() {
    JOptionPane.showMessageDialog(null, "You did not correctly"
    		+ " specify DB parameters", "alert",
        JOptionPane.ERROR_MESSAGE);
  }

  /**
   * removes all buttons
   */
  public void removeButtons() {
    buttonPanel.removeAll();
  }

  /**
   * removes the menu from menu bar
   */
  public void removeMenu() {
    mainFrame.remove(bar);
  }

  /**
   * sets label text for buttons
   * @param labelNum index of label
   * @param labelText the String representing the label text
   */
  public void setLabelText(int labelNum, String labelText) {
    labels.get(labelNum).setText(labelText);
  }

  /**
   * 
   * @param fieldNum
   * @param fieldText
   */
  public void setFieldText(int fieldNum, String fieldText) {
    fields.get(fieldNum).setText(fieldText);
  }

  /**
   * 
   * @return an ArrayList of JLabels 
   * representing the labels of the menus
   */
  public ArrayList<JLabel> getLabelsArray() {

    return labels;
  }

  public ArrayList<JTextField> getFieldsArray() {
    return fields;
  }

  /**
   * closes the frame
   */
  public void close() {
    mainFrame.dispose();
  }
  
  /**
   * update method for observer pattern
   */
  @Override
  public void update(ArrayList<String> contactInfo) {

    for (int i = 0; i < 5; i++) {
      setFieldText(i, contactInfo.get(i));
    }

  }
}
