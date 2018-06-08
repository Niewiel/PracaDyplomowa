package pl.niewiel.pracadyplomowa.database.model.middleTables;

import com.orm.dsl.Table;

import pl.niewiel.pracadyplomowa.database.model.Building;
import pl.niewiel.pracadyplomowa.database.model.Component;

@Table
public class ComponentToBuilding {
    private long componentId;
    private long buildingId;

    public ComponentToBuilding() {
    }

    public ComponentToBuilding(Component component, Building building) {
        this.componentId = component.getmId();
        this.buildingId = building.getmId();
    }

    public long getComponentId() {
        return componentId;
    }

    public void setComponentId(long componentId) {
        this.componentId = componentId;
    }

    public long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(long buildingId) {
        this.buildingId = buildingId;
    }
}
