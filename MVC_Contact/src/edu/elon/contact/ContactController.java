package edu.elon.contact;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class ContactController implements ContactControllerInterface {

  ContactModelInterface model;
  ContactView view;

  private ArrayList<String> labelTextArray1 = new ArrayList<String>();
  private ArrayList<String> labelTextArray2 = new ArrayList<String>();
  private ArrayList<String> fieldTextArray1 = new ArrayList<String>();
  private ArrayList<String> fieldTextArray2 = new ArrayList<String>();
  // private ArrayList<String> fieldText
  private Map<String, String> buttonMap = new HashMap<String, String>();

  // private ArrayList<String> buttonTextArray = new ArrayList<String>();
  private boolean defaults = true;
  private Connection conn;

  public ContactController() {

  }

  public ContactController(ContactModelInterface model) {
    this.model = model;
    view = new ContactView(model, this);
    view.createFrame();

    createArrays();

    for (int i = 0; i < 5; i++) {
      view.setLabelText(i, labelTextArray1.get(i));
    }

    String buttonText = "Previous";
    view.makeButton(buttonText, buttonMap.get(buttonText), BorderLayout.LINE_END);
    buttonText = "Next";
    view.makeButton(buttonText, buttonMap.get(buttonText), BorderLayout.LINE_START);

    view.makeMenu(false);
  }

  public void createArrays() {
    String text = "First Name";
    labelTextArray1.add(text);
    text = "Middle Name";
    labelTextArray1.add(text);
    text = "Last Name";
    labelTextArray1.add(text);
    text = "Email";
    labelTextArray1.add(text);
    text = "Major";
    labelTextArray1.add(text);

    text = "Username";
    labelTextArray2.add(text);
    text = "Password";
    labelTextArray2.add(text);
    text = "IP Address";
    labelTextArray2.add(text);
    text = "DB Name";
    labelTextArray2.add(text);
    text = "Table Name";
    labelTextArray2.add(text);

    text = model.getDBInfo().get(0);
    fieldTextArray1.add(text);
    text = model.getDBInfo().get(1);
    fieldTextArray1.add(text);
    text = model.getDBInfo().get(2);
    fieldTextArray1.add(text);
    text = model.getDBInfo().get(3);
    fieldTextArray1.add(text);
    text = model.getDBInfo().get(4);
    fieldTextArray1.add(text);

    text = "Next";
    buttonMap.put(text, "doNext");
    text = "Previous";
    buttonMap.put(text, "doPrevious");
    text = "OK";
    buttonMap.put(text, "doConnection");
    text = "updateOK";
    buttonMap.put(text, "doAdd");
    text = "Cancel";
    buttonMap.put(text, "doCancel");
  }

  @Override
  public void closeUI() {
    view.close();
  }

  @Override
  public void connect() {
    view.removeButtons();
    for (int i = 0; i < 5; i++) {
      view.setLabelText(i, labelTextArray2.get(i));
    }
    if (defaults) {
      for (int i = 0; i < 5; i++) {
        view.setFieldText(i, fieldTextArray1.get(i));
      }
    } else {
      for (int i = 0; i < 5; i++) {
        view.setFieldText(i, fieldTextArray2.get(i));
      }
    }

    String buttonText = "OK";
    view.makeButton(buttonText, buttonMap.get(buttonText), BorderLayout.LINE_END);

  }

  @Override
  public void clearDB() {
    try {
      model.deleteAll(conn);
      for (int i = 0; i < 5; i++) {
        view.setFieldText(i, "");
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  public void add() {
    for (int i = 0; i < fieldTextArray1.size(); i++) {
      fieldTextArray1.remove(i);
      i--;
    }
    for (int i = 0; i < 5; i++) {
      fieldTextArray1.add(view.getFieldsArray().get(i).getText());
      view.setFieldText(i, "");
    }
    view.removeButtons();
    view.makeButton("OK", buttonMap.get("updateOK"), BorderLayout.LINE_START);
    String button = "Cancel";
    view.makeButton(button, buttonMap.get(button), BorderLayout.LINE_END);
  }

  @Override
  public void edit() {
    ArrayList<String> info = new ArrayList<String>();
    for (JTextField field : view.getFieldsArray()) {
      info.add(field.getText());
    }
    try {
      model.updateContact(conn, info);
      System.out.println("Updated");
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  public void remove() {
    ArrayList<String> info = new ArrayList<String>();
    for (JTextField field : view.getFieldsArray()) {
      info.add(field.getText());
    }
    try {
      model.deleteContact(conn, info);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void doConnection() {
    defaults = false;
    for (int i = 0; i < 5; i++) {
      fieldTextArray2.add(view.getFieldsArray().get(i).getText());
    }

    String connString = "jdbc:mysql://" + view.getFieldsArray().get(2).getText() + "/"
        + view.getFieldsArray().get(3).getText();

    try {
      conn = model.connectToDatabase(connString, view.getFieldsArray().get(0).getText(),
          view.getFieldsArray().get(1).getText());

      view.removeMenu();
      view.makeMenu(true);
      for (int i = 0; i < 5; i++) {
        view.setLabelText(i, labelTextArray1.get(i));
      }
      model.displayFirst(conn);
      view.removeButtons();
      String buttonText = "Previous";
      view.makeButton(buttonText, buttonMap.get(buttonText), BorderLayout.LINE_END);
      buttonText = "Next";
      view.makeButton(buttonText, buttonMap.get(buttonText), BorderLayout.LINE_START);

    } catch (SQLException e) {
      // e.printStackTrace();
      view.connectionToDBFailed();
    }
  }

  @Override
  public void doNext() {
    try {
      model.displayNext(conn, view.getFieldsArray().get(3).getText());
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  @Override
  public void doPrevious() {
    try {
      model.displayPrevious(conn, view.getFieldsArray().get(3).getText());
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  @Override
  public void doAdd() {
    ArrayList<String> info = new ArrayList<String>();
    for (JTextField field : view.getFieldsArray()) {
      info.add(field.getText());
    }
    try {
      model.createNew(conn, info);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    view.removeButtons();
    String buttonText = "Previous";
    view.makeButton(buttonText, buttonMap.get(buttonText), BorderLayout.LINE_END);
    buttonText = "Next";
    view.makeButton(buttonText, buttonMap.get(buttonText), BorderLayout.LINE_START);

    for (int i = 0; i < 5; i++) {
      view.setFieldText(i, fieldTextArray1.get(i));
    }

  }

  @Override
  public void doCancel() {
    view.removeButtons();
    String buttonText = "Previous";
    view.makeButton(buttonText, buttonMap.get(buttonText), BorderLayout.LINE_END);
    buttonText = "Next";
    view.makeButton(buttonText, buttonMap.get(buttonText), BorderLayout.LINE_START);

    for (int i = 0; i < 5; i++) {
      view.setFieldText(i, fieldTextArray1.get(i));
    }
  }
}