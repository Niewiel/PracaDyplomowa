package pl.niewiel.pracadyplomowa.database.model;

import com.orm.dsl.Column;
import com.orm.dsl.Ignore;
import com.orm.dsl.Table;
import com.orm.dsl.Unique;

import java.sql.Timestamp;
import java.util.List;

@Table(name = "component")
public class Component {

    @Column(name = "mid")
    @Unique
    private long mId;
    @Column(name = "bs_id")
    @Unique
    private long bsId;
    private Building budynek;
    @Column(name = "date_add")
    private Timestamp dateAdd;
    @Column(name = "date_edit")
    private Timestamp dateEdit;
    private int status;
    private String name;
    private boolean sync = false;
    @Ignore
    private List<ComponentType> componentTypeList;


    public Component() {
        this.dateAdd = new Timestamp(System.currentTimeMillis());
    }

    public Component(long mId, Building budynek, Timestamp dateAdd, Timestamp dateEdit, int status, String name, boolean sync, List<ComponentType> componentTypeList) {
        this.mId = mId;
        this.budynek = budynek;
        this.dateAdd = dateAdd;
        this.dateEdit = dateEdit;
        this.status = status;
        this.name = name;
        this.sync = sync;
        this.componentTypeList = componentTypeList;
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

    public Building getBudynek() {
        return budynek;
    }

    public void setBudynek(Building budynek) {
        this.budynek = budynek;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }


}
