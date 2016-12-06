package edu.elon.contact;

import java.util.ArrayList;

/**
 * 
 * Observer interface for Contact application
 * 
 * @author mthompson31
 * @author jwells8
 * 
 * Copyright (c) 2016 by Jacob Wells and Mitchell Thompson
 *
 */
public interface ContactObserver {

  void update(ArrayList<String> contactInfo);

}
