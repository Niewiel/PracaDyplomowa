package pl.niewiel.pracadyplomowa.database.model;

import com.orm.dsl.Table;

@Table
public class PhotoToComponent {
    private long componentId;
    private long photoId;

    public PhotoToComponent() {
    }

    public PhotoToComponent(long componentId, long photoId) {
        this.componentId = componentId;
        this.photoId = photoId;
    }
}
