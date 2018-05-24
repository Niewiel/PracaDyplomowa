package pl.niewiel.pracadyplomowa.database.model;

import com.orm.dsl.Column;

import java.sql.Timestamp;

public class ComponentType {

    @Column(name="bs_id")
    private long bsId;
    private String name;
    @Column(name="date_add")
    private Timestamp dateAdd;
    private boolean sync=false;

    public ComponentType() {
    }

    public ComponentType(long bsId, String name, Timestamp dateAdd, boolean sync) {
        this.bsId = bsId;
        this.name = name;
        this.dateAdd = dateAdd;
        this.sync = sync;
    }

    @Override
    public String toString() {
        return "ComponentType{" +
                "bsId=" + bsId +
                ", name='" + name + '\'' +
                ", dateAdd=" + dateAdd +
                ", sync=" + sync +
                '}';
    }

    public long getBsId() {
        return bsId;
    }

    public void setBsId(long bsId) {
        this.bsId = bsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(Timestamp dateAdd) {
        this.dateAdd = dateAdd;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }
}
