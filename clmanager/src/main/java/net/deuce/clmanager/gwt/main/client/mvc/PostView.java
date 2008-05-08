package net.deuce.clmanager.gwt.main.client.mvc;

import net.deuce.clmanager.gwt.main.client.model.PostModel;
import net.mygwt.ui.client.Registry;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.SelectionListener;
import net.mygwt.ui.client.mvc.AppEvent;
import net.mygwt.ui.client.mvc.Controller;
import net.mygwt.ui.client.util.Format;
import net.mygwt.ui.client.widget.ToolBar;
import net.mygwt.ui.client.widget.ToolItem;
import net.mygwt.ui.client.widget.WidgetContainer;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PostView extends ReplyView {

    private VerticalPanel panel = new VerticalPanel();
    private VerticalPanel headerPanel = new VerticalPanel();
    private HTML body = new HTML();
    private ScrollPanel scroller = new ScrollPanel(body);
    
    private ToolBar toolBar;
    private HTML header = new HTML();
    private PostModel post;
    
    private String headerHTML =
        "<div class=post-detail><p><b>{0}</b></p><p><b>City:</b> {1} <b>Category:</b> {2} <b>Location:</b> {3}</p><p><b>Date:</b> {4}/{5} <b>Age:</b> {6}</p><p><b>Title:</b> {7}</p></div>";

    public PostView(Controller controller) {
        super(controller);
    }
    
        
    protected void initialize() {
        
        toolBar = new ToolBar();
        
        ToolItem flagItem = new ToolItem(Style.PUSH);
        flagItem.setText("Flag");
        flagItem.setBorders(true);
        flagItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(BaseEvent be) {
                flag(post);
            }
        });
        toolBar.add(flagItem);
        
        //ToolItemAdapter messageTemplateAdapter = new ToolItemAdapter(getMessageTemplates());
        //toolBar.add(messageTemplateAdapter);
        
        ToolItem replyItem = new ToolItem(Style.PUSH);
        replyItem.setText("Reply");
        replyItem.setBorders(true);
        replyItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(BaseEvent be) {
                setupReply(post);
            }
        });
        toolBar.add(replyItem);
        
        ToolItem editReplyItem = new ToolItem(Style.PUSH);
        editReplyItem.setText("Edit/Reply");
        editReplyItem.setBorders(true);
        editReplyItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(BaseEvent be) {
                setupEditReply(post);
            }
        });
        toolBar.add(editReplyItem);
        
        ToolItem newReplyItem = new ToolItem(Style.PUSH);
        newReplyItem.setText("New Reply");
        newReplyItem.setBorders(true);
        newReplyItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(BaseEvent be) {
                setupNewReply(post);
            }
        });
        toolBar.add(newReplyItem);
        
        body.setWordWrap(true);

        headerPanel.add(toolBar);
        headerPanel.add(header);
        headerPanel.setWidth("100%");

        DockPanel innerPanel = new DockPanel();
        innerPanel.add(headerPanel, DockPanel.NORTH);
        innerPanel.add(scroller, DockPanel.CENTER);

        innerPanel.setCellHeight(scroller, "100%");
        panel.add(innerPanel);
        innerPanel.setSize("100%", "100%");
        scroller.setSize("100%", "100%");
        
        body.setStyleName("postView-body");
    }
    
    
    protected void handleEvent(AppEvent event) {
        switch (event.type) {
        case AppEvents.ViewPost:
            post = (PostModel) event.data;
            WidgetContainer south = (WidgetContainer) Registry.get("south");

            south.removeAll();
            if (post != null) {
                String[] values = {
                    Long.toString(post.getClId().longValue()),
                    post.getCity(),
                    post.getCategory(),
                    post.getLocation(),
                    Integer.toString(post.getDate().getMonth()+1),
                    Integer.toString(post.getDate().getDate()),
                    post.getAge(),
                    post.getTitle(),
                };
                String s = Format.substitute(headerHTML, values);
                header.setHTML(s);
                
                body.setHTML(post.getContent().trim());

                south.add(panel);
                south.layout(true);

            }
            break;
        case AppEvents.RefreshMessageTemplates:
            refreshMessageTemplates();
            break;
        }
    }
}
