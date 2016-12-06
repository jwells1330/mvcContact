package edu.elon.contact;

/**
 * 
 * Executes the application to start it
 * 
 * @author mthompson31
 * @author jwells8
 * 
 * Copyright (c) 2016 by Jacob Wells and Mitchell Thompson
 *
 */
public class ContactApplication {

	/**
	 * Starts the application by making a new model and assigning
	 * it to the controller
	 * @param args console arguments
	 */
  public static void main(String[] args) {
    ContactModelInterface model = new ContactModel();
    ContactControllerInterface controller = new ContactController(model);
  }
}
