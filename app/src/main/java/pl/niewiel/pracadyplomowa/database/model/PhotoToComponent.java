package pl.niewiel.pracadyplomowa.database.model;

import com.orm.dsl.Table;

@Table
public class PhotoToComponent {
    private long componentId;
    private long photoId;

    public PhotoToComponent() {
    }

    public PhotoToComponent(Photo photo, Component component) {
        this.componentId = component.getmId();
        this.photoId = photo.getmId();
    }

    @Override
    public String toString() {
        return "PhotoToComponent{" +
                "componentId=" + componentId +
                ", photoId=" + photoId +
                '}';
    }

    public long getComponentId() {
        return componentId;
    }

    public void setComponentId(long componentId) {
        this.componentId = componentId;
    }

    public long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(long photoId) {
        this.photoId = photoId;
    }
}
