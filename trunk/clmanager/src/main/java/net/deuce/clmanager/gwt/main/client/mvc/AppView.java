package net.deuce.clmanager.gwt.main.client.mvc;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.Registry;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.mvc.AppEvent;
import net.mygwt.ui.client.mvc.Controller;
import net.mygwt.ui.client.mvc.Dispatcher;
import net.mygwt.ui.client.widget.ContentPanel;
import net.mygwt.ui.client.widget.ExpandBar;
import net.mygwt.ui.client.widget.ExpandItem;
import net.mygwt.ui.client.widget.SearchableExpandItem;
import net.mygwt.ui.client.widget.ThemeSelector;
import net.mygwt.ui.client.widget.Viewport;
import net.mygwt.ui.client.widget.WidgetContainer;
import net.mygwt.ui.client.widget.layout.BorderLayout;
import net.mygwt.ui.client.widget.layout.BorderLayoutData;
import net.mygwt.ui.client.widget.layout.FillLayout;
import asquare.gwt.debug.client.DebugConsole;

public class AppView extends BaseView {

    private Viewport viewport;
    private ContentPanel west;
    private ContentPanel east;
    private WidgetContainer main;
    private WidgetContainer mainContainer;
    private ContentPanel center;
    private ContentPanel focus;
    private WidgetContainer south;
    private boolean centerFocused = false;

    private ExpandBar expandBar;

    public AppView(Controller controller) {
      super(controller);
    }

    protected void initialize() {
      viewport = new Viewport();
      viewport.setStyleName("my-border-layout");
      viewport.setLayout(new BorderLayout());
      
      mainContainer = new WidgetContainer();
      mainContainer.setLayout(new BorderLayout());
      viewport.add(mainContainer, new BorderLayoutData(Style.CENTER));
      
      BorderLayoutData westData = new BorderLayoutData(Style.WEST, 200, 150, 350);
      
      east = new ContentPanel();
      east.setText("Photos");
      east.setLayout(new FillLayout());
      mainContainer.add(east, new BorderLayoutData(Style.EAST, 300, 150, 250));

      west = new ContentPanel(Style.HEADER);
      west.setText("CLManager");
      west.getHeader().setStyleAttribute("borderBottom", "none");
      createExpandBar();
      west.setLayout(new FillLayout());
      west.add(expandBar);

      mainContainer.add(west, westData);

      main = new WidgetContainer();
      BorderLayout layout = new BorderLayout();
      layout.setMargin(0);
      main.setLayout(layout);
      
      focus = new ContentPanel(Style.HEADER);
      focus.getHeader().addWidget(new ThemeSelector());
      focus.setLayout(new FillLayout());

      center = new ContentPanel(Style.HEADER);
      center.getHeader().addWidget(new ThemeSelector());
      center.setLayout(new FillLayout());

      south = new WidgetContainer();
      south.setLayout(new FillLayout());
      south.setBorders(true);
      south.setStyleAttribute("backgroundColor", "white");

      main.add(center, new BorderLayoutData(Style.CENTER));
      main.add(south, new BorderLayoutData(Style.SOUTH, .10f, 50, 2000));

      mainContainer.add(main, new BorderLayoutData(Style.CENTER));

      viewport.add(DebugConsole.getInstance(), new BorderLayoutData(Style.SOUTH, .33f, 50, 2000));
      
      viewport.layout();
      viewport.hideLoadingPanel("loading");

      Registry.register("west", west);
      Registry.register("center", center);
      Registry.register("south", south);
      Registry.register("east", east);
      Registry.register("focus", focus);
      
    }
    
    public void focusCenter() {
        if (!centerFocused) {
            mainContainer.remove(east);
            mainContainer.remove(main);
            mainContainer.add(focus, new BorderLayoutData(Style.CENTER));
            mainContainer.layout(true);
            centerFocused = true;
        }
    }
    
    public void unfocusCenter() {
        if (centerFocused) {
            mainContainer.remove(focus);
            mainContainer.add(east, new BorderLayoutData(Style.EAST, 300, 150, 250));
            mainContainer.add(main, new BorderLayoutData(Style.CENTER));
            centerFocused = false;
            mainContainer.layout(true);
        }
    }

    protected void handleEvent(AppEvent event) {
        switch (event.type) {
        case AppEvents.Init:
            //ExpandItem item = expandBar.getItem(0);
            //expandBar.setExpanded(item, true);
            break;
        case AppEvents.FocusCenter:
            focusCenter();
            break;
        case AppEvents.UnfocusCenter:
            unfocusCenter();
            break;
        }
    }

    private void createExpandBar() {
      expandBar = new ExpandBar(Style.SINGLE | Style.HEADER);
      expandBar.setBorders(false);
      expandBar.setHeaderHeight(28);
      
      ExpandItem citiesItem = new SearchableExpandItem();
      citiesItem.getContainer().setScrollEnabled(true);
      citiesItem.setText("Cities");
      citiesItem.addListener(Events.Expand, new Listener() {
        public void handleEvent(BaseEvent be) {
          Dispatcher.forwardEvent(AppEvents.NavCities);
        }
      });
      expandBar.add(citiesItem);
      
      ExpandItem categoriesItem = new SearchableExpandItem();
      categoriesItem.getContainer().setScrollEnabled(true);
      categoriesItem.setText("Categories");
      categoriesItem.addListener(Events.Expand, new Listener() {
        public void handleEvent(BaseEvent be) {
          Dispatcher.forwardEvent(AppEvents.NavCategories);
        }
      });
      expandBar.add(categoriesItem);
      
      ExpandItem emailSettingsItem = new ExpandItem();
      emailSettingsItem.getContainer().setScrollEnabled(true);
      emailSettingsItem.getContainer().setBorders(false);
      emailSettingsItem.setText("Email Settings");
      emailSettingsItem.addListener(Events.Expand, new Listener() {
        public void handleEvent(BaseEvent be) {
          Dispatcher.forwardEvent(AppEvents.ViewEmailSettings);
        }
      });
      expandBar.add(emailSettingsItem);
      
      ExpandItem messageTemplateItem = new ExpandItem();
      messageTemplateItem.getContainer().setScrollEnabled(true);
      messageTemplateItem.getContainer().setBorders(false);
      messageTemplateItem.setText("Message Templates");
      messageTemplateItem.addListener(Events.Expand, new Listener() {
        public void handleEvent(BaseEvent be) {
          Dispatcher.forwardEvent(AppEvents.NavMessageTemplates);
        }
      });
      expandBar.add(messageTemplateItem);
      
      

      /*
      ExpandItem subscribedItem = new ExpandItem();
      subscribedItem.getContainer().setScrollEnabled(true);
      subscribedItem.setText("Subscribed");
      subscribedItem.addListener(Events.Expand, new Listener() {
        public void handleEvent(BaseEvent be) {
          Dispatcher.forwardEvent(AppEvents.NavSubscribed);
        }
      });
      expandBar.add(subscribedItem);
      */

      
      Registry.register("emailSettingsItem", emailSettingsItem);
      Registry.register("messageTemplateItem", messageTemplateItem);
      Registry.register("citiesItem", citiesItem);
      Registry.register("categoriesItem", categoriesItem);

    }    

}
