package net.deuce.clmanager.gwt.main.client.widget;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LoginWindow extends DialogBox {
    
    public LoginWindow(final AsyncCallback callback) {
        super(false, false);
        setText("Login Window");
        
        VerticalPanel vp = new VerticalPanel();
        HorizontalPanel hp = new HorizontalPanel();
        hp.add(new Label("Enter Username:"));
        final TextBox textBox = new TextBox();
        textBox.addKeyboardListener(new KeyboardListenerAdapter() {
            public void onKeyPress(Widget sender, char keyCode, int modifiers) {
                if (keyCode == KeyboardListener.KEY_ENTER) {
                    String username = textBox.getText().trim();
                    if (username.length() > 0) {
                        hide();
                        removeFromParent();
                        callback.onSuccess(username);
                    }
                }
            }
        });
        hp.add(textBox);
        vp.add(hp);
        Button close = new Button("Ok");
        close.addClickListener(new ClickListener() {
            public void onClick(Widget sender) {
                String username = textBox.getText().trim();
                if (username.length() > 0) {
                    hide();
                    removeFromParent();
                    callback.onSuccess(username);
                }
            }
        });
        vp.add(close);
        super.setSize("550", "400");
        setWidget(vp);
    }
}
