package net.deuce.clmanager.gwt.main.client.widget;

import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Spinner extends Composite {

    private HorizontalPanel wrapper;
    private int minValue;
    private int maxValue;
    private NumberTextBox numberField;
    private Listener listener;
    
    public Spinner(String label, int min, int max, int value, Listener listener) {
        this.minValue = min;
        this.maxValue = max;
        this.listener = listener;
        
        this.wrapper = new HorizontalPanel();
        numberField = new NumberTextBox(min, max, value);
        
        wrapper.add(new Label(label));
        wrapper.add(numberField);
        numberField.addChangeListener(new ChangeListener() {
            public void onChange(Widget sender) {
                notifyListener();
            }
        });
        /*
        VerticalPanel buttonPanel = new VerticalPanel();
        wrapper.add(buttonPanel);
        Button up = new Button();
        up.setWidth("5");
        up.setHeight("5");
        up.setText("^");
        up.addClickListener(new ClickListener() {
            public void onClick(Widget sender) {
                if (numberField.getValue() < maxValue) {
                    numberField.increment();
                    notifyListener();
                }
            }
        });
        buttonPanel.add(up);
        
        Button down = new Button();
        down.setWidth("5");
        down.setHeight("5");
        down.setText("v");
        down.addClickListener(new ClickListener() {
            public void onClick(Widget sender) {
                if (numberField.getValue() < maxValue) {
                    numberField.decrement();
                    notifyListener();
                }
            }
        });
        buttonPanel.add(down);
        */
        
        initWidget(wrapper);
    }
    
    private void notifyListener() {
        if (listener != null) {
            BaseEvent evt = new BaseEvent();
            evt.value = new Integer(numberField.getValue());
            listener.handleEvent(evt);
        }
    }
    
    public int getValue() {
        return numberField.getValue();
    }

}
