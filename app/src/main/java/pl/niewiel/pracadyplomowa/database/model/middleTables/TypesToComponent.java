package pl.niewiel.pracadyplomowa.database.model.middleTables;

import com.orm.dsl.Table;

import pl.niewiel.pracadyplomowa.database.model.Component;
import pl.niewiel.pracadyplomowa.database.model.ComponentType;

@Table
public class TypesToComponent {
    private long componentId;
    private long typeId;

    public TypesToComponent(Component component, ComponentType type) {
        this.componentId = component.getmId();
        this.typeId = type.getmId();
    }

    @Override
    public String toString() {
        return "TypesToComponent{" +
                "componentId=" + componentId +
                ", typeId=" + typeId +
                '}';
    }

    public TypesToComponent() {
    }

    public long getComponentId() {
        return componentId;
    }

    public void setComponentId(long componentId) {
        this.componentId = componentId;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }
}