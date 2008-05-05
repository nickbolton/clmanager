/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package net.mygwt.ui.client.util;

import java.util.Iterator;
import java.util.Map;

/**
 * Formatting functions.
 */
public class Format {

  /**
   * Substitutes the indexed parameters.
   * 
   * @param text the text
   * @param param the parameter
   * @return the new text
   */
  public static String substitute(String text, int param) {
    return substitute(text, safeRegexReplacement("" + param));
  }

  /**
   * Substitutes the named parameters. The passed keys and values must be
   * Strings.
   * 
   * @param text the text
   * @param params the parameters
   * @return the new text
   */
  public static String substitute(String text, Map params) {
    Iterator it = params.keySet().iterator();
    while (it.hasNext()) {
      String key = (String) it.next();
      text = text.replaceAll("\\{" + key + "}",
          safeRegexReplacement((String) params.get(key)));
    }
    return text;
  }

  /**
   * Substitutes the indexed parameters.
   * 
   * @param text the text
   * @param param the parameter
   * @return the new text
   */
  public static String substitute(String text, String param) {
    return text.replaceAll("\\{0}", safeRegexReplacement(param));
  }

  /**
   * Substitutes the named parameter.
   * 
   * @param text the text
   * @param name the parameter name
   * @return the parameter value
   */
  public static String substitute(String text, String name, String value) {
    return text = text.replaceAll("\\{" + name + "}", safeRegexReplacement(value));
  }

  /**
   * Substitutes the indexed parameters.
   * 
   * @param text the text
   * @param params the parameters
   * @return the new text
   */
  public static String substitute(String text, String[] params) {
    for (int i = 0; i < params.length; i++) {
      String p = params[i];
      if (p == null) {
        p = "";
      }
      text = text.replaceAll("\\{" + i + "}", safeRegexReplacement(p));
    }
    return text;
  }

  /**
   * Substitutes the named parameters. The passed keys and values must be
   * Strings.
   * 
   * @param text the text
   * @param keys the parameter names
   * @param params the parameter values
   * @return the new text
   */
  public static String substitute(String text, String[] keys, Map params) {
    for (int i = 0; i < keys.length; i++) {
      text = text.replaceAll("\\{" + keys[i] + "}",
          safeRegexReplacement((String) params.get(keys[i])));
    }
    return text;
  }

  /**
   * Replace any \ or $ characters in the replacement string with an escaped \\
   * or \$.
   * 
   * @param replacement the regular expression replacement text
   * @return null if replacement is null or the result of escaping all \ and $
   *         characters
   */
  private static String safeRegexReplacement(String replacement) {
    if (replacement == null) {
      return replacement;
    }

    return replacement.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$");
  }
}
