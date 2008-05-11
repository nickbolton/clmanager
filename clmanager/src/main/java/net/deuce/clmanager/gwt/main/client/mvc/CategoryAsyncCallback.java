package net.deuce.clmanager.gwt.main.client.mvc;

import java.util.List;

import net.deuce.clmanager.gwt.main.client.model.CategoryModel;
import net.deuce.clmanager.gwt.main.client.model.CityModel;
import net.deuce.clmanager.gwt.main.client.util.Utils;
import net.mygwt.ui.client.viewer.IAsyncContentCallback;
import net.mygwt.ui.client.widget.LoadingPanel;
import asquare.gwt.debug.client.Debug;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class CategoryAsyncCallback implements AsyncCallback {
    
    private IAsyncContentCallback contentCallback;
    private CityModel cityModel;
    private long startTime;

    public CategoryAsyncCallback(long startTime, CityModel cityModel, IAsyncContentCallback contentCallback) {
        this.startTime = startTime;
        this.cityModel = cityModel;
        this.contentCallback = contentCallback;
    }
    
    public void onFailure (Throwable caught) { 
        LoadingPanel.get().hide();
        Debug.println(Utils.getStacktraceAsString(caught));
    } 
     
    public void onSuccess (Object result) { 
        LoadingPanel.get().hide();
        System.out.println("Fetch categories succeeded in " + (System.currentTimeMillis()-startTime) + " ms");
        List l = (List)result;
        CategoryModel[] categories = new CategoryModel[l.size()];
        for (int i=0; i<l.size(); i++) {
            categories[i] = (CategoryModel)l.get(i);
        }
        cityModel.setChildren(l);
        contentCallback.setElements(categories);
    } 

}
