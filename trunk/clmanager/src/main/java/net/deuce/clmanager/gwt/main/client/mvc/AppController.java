package net.deuce.clmanager.gwt.main.client.mvc;

import net.mygwt.ui.client.mvc.AppEvent;
import net.mygwt.ui.client.mvc.Controller;


public class AppController extends Controller {

    private AppView appView;
    private ViewEmailSettingsView viewEmailSettingsView;
    private MessageTemplateView messageTemplateView;
    private ViewMessageTemplateView viewMessageTemplateView;
    private SetViewView setViewView;
    private CityView cityView;
    private CategoryView categoryView;
    private PostListView postListView;
    private PostView postView;
    private PhotoView photoView;

    public AppController() {
      registerEventType(AppEvents.Init);
      registerEventType(AppEvents.ClearReplyPending);
      registerEventType(AppEvents.PostReplied);
      registerEventType(AppEvents.PostReplyFailed);
      registerEventType(AppEvents.UpdatePost);
      registerEventType(AppEvents.RefreshMessageTemplates);
      registerEventType(AppEvents.NavMessageTemplates);
      registerEventType(AppEvents.ViewMessageTemplate);
      registerEventType(AppEvents.NavEmailSettings);
      registerEventType(AppEvents.NavEmailSettings);
      registerEventType(AppEvents.NavSetView);
      registerEventType(AppEvents.ViewCities);
      registerEventType(AppEvents.ViewCategories);
      registerEventType(AppEvents.CitySubscribed);
      registerEventType(AppEvents.CategorySubscribed);
      registerEventType(AppEvents.ViewPostList);
      registerEventType(AppEvents.ViewPost);
    }

    public void handleEvent(AppEvent event) {
        switch (event.type) {
        case AppEvents.Init:
            forwardToView(appView, event);
        case AppEvents.NavEmailSettings:
            forwardToView(viewEmailSettingsView, event);
            break;
        case AppEvents.RefreshMessageTemplates:
            forwardToView(messageTemplateView, event);
            forwardToView(postView, event);
            break;
        case AppEvents.NavMessageTemplates:
            forwardToView(messageTemplateView, event);
            break;
        case AppEvents.ViewMessageTemplate:
            forwardToView(viewMessageTemplateView, event);
            break;
        case AppEvents.NavSetView:
            forwardToView(setViewView, event);
            break;
        case AppEvents.ViewCities:
            forwardToView(cityView, event);
            break;
        case AppEvents.ViewCategories:
            forwardToView(categoryView, event);
            break;
        case AppEvents.ClearReplyPending:
        case AppEvents.UpdatePost:
        case AppEvents.PostReplyFailed:
        case AppEvents.PostReplied:
        case AppEvents.ViewPostList:
        case AppEvents.CitySubscribed:
        case AppEvents.CategorySubscribed:
            forwardToView(postListView, event);
            break;
        case AppEvents.ViewPost:
            forwardToView(postView, event);
            forwardToView(photoView, event);
            break;
        }
    }

    public void initialize() {
      appView = new AppView(this);
      viewEmailSettingsView = new ViewEmailSettingsView(this);
      messageTemplateView = new MessageTemplateView(this);
      viewMessageTemplateView = new ViewMessageTemplateView(this);
      setViewView = new SetViewView(this);
      cityView = new CityView(this);
      categoryView = new CategoryView(this);
      postListView = new PostListView(this);
      postView = new PostView(this);
      photoView = new PhotoView(this);
    }
}
