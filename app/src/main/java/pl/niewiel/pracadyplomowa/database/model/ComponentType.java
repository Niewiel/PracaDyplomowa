package pl.niewiel.pracadyplomowa.database.model;

import com.orm.dsl.Column;
import com.orm.dsl.Table;

import java.io.Serializable;
import java.sql.Timestamp;

@Table(name = "component_type")
public class ComponentType implements Serializable {

    @Column(name = "mid")

    private long mId;
    @Column(name="bs_id")

    private long bsId;
    private String name;
    @Column(name="date_add")
    private Timestamp dateAdd;
    @Column(name = "date_edit")
    private Timestamp dateEdit;
    private boolean sync=false;

    public ComponentType() {
    }

    public ComponentType(long bsId, String name, boolean sync) {
        this.bsId = bsId;
        this.name = name;
        this.sync = sync;
    }

    public ComponentType(String name) {
        this.name = name;
        this.dateAdd = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return name;
    }

    public long getmId() {
        return mId;
    }

    public void setmId(long mId) {
        this.mId = mId;
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

    public Timestamp getDateEdit() {
        return dateEdit;
    }

    public void setDateEdit(Timestamp dateEdit) {
        this.dateEdit = dateEdit;
    }

    public void setDateEdit() {
        this.dateEdit = new Timestamp(System.currentTimeMillis());
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }
}
