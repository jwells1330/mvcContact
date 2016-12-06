package edu.elon.contact;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ContactModelInterface {

  void registerObserver(ContactObserver o);

  void removeObserver(ContactObserver o);

  void notifyObservers(ArrayList<String> info);

  public ArrayList<String> getDBInfo();

  public Connection connectToDatabase(String connString, String userName, String passWord) throws SQLException;

  public void displayFirst(Connection conn) throws SQLException;

  public void displayNext(Connection conn, String email) throws SQLException;

  public void displayPrevious(Connection conn, String email) throws SQLException;

  public void deleteAll(Connection conn) throws SQLException;

  public void createNew(Connection conn, ArrayList<String> contactInfo) throws SQLException;

  public void deleteContact(Connection conn, ArrayList<String> contactInfo) throws SQLException;

  public void updateContact(Connection conn, ArrayList<String> contactInfo) throws SQLException;
}
