package pl.niewiel.pracadyplomowa.database.model;

import com.orm.dsl.Column;
import com.orm.dsl.NotNull;
import com.orm.dsl.Table;
import com.orm.dsl.Unique;

import java.sql.Timestamp;



@Table
public class User {

    @Column(name = "bs_id")
    @Unique
    private int bsId;
    @Column(name = "date_add")
    private Timestamp dateAdd;
    @Column(name = "date_edit")
    private Timestamp dateEdit;
    @Unique
    private String name;
    private String password;
    @Unique
    private String email;
    @NotNull
    @Column(name = "is_logged_in")
    private int isLoggedIn = 0;


    public User() {
    }

    public User(String name, String password, String email) {
        this.dateAdd=new Timestamp(System.currentTimeMillis());
        this.name = name;
        this.password = password;
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "bsId=" + bsId +
                ", dateAdd=" + dateAdd +
                ", dateEdit=" + dateEdit +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", isLoggedIn=" + isLoggedIn +
                '}';
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

    public void setDateEdit() {
        this.dateEdit = new Timestamp(System.currentTimeMillis());
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

    public int isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(int loggedIn) {
        isLoggedIn = loggedIn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
