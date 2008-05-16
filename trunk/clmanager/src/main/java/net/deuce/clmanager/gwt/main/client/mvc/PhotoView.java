package net.deuce.clmanager.gwt.main.client.mvc;

import java.util.List;

import net.deuce.clmanager.gwt.main.client.ImageService;
import net.deuce.clmanager.gwt.main.client.ImageServiceAsync;
import net.deuce.clmanager.gwt.main.client.model.ImageModel;
import net.deuce.clmanager.gwt.main.client.model.PostModel;
import net.mygwt.ui.client.Registry;
import net.mygwt.ui.client.mvc.AppEvent;
import net.mygwt.ui.client.mvc.Controller;
import net.mygwt.ui.client.mvc.View;
import net.mygwt.ui.client.widget.WidgetContainer;
import net.mygwt.ui.client.widget.layout.FlowLayout;
import net.mygwt.ui.client.widget.layout.RowData;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class PhotoView extends View {

    private WidgetContainer list;
    private long lastClick = 0L;

    public PhotoView(Controller controller) {
        super(controller);
    }

    protected void initialize() {
        list = new WidgetContainer();
        list.setScrollEnabled(true);
        list.setLayout(new FlowLayout());
    }

    protected void handleEvent(AppEvent event) {
        if (event.type == AppEvents.ViewPost) {
            PostModel post = (PostModel) event.data;
            final WidgetContainer east = (WidgetContainer) Registry.get("east");

            east.removeAll();
            if (post != null) {
                ImageServiceAsync serviceProxy = (ImageServiceAsync) GWT.create(ImageService.class);
                ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
                target.setServiceEntryPoint(GWT.getModuleBaseURL() + "ImageService");
                AsyncCallback callback = new AsyncCallback() {
                    public void onFailure(Throwable caught) {
                        System.out.println("Failed: " + caught);
                        east.layout(true);
                    }
                    public void onSuccess(Object result) {
                        List l = (List)result;
                        for (int i=0; i<l.size(); i++) {
                            ImageModel model = (ImageModel)l.get(i);
                            final Image image = new Image("/clserver"+model.getPath());
                            image.addClickListener(new ClickListener() {
                                public void onClick(Widget sender) {
                                    long t = System.currentTimeMillis();
                                    if (lastClick > 0 && (t-lastClick)<300) {
                                    }
                                    lastClick = t;
                                }
                            });
                            list.add(image, new RowData());
                        }
                        east.layout(true);
                    }
                };
                
                list.removeAll();
                
                serviceProxy.getImages(post.getId(), callback);
                east.add(list);

            }
        }
    }
}
