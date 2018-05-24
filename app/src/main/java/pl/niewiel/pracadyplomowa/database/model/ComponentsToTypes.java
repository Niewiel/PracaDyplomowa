package pl.niewiel.pracadyplomowa.database.model;

import com.orm.dsl.Table;

@Table
public class ComponentsToTypes {
    private int componentId;
    private int componentTypeId;

    public ComponentsToTypes() {
    }

    public int getComponentId() {
        return componentId;
    }

    public void setComponentId(int componentId) {
        this.componentId = componentId;
    }

    public int getComponentTypeId() {
        return componentTypeId;
    }

    public void setComponentTypeId(int componentTypeId) {
        this.componentTypeId = componentTypeId;
    }
}
