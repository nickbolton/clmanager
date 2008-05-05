/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.messages;

import com.google.gwt.i18n.client.Messages;

/**
 * MyGWT messages.
 */
public interface MyMessages extends Messages {

  String yes();

  String no();

  String ok();

  String cancel();

  String sortAsc();

  String sortDesc();

  String columns();

  /* Themes */

  String blueTheme();

  String grayTheme();

  /* PagingToolbar */

  String beforePage();

  String afterPage(int pages);

  String firstPage();

  String previousPage();

  String nextPage();

  String lastPage();

  String refresh();

  String displaying(int start, int end, int total);

  String emptyMsg();

  /* Fields */
  String invalid();

  String minLength(int length);

  String maxLength(int length);

  String blank();

  /* loading panel */

  String loading();

}
