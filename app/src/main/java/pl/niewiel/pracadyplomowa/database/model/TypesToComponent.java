package pl.niewiel.pracadyplomowa.database.model;

import com.orm.dsl.Table;

@Table(name = "types_to_component")
public class TypesToComponent {
    private long componentId;
    private long typeId;

    private long mid;

    public TypesToComponent(Component component, ComponentType type) {
        this.componentId = component.getmId();
        this.typeId = type.getmId();
    }

    public TypesToComponent() {
    }

    @Override
    public String toString() {
        return "TypesToComponent{" +
                "componentId=" + componentId +
                ", typeId=" + typeId +
                '}';
    }

    public long getMid() {
        return mid;
    }

    public void setMid(long mid) {
        this.mid = mid;
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