package edu.elon.contact;

public class ContactApplication {

  public static void main(String[] args) {
    ContactModelInterface model = new ContactModel();
    ContactControllerInterface controller = new ContactController(model);
  }
}
