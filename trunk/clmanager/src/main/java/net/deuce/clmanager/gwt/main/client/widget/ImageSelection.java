/*
 * ReflectedImageGroup.java
 *
 * Created on November 7, 2007, 8:43 PM
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.deuce.clmanager.gwt.main.client.widget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.deuce.clmanager.gwt.main.client.model.ExternalPhoto;
import net.deuce.clmanager.gwt.main.client.model.PhotoWrapper;
import net.deuce.clmanager.gwt.main.client.util.Utils;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;
import com.totsp.gwittir.client.beans.Bindable;
import com.totsp.gwittir.client.fx.ui.ReflectedImage;
import com.totsp.gwittir.client.ui.AbstractBoundWidget;
import com.totsp.gwittir.client.ui.ToStringRenderer;

/**
 *
 * @author cooper
 */
public class ImageSelection extends AbstractBoundWidget implements Bindable {
    
    private List value;
    final private double maxScalar = 1.25D;
    private transient ReflectedImage[] images;
    final private int baseWidth;
    final private int baseHeight;
    final private double reflectHeight;
    final private double opacity;
    final private Grid grid = new Grid(1,3);
    final private FlexTable imagesPanel = new FlexTable();
    private List selected;
    private List initiallySelected;
    
    /** Creates a new instance of ReflectedImageGroup */
    public ImageSelection(final int baseWidth,
            final int baseHeight, final double reflectHeight, final double opacity) {
        super();
        this.baseWidth = baseWidth;
        this.baseHeight = baseHeight;
        this.reflectHeight = reflectHeight;
        this.opacity = opacity;
        this.imagesPanel.setHeight("100%");
        final int  padding = ((int)(baseHeight * maxScalar) - baseHeight )/4;
        this.grid.setCellSpacing( padding );
        this.grid.setHeight("100%");
        this.grid.setStyleName("gwittir-ReflectedFisheyeImageGroup");
        this.grid.getRowFormatter().setVerticalAlign(0, HasVerticalAlignment.ALIGN_MIDDLE);
        this.imagesPanel.getRowFormatter().setVerticalAlign(0, HasVerticalAlignment.ALIGN_MIDDLE);
        this.grid.setWidget(0,1, imagesPanel );
        this.imagesPanel.setCellPadding(0);
        this.imagesPanel.setCellSpacing(2);
        this.setRenderer( new ToStringRenderer() );
         super.initWidget( grid );
    }
    
    public void setInitiallySelected(List ids) {
        this.initiallySelected = ids;
        if (value != null && value.size() > 0 && ids != null) {
            for (int i=0; i<ids.size(); i++) {
                String id = ((PhotoWrapper)ids.get(i)).getExternalId();
                for (int j=0; j<value.size(); j++) {
                    if (Utils.isEqual(((ExternalPhoto)value.get(j)).getId(), id)) {
                        toggleSelected(j);
                    }
                }
            }
        }
    }
    
    public void clearSelection() {
        for (int i=0; i<selected.size(); i++) {
            selected.set(i, Boolean.FALSE);
            images[i].setHeight( baseHeight );
            images[i].setWidth( baseWidth );
        }
    }
    
    public Object getValue() {
        return this.value;
    }
    
    public void setValue(Object obj) {
        if (obj == null) {
            obj = new ArrayList(0);
        }
        value = (List) obj;
        this.changes.firePropertyChange( "value", null, value );
        this.render( value );
        this.selected = new ArrayList(value.size());
        for (int i=0; i<value.size(); i++) {
            boolean b = false;
            for (int j=0; !b && j<initiallySelected.size(); j++) {
                ExternalPhoto p1 = (ExternalPhoto)value.get(i);
                PhotoWrapper p2 = (PhotoWrapper)initiallySelected.get(j);
                b = Utils.isEqual(p1.getId(), p2.getExternalId());
            }
            selected.add(Boolean.FALSE);
            if (b) {
                toggleSelected(i);
            }
        }
    }
    
    protected boolean isSelected(int index) {
        boolean b = false;
        if (index < selected.size()) {
            b = ((Boolean)selected.get(index)).booleanValue();
        }
        return b;
    }
    
    protected void render(Collection value){
        this.images = new ReflectedImage[ value.size() ];
        this.imagesPanel.clear();
        if( this.imagesPanel.getRowCount() > 0 ){
            this.imagesPanel.removeRow(0);
        }
        if( value == null ){
            return;
        }
        Iterator it = value.iterator();
        final int maxSize = (int)(baseWidth*maxScalar) +4;
        this.imagesPanel.setWidth( maxSize * value.size() +"px");
        for( int i=0; it.hasNext(); i++ ){
            final int index = i;
            final Object selectObject = it.next();
            this.images[i] = new ReflectedImage(
                    this.getRenderer().render( selectObject ).toString(),
                    this.baseWidth, this.baseHeight,
                    this.reflectHeight, this.opacity );
            
            this.imagesPanel.getCellFormatter().setWidth(0, index, maxSize+"px");
            this.imagesPanel.getCellFormatter().setHorizontalAlignment(0, index, HasHorizontalAlignment.ALIGN_CENTER);
            this.imagesPanel.setWidget( 0, index, this.images[i] );
            
            
            this.images[i].addMouseListener( new MouseListener(){
                final Timer t = new Timer(){
                    public void run() {
                        if( !isSelected(index) ){
                            images[index].setHeight( (int) (baseHeight * maxScalar) );
                            images[index].setWidth( (int) (baseWidth * maxScalar) );
                        }
                    }
                    
                };
                public void onMouseUp(Widget sender, int x, int y) {
                }
                
                public void onMouseMove(Widget sender, int x, int y) {
                }
                
                public void onMouseDown(Widget sender, int x, int y) {
                    toggleSelected(index);
                }
                
                public void onMouseLeave(Widget sender) {
                    t.cancel();
                    if( !isSelected(index) ) {
                        images[index].setHeight( baseHeight );
                        images[index].setWidth( baseWidth );
                    }
                }
                
                public void onMouseEnter(Widget sender) {
                    t.schedule(50);
                }
                
            });
        }
    }
    
    public List getSelected() {
        List l = new ArrayList();
        Object[] arr = value.toArray();
        for (int i=0; i<value.size(); i++) {
            if (isSelected(i)) {
                l.add(arr[i]);
            }
        }
        return l;
    }
    
    public void toggleSelected(int index) {
        Object[] arr = value.toArray();
        if( isSelected(index) ){
            this.imagesPanel.getCellFormatter()
            .removeStyleName(0, index, "selected");
            this.images[index].setHeight( baseHeight );
            this.images[index].setWidth( baseWidth );   
            selected.set(index, Boolean.FALSE);
        } else {
            boolean found = false;
            images[index].setHeight( (int) (baseHeight * maxScalar) );
            images[index].setWidth( (int) (baseWidth * maxScalar) );
            this.imagesPanel.getCellFormatter().setStyleName(0, index, "selected");
            this.changes.firePropertyChange( "selected", selected, selected );
            selected.set(index, Boolean.TRUE);
        }
    }
    
    
}