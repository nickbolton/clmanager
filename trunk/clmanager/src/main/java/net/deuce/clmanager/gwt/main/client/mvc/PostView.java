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
import net.mygwt.ui.client.widget.layout.FillLayout;
import net.mygwt.ui.client.widget.layout.RowData;
import net.mygwt.ui.client.widget.layout.RowLayout;

import com.google.gwt.user.client.ui.HTML;

public class PostView extends ReplyView {

    private WidgetContainer wrapper;
    private ToolBar toolBar;
    private WidgetContainer bodyWrapper;
    private HTML header;
    private HTML body;
    private PostModel post;
    
    private String headerHTML =
        "<div class=post-detail><p><b>{0}</b></p><p><b>City:</b> {1} <b>Category:</b> {2} <b>Location:</b> {3}</p><p><b>Date:</b> {4}/{5} <b>Age:</b> {6}</p><p><b>Title:</b> {7}</p></div>";

    public PostView(Controller controller) {
        super(controller);
    }
    
        
    protected void initialize() {
        wrapper = new WidgetContainer();
        wrapper.setLayout(new RowLayout(Style.VERTICAL));
        
        toolBar = new ToolBar();
        
        wrapper.add(toolBar, new RowData(RowData.FILL_HORIZONTAL));
        
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
        
        header = new HTML("");
        wrapper.add(header, new RowData(RowData.FILL_HORIZONTAL));
        
        body = new HTML();
        body.setWordWrap(true);
        bodyWrapper = new WidgetContainer();
        bodyWrapper.setLayout(new FillLayout());
        bodyWrapper.setScrollEnabled(true);
        bodyWrapper.setStyleAttribute("word-wrap", "break-word");
        bodyWrapper.add(body);
        
        wrapper.add(bodyWrapper, new RowData(RowData.FILL_BOTH));
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
                //DOM.setInnerHTML(header.getElement(), s);
                
                s = "<div style='padding: 12px; font-size: 16px; '><pre>"
                    + post.getContent().trim() + "</pre></div>";
                //DOM.setInnerHTML(body.getElement(), s);
                body.setHTML(s);

                south.add(wrapper);
                south.layout(true);

            }
            break;
        case AppEvents.RefreshMessageTemplates:
            refreshMessageTemplates();
            break;
        }
    }
}
