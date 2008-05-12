package net.deuce.clmanager.gwt.main.client.mvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import net.deuce.clmanager.gwt.main.client.CategoryService;
import net.deuce.clmanager.gwt.main.client.CategoryServiceAsync;
import net.deuce.clmanager.gwt.main.client.CityService;
import net.deuce.clmanager.gwt.main.client.CityServiceAsync;
import net.deuce.clmanager.gwt.main.client.PostService;
import net.deuce.clmanager.gwt.main.client.PostServiceAsync;
import net.deuce.clmanager.gwt.main.client.UserService;
import net.deuce.clmanager.gwt.main.client.UserServiceAsync;
import net.deuce.clmanager.gwt.main.client.model.CategoryModel;
import net.deuce.clmanager.gwt.main.client.model.CityModel;
import net.deuce.clmanager.gwt.main.client.model.Folder;
import net.deuce.clmanager.gwt.main.client.model.PostFilter;
import net.deuce.clmanager.gwt.main.client.model.PostModel;
import net.deuce.clmanager.gwt.main.client.model.PostingGroup;
import net.deuce.clmanager.gwt.main.client.model.UserModel;
import net.deuce.clmanager.gwt.main.client.util.Utils;
import net.deuce.clmanager.gwt.main.client.widget.Spinner;
import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.Registry;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.data.DataCallback;
import net.mygwt.ui.client.data.LoadConfig;
import net.mygwt.ui.client.data.LoadResult;
import net.mygwt.ui.client.data.Model;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.event.SelectionListener;
import net.mygwt.ui.client.mvc.AppEvent;
import net.mygwt.ui.client.mvc.Controller;
import net.mygwt.ui.client.viewer.CellLabelProvider;
import net.mygwt.ui.client.viewer.IAsyncContentCallback;
import net.mygwt.ui.client.viewer.ModelCellLabelProvider;
import net.mygwt.ui.client.viewer.ModelRemoteContentProvider;
import net.mygwt.ui.client.viewer.TableViewer;
import net.mygwt.ui.client.viewer.Viewer;
import net.mygwt.ui.client.viewer.ViewerFilter;
import net.mygwt.ui.client.viewer.ViewerFilterTextBox;
import net.mygwt.ui.client.viewer.ViewerSorter;
import net.mygwt.ui.client.widget.ContentPanel;
import net.mygwt.ui.client.widget.LoadingPanel;
import net.mygwt.ui.client.widget.ToolBar;
import net.mygwt.ui.client.widget.ToolItem;
import net.mygwt.ui.client.widget.ToolItemAdapter;
import net.mygwt.ui.client.widget.WidgetContainer;
import net.mygwt.ui.client.widget.layout.RowData;
import net.mygwt.ui.client.widget.layout.RowLayout;
import net.mygwt.ui.client.widget.menu.Menu;
import net.mygwt.ui.client.widget.menu.MenuItem;
import net.mygwt.ui.client.widget.table.Table;
import net.mygwt.ui.client.widget.table.TableColumn;
import net.mygwt.ui.client.widget.table.TableColumnModel;
import net.mygwt.ui.client.widget.table.TableItem;
import asquare.gwt.debug.client.Debug;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class PostListView extends ReplyView {

    private static int MIN_AGE = 0;
    private static int MAX_AGE = 99;
    
    private ContentPanel mainPanel;
    private WidgetContainer wrapper;
    private Table table;
    private TableViewer viewer;
    private ViewerFilter filter;
    private ToolBar toolBar;
    private ToolItem refreshItem;
    private ViewerFilterTextBox filterTextBox;
    private Spinner minAgeSpinner;
    private Spinner maxAgeSpinner;
    private MenuItem unreadOnlyItem;
    private MenuItem filterFlaggedItem;
    private MenuItem filterPhotoItem;
    private Menu cityMenu;
    private Menu categoryMenu;
    private Map columnModelMap;
    private Timer timer;
    private Object updateLock = new Object();
    private String defaultColor;
    private Folder postsFolder = new Folder("posts");
    private PostModel currentPost;
    private List subscribedCities = new ArrayList();
    private List subscribedCategories = new ArrayList();
    private List postingGroups = new ArrayList();
    private Map modalRequests = new HashMap();
    private ModelRemoteContentProvider contentProvider;
    
    private int minAge, maxAge;
    private boolean  filterFlagged, photosOnly, autoFetch, unreadOnly;

    public PostListView(Controller controller) {
        super(controller);
        
        columnModelMap = new HashMap();
        columnModelMap.put("fav", "favorite");
        columnModelMap.put("res", "responded");
        
        contentProvider = new ModelRemoteContentProvider() {

            public void getData(LoadConfig config, DataCallback dataCallback) {
                System.out.println("ZZZ getData");
                if (activePostingGroupCount() == 0) {
                    return;
                }
                reloadPosts(dataCallback);
            }

            public void getElements(Object input, final IAsyncContentCallback contentCallback) {
                System.out.println("ZZZ getElements");
                Folder f = (Folder)input;
                contentCallback.setElements(f.getChildren().toArray());
            }
            
        };
    }
    
    private void dumpPosts() {
        for (int i=0; i<postsFolder.getChildCount(); i++) {
            PostModel pm = (PostModel)postsFolder.getChild(i);
            Debug.println("Post: " + pm.getClId() + ", " + pm.getDate());
            System.out.println("Post: " + pm.getClId() + ", " + pm.getDate());
        }
    }
    
    private void setDataResults(List posts, DataCallback dataCallback) {
        synchronized (updateLock) {
            for (int i=0; i<posts.size(); i++) {
                PostModel pm = (PostModel)posts.get(i);
                        
                Debug.println("received post: " + pm);
                PostingGroup pg = getPostingGroup(pm.getCity(), pm.getCategory());
                if (pg.getLastFetched().longValue() < pm.getClId().longValue()) {
                    pg.setLastFetched(pm.getClId());
                }
            }
            LoadResult loadResult = new LoadResult();
            postsFolder.setChildren(posts);
            loadResult.setData(postsFolder);
            dataCallback.setResult(loadResult);
            updateRefreshButton(0);
        }
    }
    
    private void fetchNewPosts() {
        PostServiceAsync serviceProxy = (PostServiceAsync)GWT.create(PostService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "PostService");
        AsyncCallback callback = new AsyncCallback() {
            public void onFailure(Throwable caught) {
                LoadingPanel.get().hide();
                Debug.println(Utils.getStacktraceAsString(caught));
                reloadTimer(true);
            }

            public void onSuccess(Object result) {
                LoadingPanel.get().hide();
                List l = (List)result;
                System.out.println("new post count: " + l.size());
                if (l.size() > 0) {
                    Debug.println("START");
                    System.out.println("START");
                    dumpPosts();
                    
                    for (int i=l.size()-1; i>=0; i--) {
                        PostModel pm = (PostModel)l.get(i);
                        postsFolder.insert(pm, 0);
                        PostingGroup pg = getPostingGroup(pm.getCity(), pm.getCategory());
                        if (pg.getLastFetched().longValue() < pm.getClId().longValue()) {
                            pg.setLastFetched(pm.getClId());
                        }
                    }
                    
                    Debug.println("ADDED NEW POSTS");
                    System.out.println("ADDED NEW POSTS");
                    dumpPosts();
                    
                    /*
                    Collections.sort(postsFolder.getChildren(), new Comparator() {
                        public int compare(Object o1, Object o2) {
                            PostModel pm1 = (PostModel)o1;
                            PostModel pm2 = (PostModel)o2;
                            return pm2.getClId().compareTo(pm1.getClId());
                        }
                    });
                    */
                            
                    while (postsFolder.getChildCount() > 100) {
                        postsFolder.remove(postsFolder.getChildCount()-1);
                    }
                    
                    Debug.println("TRIMMED POSTS");
                    System.out.println("TRIMMED POSTS");
                    dumpPosts();
                    //ISelection selection = viewer.getSelection();
                    viewer.applyFilters();
                    //if (selection != null) {
                    //    viewer.setSelection(selection, true);
                    //}
                }
                
                reloadTimer(true);
            }
                    
        };
        Debug.println("sending getPosts request: " + postingGroups);
        serviceProxy.getNewPosts(getUser().getUsername(), postingGroups, null,callback);
    }
    
    private void reloadTimer(boolean fireAlways) {
        if (timer == null) {
            timer = new Timer() {
                public void run() {
                    checkForUpdates();
                }
            };
            timer.schedule(10000);
        } else if (fireAlways) {
            timer.schedule(10000);
        }
    }
    
    private void reloadPosts(final DataCallback dataCallback) {
        PostServiceAsync serviceProxy = (PostServiceAsync)GWT.create(PostService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "PostService");
        AsyncCallback callback = new AsyncCallback() {
            public void onFailure(Throwable caught) {
                LoadingPanel.get().hide();
                Debug.println(Utils.getStacktraceAsString(caught));
                reloadTimer(false);
            }

            public void onSuccess(Object result) {
                LoadingPanel.get().hide();
                setDataResults((List)result, dataCallback);                    
                reloadTimer(false);
            }
                    
        };
        Debug.println("sending getPosts request: " + postingGroups);
        serviceProxy.getPosts(getUser().getUsername(), postingGroups, callback);
    }
    
    private void setPostingGroupActive(String city, String category) {
        PostingGroup pg = getPostingGroup(city, category);
        if (pg == null) {
            postingGroups.add(new PostingGroup(city, category));
        } else {
            pg.setActive(true);
        }
    }
    
    private synchronized void enablePostingGroups(CityModel city, CategoryModel category, boolean reload) {
        PostingGroup pg;
        if (city != null) {
            Iterator itr = subscribedCategories.iterator();
            while (itr.hasNext()) {
                setPostingGroupActive(city.getName(), ((CategoryModel)itr.next()).getName());
            }
        }
        if (category != null) {
            Iterator itr = subscribedCities.iterator();
            while (itr.hasNext()) {
                setPostingGroupActive(((CityModel)itr.next()).getName(), category.getName());
            }
        }
        if (city != null && category != null) {
            setPostingGroupActive(city.getName(), category.getName());
        }
        if (reload) {
            viewer.applyFilters();
        }
    }
    
    private synchronized void disablePostingGroups(CityModel city, CategoryModel category) {
        ListIterator itr = postingGroups.listIterator();
        while (itr.hasNext()) {
            PostingGroup pg = (PostingGroup)itr.next();
            if (city != null && Utils.isEqual(pg.getCity(), city.getName())) {
                pg.setActive(false);
            } else if (category != null && Utils.isEqual(pg.getCategory(), category.getName())) {
                pg.setActive(false);
            }
        }
        viewer.applyFilters();
    }
    
    private PostingGroup getPostingGroup(String city, String category) {
        Iterator itr = postingGroups.iterator();
        PostingGroup pg;
        while (itr.hasNext()) {
            pg = (PostingGroup)itr.next();
            if (Utils.isEqual(pg.getCity(), city) && Utils.isEqual(pg.getCategory(), category)) return pg;
        }
        return null;
    }
    
    private void postReplyFailed(Long id) {
        PostModel pm = getPostModel(id);
        if (pm != null) {
            pm.set("reply-pending", null);
            viewer.update(pm);
        }
    }
    
    private void postReplied(Long id) {
        PostModel pm = getPostModel(id);
        if (pm != null) {
            pm.setResponded(Boolean.TRUE);
            pm.set("reply-pending", null);
            viewer.update(pm);
        }
    }

    private void clearReplyPending(Long id) {
        PostModel pm = getPostModel(id);
        if (pm != null) {
            pm.set("reply-pending", null);
            viewer.update(pm);
        }
    }

    private PostModel getPostModel(Long id) {
        for (int i=0; i<postsFolder.getChildCount(); i++) {
            PostModel pm = (PostModel)postsFolder.getChild(i);
            if (pm.getId().longValue() == id.longValue()) {
                return pm;
            }
        }
        return null;
    }

    protected void handleEvent(AppEvent event) {
        switch (event.type) {
        case AppEvents.Init:
            break;
        case AppEvents.ViewPostList:
            if (mainPanel == null) {
                mainPanel = (ContentPanel) Registry.get("center");
                mainPanel.removeAll();
                mainPanel.add(wrapper);
                mainPanel.layout(true);
            }
            refresh(false);
            break;
        case AppEvents.PostReplyFailed:
            postReplyFailed((Long)event.data);
            break;
        case AppEvents.ClearReplyPending:
            clearReplyPending((Long)event.data);
            break;
        case AppEvents.PostReplied:
            postReplied((Long)event.data);
            break;
        case AppEvents.CitySubscribed:
            citySubscribed((CityModel)event.data);
            break;
        case AppEvents.CategorySubscribed:
            categorySubscribed((CategoryModel)event.data);
            break;
        case AppEvents.UpdatePost:
            viewer.update((PostModel)event.data);
            break;
        }
    }
    
    private void updateRefreshButton(int newPostCount) {
        if (newPostCount == 0) {
            refreshItem.setText("Refresh (0)");
            refreshItem.setStyleAttribute("color", "black");
        } else {
            refreshItem.setText("Refresh ("+newPostCount+")");
            refreshItem.setStyleAttribute("color", "red");
        }
    }
    
    private void checkForUpdates() {
        if (autoFetch) {
            refresh(false);
        } else {
            if (activePostingGroupCount() > 0) {
                PostServiceAsync serviceProxy = (PostServiceAsync)GWT.create(PostService.class);
                ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
                target.setServiceEntryPoint(GWT.getModuleBaseURL() + "PostService");
                AsyncCallback callback = new AsyncCallback() {
                    public void onFailure(Throwable caught) {
                        Debug.println(Utils.getStacktraceAsString(caught));
                        timer.schedule(10000);
                    }
        
                    public void onSuccess(Object result) {
                        updateRefreshButton(((Integer)result).intValue());
                        timer.schedule(10000);
                    }
                        
                };
                UserModel user = getUser();
                PostFilter postFilter = new PostFilter(minAge, maxAge, unreadOnly,
                    filterFlagged, photosOnly, filterTextBox.getText());
                serviceProxy.getNewPostCount(getUser().getUsername(), postingGroups, postFilter, callback);
            }
        }
    }
    
    private void refresh(boolean force) {
        if (force || !autoFetch || postsFolder.getChildCount() == 0) {
            Iterator itr = postingGroups.iterator();
            while (itr.hasNext()) {
                ((PostingGroup)itr.next()).setLastFetched(new Long(0L));
            }
            Debug.println("Refreshing with postingGroups: " + postingGroups);
            contentProvider.load();
        } else {
            fetchNewPosts();
        }
    }
    
    protected void initialize() {
        wrapper = new WidgetContainer();
        wrapper.setLayout(new RowLayout());
        
        filterTextBox = new ViewerFilterTextBox();
        filter = new ViewerFilter() {
                public boolean select(Viewer viewer, Object parent, Object element) {
                PostModel m = (PostModel) element;
                if (unreadOnly && m.isViewed().booleanValue()) {
                    Debug.println("post("+m.getClId()+") failed unread only filter: " + unreadOnly + ", " +m.isViewed());
                    return false;
                }
                if (minAge > MIN_AGE || maxAge < MAX_AGE) {
                    if (m.getAge() == null || m.getAge().length() == 0) {
                        Debug.println("post("+m.getClId()+") failed age filter: " + minAge + ", " + maxAge + ", " +m.getAge());
                        return false;
                    } 
                    try {
                        int age = Integer.valueOf(m.getAge()).intValue();
                        if (age < minAge || maxAge < age) {
                            Debug.println("post("+m.getClId()+") failed age filter: " + minAge + ", " + maxAge + ", " +m.getAge());
                            return false;
                        }
                    } catch (NumberFormatException nfe) {
                        Debug.println("post("+m.getClId()+") failed age filter: " + minAge + ", " + maxAge + ", " +m.getAge());
                        return false;
                    }
                }
                if (filterFlagged && m.isFlagged().booleanValue()) {
                    Debug.println("post("+m.getClId()+") failed flagged filter: " + filterFlagged + ", " +m.isFlagged());
                    return false;
                }
                if (photosOnly && !m.isPic().booleanValue()) {
                    Debug.println("post("+m.getClId()+") failed photosOnly filter: " + photosOnly + ", " +m.isPic());
                    return false;
                }
                
                boolean subscribed = false;
                PostingGroup pg = getPostingGroup(m.getCity(), m.getCategory());
                if (pg == null || !pg.isActive()) {
                    Debug.println("post("+m.getClId()+") failed subscribed filter: " + postingGroups + ", " + m.getCity() + ", " + m.getCategory());
                    return false;
                }

                boolean filtered = false;
                UserModel user = getUser();
                Iterator itr = user.getCitySubscriptionFilter().iterator();
                while (!filtered && itr.hasNext()) {
                    filtered = Utils.isEqual(m.getCity(), itr.next());
                }
                if (filtered) {
                    Debug.println("post("+m.getClId()+") failed city filter: " + user.getCitySubscriptionFilter() + ", " + m.getCity());
                    return false;
                }

                itr = user.getCategorySubscriptionFilter().iterator();
                while (!filtered && itr.hasNext()) {
                    filtered = Utils.isEqual(m.getCategory(), itr.next());
                }
                if (filtered) {
                    Debug.println("post("+m.getClId()+") failed category filter: " + user.getCategorySubscriptionFilter() + ", " + m.getCategory());
                    return false;
                }

                if (filterTextBox.getText().length() == 0) {
                    return true;
                }

                String text = filterTextBox.getText().toLowerCase();
                boolean result = false;
                itr = m.getPropertyNames();
                while (!result && itr.hasNext()) {
                    String value = m.getAsString((String) itr.next())
                        .toLowerCase();
                    result = value.indexOf(text) >= 0;
                }
                if (!result) {
                    Debug.println("post("+m.getClId()+") failed text filter: " + text);
                }
                return result;
            }
        };
        
        toolBar = new ToolBar();
        wrapper.add(toolBar, new RowData(RowData.FILL_HORIZONTAL));
        
        ToolBar actionToolBar = new ToolBar();
        wrapper.add(actionToolBar, new RowData(RowData.FILL_HORIZONTAL));
        
        TableColumn[] columns = new TableColumn[7];
        columns[0] = new TableColumn("date", "Date", .11f);
        columns[1] = new TableColumn("fav", "Fav", .07f);
        columns[2] = new TableColumn("res", "Res", .07f);
        columns[3] = new TableColumn("pic", "Pic", .07f);
        columns[4] = new TableColumn("age", "Age", .11f);
        columns[5] = new TableColumn("flagged", "Fl", .07f);
        columns[6] = new TableColumn("title", "Title", .5f);

        TableColumnModel cm = new TableColumnModel(columns);

        table = new Table(Style.SINGLE, cm);
        table.setBorders(true);
        table.addListener(Events.SelectionChange, new Listener() {

            public void handleEvent(BaseEvent be) {
                if (table.getSelection().length > 0) {
                    TableItem item = table.getSelection()[0];
                    final PostModel post = (PostModel) item.getData();
                    currentPost = post;
                    post.setViewed(Boolean.TRUE);
                    showPost(post);
                    
                    PostServiceAsync serviceProxy = (PostServiceAsync)GWT.create(PostService.class);
                    ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
                    target.setServiceEntryPoint(GWT.getModuleBaseURL() + "PostService");
                    AsyncCallback callback = new AsyncCallback() {
                        public void onFailure(Throwable caught) {
                            Debug.println(Utils.getStacktraceAsString(caught));
                        }

                        public void onSuccess(Object result) {
                            viewer.update(post);
                        }
                        
                    };
                    serviceProxy.setViewed(getUser().getUsername(), post.getId(), callback);

                }
            }
        });
        
        viewer = new TableViewer(table);
        viewer.setSorter(new ViewerSorter() {
            public int compare(Viewer viewer, Object e1, Object e2) {
                if (e1 instanceof PostModel && e2 instanceof PostModel) {
                    PostModel p1 = (PostModel)e1;
                    PostModel p2 = (PostModel)e2;
                    int result = 0;
                    /*if (p2.isViewed().booleanValue() && !p1.isViewed().booleanValue()) {
                        return -1;
                    } else if (p1.isViewed().booleanValue() && !p2.isViewed().booleanValue()) {
                        return 1;
                    }*/
                    if (p1.getDate() != null && p2.getDate() != null) {
                        result = p2.getDate().compareTo(p1.getDate());
                    }
                    if (result == 0) {
                        result = p2.getClId().compareTo(p1.getClId());
                    }
                    return result;
                }
                return super.compare(viewer, e1, e2);
            }
        });
        ModelCellLabelProvider lp = new ModelCellLabelProvider() {
            public String getModelProperty(Model model, String columnId) {
                PostModel pm = (PostModel)model;
                boolean bold = !pm.isViewed().booleanValue();
                StringBuffer sb = new StringBuffer();
                if (bold) {
                    sb.append("<b>");
                }
                Object p = model.get(columnId);
                if (p == null) {
                    p = model.get((String)columnModelMap.get(columnId));
                }
                String value;
                if (Utils.isEqual(columnId, "res") && pm.get("reply-pending") != null && ((Boolean)pm.get("reply-pending")).booleanValue()) {
                    value = "-";
                } else if (p instanceof Boolean) {
                    value = ((Boolean)p).booleanValue() ? columnId.substring(0,1).toUpperCase() : "";
                } else if (p instanceof Date) {
                    Date d = (Date)p;
                    value = (d.getMonth()+1)+"/"+d.getDate();
                } else {
                    value = super.getModelProperty(model, columnId);
                }
                sb.append(value);
                if (bold) {
                    sb.append("</b>");
                }
                return sb.toString();
            }
        };
        viewer.getViewerColumn(0).setLabelProvider(lp);
        viewer.getViewerColumn(1).setLabelProvider(new CellLabelProvider() {
            public String getText(Object element) {
                return "";
            }
        });
        viewer.getViewerColumn(2).setLabelProvider(lp);
        viewer.getViewerColumn(3).setLabelProvider(lp);
        viewer.getViewerColumn(4).setLabelProvider(lp);
        viewer.getViewerColumn(5).setLabelProvider(lp);
        viewer.getViewerColumn(6).setLabelProvider(lp);
        viewer.setContentProvider(contentProvider);
        wrapper.add(table, new RowData(RowData.FILL_BOTH));
        
        autoFetch = Boolean.valueOf(getUser().getPreference("autoFetch")).booleanValue();
        final ToolItem autoRefreshItem = new ToolItem(Style.TOGGLE);
        autoRefreshItem.setSelected(autoFetch);
        autoRefreshItem.setText("Auto");
        autoRefreshItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(BaseEvent be) {
                autoFetch = autoRefreshItem.isSelected();
                savePreference("autoFetch", ""+autoFetch, null);
            }
        });
        toolBar.add(autoRefreshItem);
        
        refreshItem = new ToolItem(Style.PUSH);
        refreshItem.setText("Refresh");
        refreshItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(BaseEvent be) {
                refresh(true);
            }
        });
        toolBar.add(refreshItem);
        
        toolBar.add(new ToolItem(Style.SEPARATOR));
        
        ToolItem clearFilters = new ToolItem(Style.PUSH);
        clearFilters.setText("Clear Filters");
        clearFilters.addSelectionListener(new SelectionListener() {
            public void widgetSelected(BaseEvent be) {
                
                final Map preferences = new HashMap();
                preferences.put("minAge", ""+MIN_AGE);
                preferences.put("maxAge", ""+MAX_AGE);
                preferences.put("noFlagged", "false");
                preferences.put("photosOnly", "false");
                preferences.put("searchTerm", "");
                
                savePreferences(preferences, new AsyncCallback() {
                    public void onFailure(Throwable caught) {
                        Debug.println(Utils.getStacktraceAsString(caught));
                    }

                    public void onSuccess(Object result) {
                        minAge = MIN_AGE;
                        maxAge = MAX_AGE;
                        unreadOnly = false;
                        filterFlagged = false;
                        photosOnly = false;
                        UserModel user = getUser();
                        user.addAll(preferences);
                        filterTextBox.setText("");
                        minAgeSpinner.setValue(MIN_AGE);
                        maxAgeSpinner.setValue(MAX_AGE);
                        filterPhotoItem.setSelected(false);
                        filterFlaggedItem.setSelected(false);
                        unreadOnlyItem.setSelected(false);
                        clearCityFilters(false);
                        clearCategoryFilters(true); // updates the view
                        viewer.applyFilters();
                    }
                });
                
            }
        });
        toolBar.add(clearFilters);
        
        final ToolItem flagItem = new ToolItem(Style.PUSH);  
        flagItem.setText("Flag");
        flagItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(BaseEvent be) {
                if (currentPost != null) {
                    flag(currentPost);
                }
            }

        });
        actionToolBar.add(flagItem);

        actionToolBar.add(new ToolItem(Style.SEPARATOR));
        
        final ToolItem newReplyItem = new ToolItem(Style.PUSH);  
        newReplyItem.setText("New Reply");
        newReplyItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(BaseEvent be) {
                setupNewReply(currentPost);
            }

        });
        actionToolBar.add(newReplyItem);
        
        final ToolItem editReplyItem = new ToolItem(Style.PUSH);  
        editReplyItem.setText("Edit/Reply");
        editReplyItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(BaseEvent be) {
                setupEditReply(currentPost);
            }

        });
        actionToolBar.add(editReplyItem);
        
        final ToolItem replyItem = new ToolItem(Style.PUSH);  
        replyItem.setText("Reply");
        replyItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(BaseEvent be) {
                setupReply(currentPost);
            }

        });
        actionToolBar.add(replyItem);
        
        actionToolBar.add(new ToolItem(Style.SEPARATOR));
        
        ToolItemAdapter labelAdapter = new ToolItemAdapter(new Label("Message Template"));
        actionToolBar.add(labelAdapter);
        ToolItemAdapter messageTemplateAdapter = new ToolItemAdapter(getMessageTemplates());
        actionToolBar.add(messageTemplateAdapter);
        
        cityMenu = new Menu();  
        buildCityMenu();
        ToolItem cityMenuItem = new ToolItem(Style.MENU);  
        cityMenuItem.setText("Cities");  
        cityMenuItem.setMenu(cityMenu);  
        toolBar.add(cityMenuItem);
        
        categoryMenu = new Menu();  
        buildCategoryMenu();
        ToolItem categoryMenuItem = new ToolItem(Style.MENU);  
        categoryMenuItem.setText("Categories");  
        categoryMenuItem.setMenu(categoryMenu);  
        
        toolBar.add(categoryMenuItem);
        
        UserModel user = getUser();
        filterTextBox.setText(user.getPreference("searchTerm"));
        filterTextBox.bind(viewer);
        filterTextBox.addChangeListener(new ChangeListener() {
            public void onChange(Widget sender) {
                if (filterTextBox.getText() != null && filterTextBox.getText().matches("^[0-9]+$")) {
                    long clPostId = Long.valueOf(filterTextBox.getText()).longValue();
                    boolean found = false;
                    for (int i=0; !found && i<postsFolder.getChildCount(); i++) {
                        found = ((PostModel)postsFolder.getChild(i)).getClId().longValue() == clPostId;
                    }
                    if (!found) {
                        loadPost(clPostId);
                    }
                }
                savePreference("searchTerm", filterTextBox.getText(), null);
                viewer.applyFilters();
            }

        });
        ToolItemAdapter filterTextBoxAdapter = new ToolItemAdapter(filterTextBox);
        toolBar.add(filterTextBoxAdapter);
        
        viewer.addFilter(filter);  
        
        ToolItem clearSearchButton = new ToolItem(Style.PUSH);
        clearSearchButton.setText("x");
        clearSearchButton.setWidth(clearSearchButton.getHeight());
        clearSearchButton.addSelectionListener(new SelectionListener() {
            public void widgetSelected(BaseEvent be) {
                filterTextBox.setText("");
                viewer.applyFilters();
                savePreference("searchTerm", "", null);
            }
        });
        toolBar.add(clearSearchButton);

        Menu filterMenu = new Menu();  
        unreadOnlyItem = new MenuItem(Style.CHECK);  
        unreadOnlyItem.setText("Unread");  
        unreadOnly = Boolean.valueOf(user.getPreference("unreadOnly")).booleanValue();
        unreadOnlyItem.setSelected(unreadOnly);
        unreadOnlyItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(BaseEvent be) {
                unreadOnly = unreadOnlyItem.isSelected();
                viewer.applyFilters();
                savePreference("unreadOnly", ""+unreadOnly, null);
            }

        });
        filterMenu.add(unreadOnlyItem);  
        
        filterFlaggedItem = new MenuItem(Style.CHECK);  
        filterFlaggedItem.setText("No Flagged");  
        filterFlagged = Boolean.valueOf(user.getPreference("noFlagged")).booleanValue();
        filterFlaggedItem.setSelected(filterFlagged);
        filterFlaggedItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(BaseEvent be) {
                filterFlagged = filterFlaggedItem.isSelected();
                viewer.applyFilters();
                savePreference("noFlagged", ""+filterFlagged, null);
            }

        });
        filterMenu.add(filterFlaggedItem);  
           
        filterPhotoItem = new MenuItem(Style.CHECK);  
        filterPhotoItem.setText("Photos Only");  
        photosOnly = Boolean.valueOf(user.getPreference("photosOnly")).booleanValue();
        filterPhotoItem.setSelected(photosOnly);
        filterPhotoItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(BaseEvent be) {
                photosOnly = filterPhotoItem.isSelected();
                viewer.applyFilters();
                savePreference("photosOnly", ""+photosOnly, null);
            }

        });
        filterMenu.add(filterPhotoItem);  
        
        filterMenu.add(new MenuItem(Style.SEPARATOR));
           
        MenuItem allViewedItem = new MenuItem(Style.PUSH);
        allViewedItem.setText("Mark All Viewed");
        allViewedItem.addSelectionListener(new SelectionListener() {
            public void widgetSelected(BaseEvent be) {
                PostServiceAsync serviceProxy = (PostServiceAsync)GWT.create(PostService.class);
                ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
                target.setServiceEntryPoint(GWT.getModuleBaseURL() + "PostService");
                final String modalOriginator = "PostListView.PostService::setAllViewed";
                goModal(modalOriginator, "Marking all as viewed...");
                AsyncCallback callback = new AsyncCallback() {
                    public void onFailure(Throwable caught) {
                        clearModal(modalOriginator);
                        Debug.println(Utils.getStacktraceAsString(caught));
                    }

                    public void onSuccess(Object result) {
                        clearModal(modalOriginator);
                        PostModel[] posts = (PostModel[])viewer.getElements();
                        for (int i=0; posts != null && i<posts.length; i++) {
                            posts[i].setViewed(Boolean.TRUE);
                        }
                        viewer.update();
                    }
                    
                };
                serviceProxy.setAllViewed(getUser().getUsername(), postingGroups, callback);

            }
        });
        filterMenu.add(allViewedItem);
        
        ToolItem filterMenuItem = new ToolItem(Style.MENU);  
        filterMenuItem.setText("Filters");  
        filterMenuItem.setMenu(filterMenu);  
        
        toolBar.add(filterMenuItem);
        
        if (user.getPreference("minAge") != null) {
            minAge = Integer.valueOf(user.getPreference("minAge")).intValue();
        } else {
            minAge = MIN_AGE;
        }
        if (user.getPreference("maxAge") != null) {
            maxAge = Integer.valueOf(user.getPreference("maxAge")).intValue();
        } else {
            maxAge = MAX_AGE;
        }
        minAgeSpinner = new Spinner("Min Age", MIN_AGE, MAX_AGE, minAge, new Listener() {
            public void handleEvent(BaseEvent be) {
                if (be.value != null) {
                    Integer n = (Integer)be.value;
                    minAge = n.intValue();
                    viewer.applyFilters();
                    setPreference("minAge", ""+minAge);
                }
            }
        });
        toolBar.add(new ToolItemAdapter(minAgeSpinner));
        
        maxAgeSpinner = new Spinner("Max Age", MIN_AGE, MAX_AGE, maxAge, new Listener() {
            public void handleEvent(BaseEvent be) {
                if (be.value != null) {
                    Integer n = (Integer)be.value;
                    maxAge = n.intValue();
                    viewer.applyFilters();
                    setPreference("maxAge", ""+maxAge);
                }
            }
        });
        toolBar.add(new ToolItemAdapter(maxAgeSpinner));
        
    }
    
    private void loadPost(long clPostId) {
        PostServiceAsync serviceProxy = (PostServiceAsync)GWT.create(PostService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "PostService");
        final String modalOriginator = "PostListView.PostService::getPostByClId";
        goModal(modalOriginator, "Loading post "+clPostId+"...");
        AsyncCallback callback = new AsyncCallback() {
            public void onFailure(Throwable caught) {
                clearModal(modalOriginator);
                Debug.println(Utils.getStacktraceAsString(caught));
            }

            public void onSuccess(Object result) {
                clearModal(modalOriginator);
                if (result != null) {
                    showPost((PostModel)result);
                }
            }
            
        };
        serviceProxy.getPostByClId(getUser().getUsername(), clPostId, callback);
    }
    
    private boolean menuContainsItem(Menu menu, String name) {
        boolean b = false;
        for (int i=0; !b && i<menu.getItemCount(); i++) {
            b = Utils.isEqual(menu.getItem(i).getText(), name);
        }
        return b;
    }
    
    private void citySubscribed(CityModel city) {
        if (city.isSubscribed().booleanValue()) {
            if (!subscribedCities.contains(city)) {
                subscribedCities.add(city);
            }
            if (!menuContainsItem(cityMenu, city.getName())) {
                final MenuItem cityItem = new MenuItem(Style.CHECK);  
                cityItem.setText(city.getName());  
                cityItem.setSelected(true);
                cityItem.addSelectionListener(new SelectionListener() {
                    public void widgetSelected(BaseEvent be) {
                        viewer.setInput(postsFolder);
                    }
        
                });
                cityMenu.add(cityItem);  
            }
            getUser().getCitySubscriptionFilter().add(city.getName());
            enablePostingGroups(city, null, true);
            removeCityFilter(city);
            
        } else {
            subscribedCities.remove(city);
            for (int i=0; i<cityMenu.getItemCount(); i++) {
                MenuItem item = cityMenu.getItem(i);
                if (Utils.isEqual(item.getText(), city.getName())) {
                    cityMenu.remove(item);
                    break;
                }
            }
            disablePostingGroups(city, null);
            removeCityFilter(city);
        }
    }
    
    private void categorySubscribed(CategoryModel category) {
        if (category.isSubscribed().booleanValue()) {
            if (!subscribedCategories.contains(category)) {
                subscribedCategories.add(category);
            }
            if (!menuContainsItem(categoryMenu, category.getName())) {
                final MenuItem categoryItem = new MenuItem(Style.CHECK);  
                categoryItem.setText(category.getName());  
                categoryItem.setSelected(true);
                categoryItem.addSelectionListener(new SelectionListener() {
                    public void widgetSelected(BaseEvent be) {
                        viewer.setInput(postsFolder);
                    }
        
                });
                categoryMenu.add(categoryItem);  
            }
            getUser().getCategorySubscriptionFilter().add(category.getName());
            enablePostingGroups(null, category, true);
            removeCategoryFilter(category);
        } else {
            subscribedCategories.remove(category);
            for (int i=0; i<categoryMenu.getItemCount(); i++) {
                MenuItem item = categoryMenu.getItem(i);
                if (Utils.isEqual(item.getText(), category.getName())) {
                    categoryMenu.remove(item);
                    break;
                }
            }
            disablePostingGroups(null, category);
            removeCategoryFilter(category);
        }
    }

    private void addCityFilter(final CityModel city) {
        UserServiceAsync serviceProxy = (UserServiceAsync)GWT.create(UserService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "UserService");
        AsyncCallback callback = new AsyncCallback() {
            public void onFailure(Throwable caught) {
                Debug.println(Utils.getStacktraceAsString(caught));
            }

            public void onSuccess(Object result) {
                getUser().getCitySubscriptionFilter().add(city.getName());
                viewer.applyFilters();
            }
        };
        serviceProxy.addCityFilter(getUser().getUsername(), city.getName(), callback);
    }
    
    private void clearCityFilters(final boolean update) {
        UserServiceAsync serviceProxy = (UserServiceAsync)GWT.create(UserService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "UserService");
        AsyncCallback callback = new AsyncCallback() {
            public void onFailure(Throwable caught) {
                Debug.println(Utils.getStacktraceAsString(caught));
            }

            public void onSuccess(Object result) {
                getUser().getCitySubscriptionFilter().clear();
                for (int i=0; i<cityMenu.getItemCount(); i++) {
                    MenuItem item = cityMenu.getItem(i);
                    item.setSelected(true);
                }
                if (update) {
                    viewer.applyFilters();
                }
            }
        };
        serviceProxy.clearCityFilters(getUser().getUsername(), callback);
    }
    
    private void removeCityFilter(final CityModel city) {
        UserServiceAsync serviceProxy = (UserServiceAsync)GWT.create(UserService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "UserService");
        AsyncCallback callback = new AsyncCallback() {
            public void onFailure(Throwable caught) {
                Debug.println(Utils.getStacktraceAsString(caught));
            }

            public void onSuccess(Object result) {
                getUser().getCitySubscriptionFilter().remove(city.getName());
                viewer.applyFilters();
            }
        };
        serviceProxy.removeCityFilter(getUser().getUsername(), city.getName(), callback);
    }
    
    private void addCategoryFilter(final CategoryModel category) {
        UserServiceAsync serviceProxy = (UserServiceAsync)GWT.create(UserService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "UserService");
        AsyncCallback callback = new AsyncCallback() {
            public void onFailure(Throwable caught) {
                Debug.println(Utils.getStacktraceAsString(caught));
            }

            public void onSuccess(Object result) {
                getUser().getCategorySubscriptionFilter().add(category.getName());
                viewer.applyFilters();
            }
        };
        serviceProxy.addCategoryFilter(getUser().getUsername(), category.getName(), callback);
    }
    
    private void clearCategoryFilters(final boolean update) {
        UserServiceAsync serviceProxy = (UserServiceAsync)GWT.create(UserService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "UserService");
        AsyncCallback callback = new AsyncCallback() {
            public void onFailure(Throwable caught) {
                Debug.println(Utils.getStacktraceAsString(caught));
            }

            public void onSuccess(Object result) {
                getUser().getCategorySubscriptionFilter().clear();
                for (int i=0; i<categoryMenu.getItemCount(); i++) {
                    MenuItem item = categoryMenu.getItem(i);
                    item.setSelected(true);
                }
                if (update) {
                    viewer.applyFilters();
                }
            }
        };
        serviceProxy.clearCategoryFilters(getUser().getUsername(), callback);
    }
    
    private void removeCategoryFilter(final CategoryModel category) {
        UserServiceAsync serviceProxy = (UserServiceAsync)GWT.create(UserService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "UserService");
        AsyncCallback callback = new AsyncCallback() {
            public void onFailure(Throwable caught) {
                Debug.println(Utils.getStacktraceAsString(caught));
            }

            public void onSuccess(Object result) {
                getUser().getCategorySubscriptionFilter().remove(category.getName());
                viewer.applyFilters();
            }
        };
        serviceProxy.removeCategoryFilter(getUser().getUsername(), category.getName(), callback);
    }
    
    private int activePostingGroupCount() {
        Iterator itr = postingGroups.iterator();
        int count = 0;
        while (itr.hasNext()) {
            if (((PostingGroup)itr.next()).isActive()) count++;
        }
        return count;
    }

    private void buildCityMenu() {
        CityServiceAsync serviceProxy = (CityServiceAsync)GWT.create(CityService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "CityService");
        final String modalOriginator = "PostListView.CityService::getSubscribedCities";
        goModal(modalOriginator, "Loading Categories...");
        serviceProxy.getSubscribedCities(getUser().getUsername(), new AsyncCallback() {
            public void onFailure (Throwable caught) { 
                clearModal(modalOriginator);
                Debug.println(Utils.getStacktraceAsString(caught));
            } 
             
            public void onSuccess (Object result) { 
                clearModal(modalOriginator);
                List l = (List)result;
                for (int i=0; i<l.size(); i++) {
                    final CityModel city = (CityModel)l.get(i);
                    subscribedCities.add(city);
                    enablePostingGroups(city, null, false);
                    if (!menuContainsItem(cityMenu, city.getName())) {
                        final MenuItem cityItem = new MenuItem(Style.CHECK);  
                        cityItem.setText(city.getName());
                        cityItem.setSelected(!getUser().getCitySubscriptionFilter().contains(city.getName()));
                        cityItem.addSelectionListener(new SelectionListener() {
                            public void widgetSelected(BaseEvent be) {
                                if (!cityItem.isSelected()) {
                                    addCityFilter(city);
                                } else {
                                    removeCityFilter(city);
                                }
                                viewer.applyFilters();
                            }
    
                        });
                        cityMenu.add(cityItem);
                    }
                }
                if (activePostingGroupCount() > 0) {
                    refresh(false);
                }
            }   
        });
    }
    
    private void buildCategoryMenu() {
        CategoryServiceAsync serviceProxy = (CategoryServiceAsync)GWT.create(CategoryService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "CategoryService");
        final String modalOriginator = "PostListView.CategoryService::getSubscribedCategories";
        goModal(modalOriginator, "Loading Categories...");
        serviceProxy.getSubscribedCategories(getUser().getUsername(), new AsyncCallback() {
            public void onFailure (Throwable caught) { 
                clearModal(modalOriginator);
                Debug.println(Utils.getStacktraceAsString(caught));
            } 
             
            public void onSuccess (Object result) { 
                clearModal(modalOriginator);
                List l = (List)result;
                for (int i=0; i<l.size(); i++) {
                    final CategoryModel category = (CategoryModel)l.get(i);
                    subscribedCategories.add(category);
                    enablePostingGroups(null, category, false);
                    if (!menuContainsItem(categoryMenu, category.getName())) {
                        final MenuItem categoryItem = new MenuItem(Style.CHECK);  
                        categoryItem.setText(category.getName());
                        categoryItem.setSelected(!getUser().getCategorySubscriptionFilter().contains(category.getName()));
                        categoryItem.addSelectionListener(new SelectionListener() {
                            public void widgetSelected(BaseEvent be) {
                                if (!categoryItem.isSelected()) {
                                    addCategoryFilter(category);
                                } else {
                                    removeCategoryFilter(category);
                                }
                                viewer.applyFilters();
                            }
    
                        });
                        categoryMenu.add(categoryItem);
                    }
                }
                if (activePostingGroupCount() > 0) {
                    refresh(false);
                }
                Registry.register("categories", l);
            }   
        });
    }
    
    private void showPost(PostModel post) {
        AppEvent evt = new AppEvent(AppEvents.ViewPost, post);
        fireEvent(evt);
    }

}
