package pl.niewiel.pracadyplomowa.database.model;

import com.orm.dsl.Column;
import com.orm.dsl.NotNull;
import com.orm.dsl.Table;
import com.orm.dsl.Unique;

import java.util.Date;


@Table
public class User {

    @Column(name = "date_add")
    private Date dateAdd;
    @Column(name = "date_edit")
    private Date dateEdit;
    @Column(name = "bs_id")
    private int bsId;
    @Unique
    private String name;
    private String password;
    @Unique
    private String email;
    @NotNull
    @Column(name = "is_logged_in")
    private boolean isLoggedIn;


    public User() {
    }

    public User(String name, String password, String email) {
        this.dateAdd=new Date();
        this.dateEdit=new Date();
        this.name = name;
        this.password = password;
        this.email = email;
        this.isLoggedIn=false;
    }

    @Override
    public String toString() {
        return "\nUser{" +
                "\ndateAdd=" + dateAdd +
                ", \ndateEdit=" + dateEdit +
                ", \nbsId=" + bsId +
                ", \nname='" + name + '\'' +
                ",\npassword='" + password + '\'' +
                ", \nmemail='" + email + '\'' +
                ", \nisLoggedIn=" + isLoggedIn +"\n"+
                '}';
    }

    public Date getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(Date dateAdd) {
        this.dateAdd = dateAdd;
    }

    public Date getDateEdit() {
        return dateEdit;
    }

    public void setDateEdit() {
        this.dateEdit = new Date();
    }

    public int getBsId() {
        return bsId;
    }

    public void setBsId(int bsId) {
        this.bsId = bsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
