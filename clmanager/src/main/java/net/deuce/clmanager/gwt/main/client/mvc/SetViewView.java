package net.deuce.clmanager.gwt.main.client.mvc;

import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.SelectionListener;
import net.mygwt.ui.client.mvc.AppEvent;
import net.mygwt.ui.client.mvc.Controller;
import net.mygwt.ui.client.widget.ContentPanel;
import net.mygwt.ui.client.widget.Dialog;
import net.mygwt.ui.client.widget.WidgetContainer;
import net.mygwt.ui.client.widget.layout.BorderLayout;
import net.mygwt.ui.client.widget.layout.BorderLayoutData;

import com.google.gwt.user.client.ui.HorizontalPanel;

public class SetViewView extends BaseView {

    private Dialog dialog;

    private ContentPanel citiesPanel;

    private ContentPanel categoriesPanel;

    public SetViewView(Controller controller) {
        super(controller);
    }

    protected void initialize() {
        dialog = new Dialog(Style.OK | Style.CLOSE | Style.RESIZE);
        dialog.center();
        dialog.setMinimumSize(500, 600);
        // dialog.addStyleName("my-shell-plain");
        dialog.setText("Set View");
        dialog.getButtonById(0).addSelectionListener(new SelectionListener() {
            public void widgetSelected(BaseEvent be) {
                dialog.close();
            }
        });

        BorderLayout layout = new BorderLayout();
        layout.setMargin(0);

        WidgetContainer c = dialog.getContent();
        c.setBorders(false);
        c.setLayout(layout);

        citiesPanel = new ContentPanel(Style.HEADER);

        categoriesPanel = new ContentPanel(Style.HEADER);

        c.add(citiesPanel, new BorderLayoutData(Style.WEST, 200, 100, 300));
        c.add(categoriesPanel, new BorderLayoutData(Style.CENTER));

        dialog.setSize(400, 600);

        HorizontalPanel hp = new HorizontalPanel();
        hp.setSpacing(10);
        
    }

    protected void handleEvent(AppEvent event) {
        if (event.type == AppEvents.NavSetView) {
            AppEvent evt = new AppEvent(AppEvents.ViewCities, citiesPanel);
            fireEvent(evt);
            evt = new AppEvent(AppEvents.ViewCategories, categoriesPanel);
            fireEvent(evt);
            dialog.open();
        }
    }

    public void delegateEvent(AppEvent evt) {
        fireEvent(evt);
    }

}
