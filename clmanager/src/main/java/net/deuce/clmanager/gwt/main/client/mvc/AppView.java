package net.deuce.clmanager.gwt.main.client.mvc;

import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.Registry;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.event.SelectionListener;
import net.mygwt.ui.client.mvc.AppEvent;
import net.mygwt.ui.client.mvc.Controller;
import net.mygwt.ui.client.mvc.Dispatcher;
import net.mygwt.ui.client.widget.ContentPanel;
import net.mygwt.ui.client.widget.Dialog;
import net.mygwt.ui.client.widget.ThemeSelector;
import net.mygwt.ui.client.widget.ToolBar;
import net.mygwt.ui.client.widget.ToolItem;
import net.mygwt.ui.client.widget.ToolItemAdapter;
import net.mygwt.ui.client.widget.Viewport;
import net.mygwt.ui.client.widget.WidgetContainer;
import net.mygwt.ui.client.widget.layout.BorderLayout;
import net.mygwt.ui.client.widget.layout.BorderLayoutData;
import net.mygwt.ui.client.widget.layout.FillLayout;
import asquare.gwt.debug.client.DebugConsole;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AppView extends BaseView {

    private Viewport viewport;
    private ContentPanel east;
    private WidgetContainer main;
    private ContentPanel center;
    private WidgetContainer south;
    private boolean debugShowing = false;

    public AppView(Controller controller) {
      super(controller);
    }

    protected void initialize() {
      viewport = new Viewport();
      viewport.setStyleName("my-border-layout");
      viewport.setLayout(new BorderLayout());
      
      VerticalPanel toolbarPanel = new VerticalPanel();
      
      ToolBar toolbar = new ToolBar();
      toolbar.setWidth("100%");
      buildMainToolbar(toolbar);
      DockPanel topToolbarPanel = new DockPanel();
      topToolbarPanel.setWidth("100%");
      topToolbarPanel.add(toolbar, DockPanel.WEST);
      ToolBar rToolbar = new ToolBar();
      rToolbar.setWidth(200);
      buildRightToolbar(rToolbar);
      topToolbarPanel.add(rToolbar, DockPanel.EAST);
      
      Label padding = new Label("");
      padding.setHeight("5px");
      toolbarPanel.add(padding);
      toolbarPanel.add(topToolbarPanel);
      viewport.add(toolbarPanel, new BorderLayoutData(Style.NORTH, 30));
      
      east = new ContentPanel();
      east.setText("Photos");
      east.setLayout(new FillLayout());
      viewport.add(east, new BorderLayoutData(Style.EAST, 300, 150, 250));

      main = new WidgetContainer();
      BorderLayout layout = new BorderLayout();
      layout.setMargin(0);
      main.setLayout(layout);
      
      center = new ContentPanel(Style.HEADER);
      center.setText("Posts");
      center.setLayout(new FillLayout());

      south = new WidgetContainer();
      south.setLayout(new FillLayout());
      south.setBorders(true);
      south.setStyleAttribute("backgroundColor", "white");

      main.add(center, new BorderLayoutData(Style.CENTER));
      main.add(south, new BorderLayoutData(Style.SOUTH, .5f, 50, 2000));

      viewport.add(main, new BorderLayoutData(Style.CENTER));

      viewport.layout();
      viewport.hideLoadingPanel("loading");

      Registry.register("center", center);
      Registry.register("south", south);
      Registry.register("east", east);
      
    }
    
    protected void handleEvent(AppEvent event) {
        switch (event.type) {
        case AppEvents.Init:
            Dispatcher.forwardEvent(AppEvents.ViewPostList);
            break;
        }
    }
    
    private void buildRightToolbar(ToolBar toolbar) {
        ToolItem debugConsoleItem = new ToolItem(Style.PUSH);
        debugConsoleItem.setText("Debug Console");
        debugConsoleItem.addSelectionListener(new SelectionListener() {
          public void widgetSelected(BaseEvent be) {
              if (!debugShowing) {
                  final DebugConsole console = DebugConsole.getInstance();
                  console.enable();
                  final Dialog dialog = new Dialog(Style.OK | Style.CLOSE);
                  dialog.setMinimumSize(475, 360);
                  dialog.center();
                  dialog.addListener(Events.Close, new Listener() {  
                      public void handleEvent(BaseEvent be) {  
                          debugShowing = false;
                          console.disable();
                          dialog.getContent().remove(console);
                      }  
                  });
                  dialog.getButtonById(0).addSelectionListener(new SelectionListener() {
                      public void widgetSelected(BaseEvent be) {
                          dialog.close();
                      }
                  });
                  dialog.setText("Debug Console");
                  dialog.getContent().setScrollEnabled(false);
                  dialog.getContent().add(console);

                  dialog.open();
                  debugShowing = true;
              }
          }
        });
        toolbar.add(debugConsoleItem);
        
        toolbar.add(new ToolItemAdapter(new ThemeSelector()));
    }

    private void buildMainToolbar(ToolBar toolbar) {
      ToolItem setViewItem = new ToolItem(Style.PUSH);
      setViewItem.setText("Set View");
      setViewItem.addSelectionListener(new SelectionListener() {
        public void widgetSelected(BaseEvent be) {
            Dispatcher.forwardEvent(AppEvents.NavSetView);
        }
      });
      toolbar.add(setViewItem);
      
      ToolItem emailSettingsItem = new ToolItem(Style.PUSH);
      emailSettingsItem.setText("Email Settings");
      emailSettingsItem.addSelectionListener(new SelectionListener() {
        public void widgetSelected(BaseEvent be) {
            Dispatcher.forwardEvent(AppEvents.NavEmailSettings);
        }
      });
      toolbar.add(emailSettingsItem);
      
      ToolItem messageTemplatesItem = new ToolItem(Style.PUSH);
      messageTemplatesItem.setText("Message Templates");
      messageTemplatesItem.addSelectionListener(new SelectionListener() {
        public void widgetSelected(BaseEvent be) {
            Dispatcher.forwardEvent(AppEvents.NavMessageTemplates);
        }
      });
      toolbar.add(messageTemplatesItem);
      
    }    

}
