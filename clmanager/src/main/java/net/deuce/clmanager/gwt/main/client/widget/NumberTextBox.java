package net.deuce.clmanager.gwt.main.client.widget;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class NumberTextBox extends TextBox {
    
    private int minValue;
    private int maxValue;
    private String previousValue;

    public NumberTextBox(int minValue, int maxValue, int initialValue, int fieldLength) {
        super();
        if (maxValue < minValue) {
            this.minValue = 0;
            this.maxValue = 99;
        } else {
            if (minValue < 0) {
                minValue = 0;
            }
            if (maxValue > 99) {
                maxValue = 99;
            }
            this.minValue = minValue;
            this.maxValue = maxValue;
        }
        if (initialValue < minValue) {
            initialValue = minValue;
        } else if (initialValue > maxValue) {
            initialValue = maxValue;
        }
        setValue(initialValue);
        setMaxLength(fieldLength);
        setVisibleLength(fieldLength);
        
        super.addChangeListener(new ChangeListener() {
            public void onChange(Widget sender) {
                try {
                    Integer.parseInt(getText());
                    previousValue = getText();
                } catch (NumberFormatException nfe) {
                    setText(previousValue);
                }
            }
        });
    }
    
    private void setValue(int i) {
        previousValue = Integer.toString(i);
        setText(previousValue);
    }
    
    public int getMinValue() {
        return minValue;
    }
    
    public void setMinValue(int minValue) {
        if (minValue < 0) {
            minValue = 0;
        } else if (minValue > maxValue) {
            minValue = maxValue;
        }
        this.minValue = minValue;
    }
    
    public int getMaxValue() {
        return maxValue;
    }
    
    public void setMaxValue(int maxValue) {
        if (minValue > maxValue) {
            maxValue = minValue;
        } else if (maxValue > 99) {
            maxValue = 99;
        }
        this.maxValue = maxValue;
    }
    
    public void increment(int byValue) {
        int currentValue = getValue();
        if (currentValue+byValue < maxValue) {
            setValue(currentValue+byValue);
        } else {
            setValue(maxValue);
        }
    }
    
    public void increment() {
        increment(1);
    }
    
    public void decrement(int byValue) {
        int currentValue = getValue();
        if (currentValue-byValue > minValue) {
            setValue(currentValue-byValue);
        } else {
            setValue(minValue);
        }
    }
    
    public void decrement() {
        decrement(1);
    }
    
    public int getValue() {
        return Integer.valueOf(getText()).intValue();
    }
}
