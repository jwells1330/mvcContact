package edu.elon.contact;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * the contact model implements the database and other functional features of the application
 * such as registering observers.
 * 
 * @author mthompson31
 * @author jwells8
 * 
 * Copyright (c) 2016 by Jacob Wells and Mitchell Thompson
 * 
 * @version 1.0
 *
 */
public class ContactModel implements ContactModelInterface {

  ArrayList<ContactObserver> contactObservers = new ArrayList<ContactObserver>();
  ArrayList<String> defaultDBInfo = new ArrayList<String>();
  ArrayList<String> info;

  /**
   * adds info for database upon instantiation
   */
  public ContactModel() {
    defaultDBInfo.add("root");
    defaultDBInfo.add("Tw0C0ins");
    defaultDBInfo.add("localhost");
    defaultDBInfo.add("contactBook");
    defaultDBInfo.add("contact");
  }
  
  /**
   * returns a String ArrayList representing parameters like address of database/password
   */
  public ArrayList<String> getDBInfo() {
    return defaultDBInfo;
  }

  /**
   * 
   */
  @Override
  public void registerObserver(ContactObserver o) {
    contactObservers.add(o);
  }

  @Override
  public void removeObserver(ContactObserver o) {
    if (contactObservers.contains(0)) {
      contactObservers.remove(o);
    }
  }

  @Override
  public void notifyObservers(ArrayList<String> info) {
    for (ContactObserver o : contactObservers) {
      o.update(info);
    }
  }

  /**
   * creates connection to sql database
   */
  public Connection connectToDatabase(String connString, String userName, String passWord) throws SQLException {

    Connection conn = null;
    conn = DriverManager.getConnection(connString, userName, passWord);

    return conn;

  }

  /**
   * displays the first entry in the database upon opening application
   */
  public void displayFirst(Connection conn) throws SQLException {
    Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
    ResultSet rs = stmt.executeQuery("SELECT * FROM contact");
    rs.next();

    info = new ArrayList<String>();
    info.add(rs.getString(2));
    info.add(rs.getString(3));
    info.add(rs.getString(4));
    info.add(rs.getString(5));
    info.add(rs.getString(6));

    notifyObservers(info);
    rs.close();
    stmt.close();

  }

  /**
   * iterates and displays subsequent entries in database
   */
  @Override
  public void displayNext(Connection conn, String email) throws SQLException {
    Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
    ResultSet rs = stmt.executeQuery("SELECT * FROM contact");
    rs.next();
    while (!rs.getString(5).equals(email)) {
      rs.next();
    }
    if (rs.next()) {
      rs.previous();
      rs.next();
    }
    if (rs.isAfterLast()) {
      rs.previous();
    }

    info = new ArrayList<String>();
    info.add(rs.getString(2));
    info.add(rs.getString(3));
    info.add(rs.getString(4));
    info.add(rs.getString(5));
    info.add(rs.getString(6));

    notifyObservers(info);
    rs.close();
    stmt.close();
  }

  /**
   * iterates and displays previous entries in database
   */
  @Override
  public void displayPrevious(Connection conn, String email) throws SQLException {
    Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
    ResultSet rs = stmt.executeQuery("SELECT * FROM contact");
    rs.next();
    while (!rs.getString(5).equals(email)) {
      rs.next();
    }
    if (rs.previous()) {
      rs.next();
      rs.previous();
    }
    if (rs.isBeforeFirst()) {
      rs.next();
    }

    info = new ArrayList<String>();
    info.add(rs.getString(2));
    info.add(rs.getString(3));
    info.add(rs.getString(4));
    info.add(rs.getString(5));
    info.add(rs.getString(6));

    notifyObservers(info);
    rs.close();
    stmt.close();

  }

  /**
   * deletes all entries in the database
   */
  @Override
  public void deleteAll(Connection conn) throws SQLException {
    Statement stmt = conn.createStatement();
    stmt.executeUpdate("DELETE FROM CONTACT");
    stmt.close();
  }

  /**
   * creates a new database entry
   */
  @Override
  public void createNew(Connection conn, ArrayList<String> contactInfo) throws SQLException {
    String statement = "INSERT INTO contact (FirstName, MiddleName, LastName, Email, Major) VALUES" + "(?,?,?,?,?)";

    PreparedStatement ps = conn.prepareStatement(statement);

    for (int i = 0; i < 5; i++) {
      ps.setString(i + 1, contactInfo.get(i));
    }

    ps.executeUpdate();
    ps.close();

  }

  /**
   * deletes a specific entry from the contact database
   */
  @Override
  public void deleteContact(Connection conn, ArrayList<String> contactInfo) throws SQLException {
    String sql = "DELETE FROM contact WHERE email = ?";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setString(1, contactInfo.get(3));

    Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
    ResultSet rs = stmt.executeQuery("SELECT * FROM contact");
    rs.next();
    while (!rs.getString(5).equals(contactInfo.get(3))) {
      rs.next();
    }
    if (rs.next()) {
      System.out.println("test");
      rs.previous();
      displayNext(conn, contactInfo.get(3));
      ps.executeUpdate();
    } else {
      rs.previous();
      displayPrevious(conn, contactInfo.get(3));
      ps.executeUpdate();

    }

    rs.close();
    stmt.close();
    ps.close();

  }

  /**
   * updates a specific entry in the contact database
   */
  @Override
  public void updateContact(Connection conn, ArrayList<String> contactInfo) throws SQLException {
    String sql = "UPDATE contact set FirstName = ?, MiddleName = ?, LastName = ?, Email = ?, Major = ?"
        + " WHERE FirstName = ? OR MiddleName = ? OR LastName = ? OR Email = ?";
    PreparedStatement ps = conn.prepareStatement(sql);

    for (int i = 0; i < 5; i++) {
      ps.setString(i + 1, contactInfo.get(i));
    }
    for (int i = 0; i < 4; i++) {
      ps.setString(i + 6, contactInfo.get(i));
    }
    ps.executeUpdate();
  }

}
