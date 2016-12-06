package edu.elon.contact;

import java.awt.event.ActionEvent;


/**
 * 
 * Interface for controller to use
 * 
 * @author mthompson31
 * @author jwells8
 * 
 * Copyright (c) 2016 by Jacob Wells and Mitchell Thompson
 *
 */
public interface ContactControllerInterface {

  public void createArrays();

  public void closeUI();

  public void connect();

  public void clearDB();

  public void add();

  public void edit();

  public void remove();

  public void doConnection();

  public void doNext();

  public void doPrevious();

  public void doAdd();

  public void doCancel();

}
