/*******************************************************************************
 * Copyright (c) 2007 MyGWT.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Darrell Meyer <darrell@mygwt.net> - initial API and implementation
 *******************************************************************************/
package net.mygwt.ui.client.viewer;

import java.util.HashMap;
import java.util.Map;

import net.mygwt.ui.client.MyDOM;
import net.mygwt.ui.client.util.Format;

/**
 * A label provider for a <code>TemplateViewer</code>. This label provider
 * differs from other label providers as it returns a HTML fragment to be used
 * to generate the element. Typically, index or key based substitution is used
 * to generate the HTML for each element.
 * 
 * @see TemplateViewer
 */
public abstract class TemplateLabelProvider implements IBaseLabelProvider {

  /**
   * The HTML fragment used to create the container element. Optionally, the
   * container element can be specified by assigning a class name of
   * "my-container" to the element. This allows the container to be decorated.
   * Default value is "&lt;div>&lt;&#47div>".
   */
  protected String containerTemplate = "<div class='my-container'></div>";

  /**
   * The HTML template used to render each element. All elements MUST be
   * assigned an id. Default value is "&lt;div id='{id}'>{text}&lt;&#47;div>".
   */
  protected String template = "<div id='{id}'>{text}</div>";

  /**
   * Creates a new label provider.
   */
  public TemplateLabelProvider() {

  }

  /**
   * Creates a new label provider with the given template.
   * 
   * @param template the template
   */
  public TemplateLabelProvider(String template) {
    this.template = template;
  }

  /**
   * Returns the style that is added to the selected element.
   * 
   * @return the select style
   */
  public abstract String getSelectStyle();

  /**
   * Returns the style that is added to a element under the mouse. Default
   * implementation returns <code>null</code>.
   * 
   * @return the over style
   */
  public String getOverStyle() {
    return null;
  }

  /**
   * Returns the value to be displayed in the form field for the element.
   * 
   * @param elem the element
   * @return the value
   */
  public String getValue(Object elem) {
    return elem.toString();
  }

  /**
   * Returns the template.
   * 
   * @return the template
   */
  public String getTemplate() {
    return template;
  }

  /**
   * Sets the template.
   * 
   * @param template the html template
   */
  public void setTemplate(String template) {
    this.template = template;
  }

  /**
   * Returns the HTML fragment used to create the container component.
   * Optionally, the container element can be specified by assigning a class
   * name of "my-container" to the element. This allows the container to be
   * decorated. Default value is "&lt;div>&lt;&#47div>".
   * 
   * @return the container html
   */
  protected String render() {
    return containerTemplate;
  }

  /**
   * Renders the current element.
   * 
   * @param elem the model element
   * @return the tempalte html
   */
  protected String renderElement(Object elem) {
    return Format.substitute(template, getParameters(elem));
  }

  /**
   * Returns the paramters to be used when rendering an element.
   * 
   * @param elem the element
   * @return the parameters
   */
  protected Map getParameters(Object elem) {
    Map params = new HashMap();
    params.put("id", MyDOM.getUniqueID());
    params.put("text", elem.toString());
    return params;
  }

}
