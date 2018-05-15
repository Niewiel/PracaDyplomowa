package pl.niewiel.pracadyplomowa.database.model;

import com.orm.dsl.Table;

import java.util.Date;

@Table
public class Token {
    private String access_token;
    private String expires_in;
    private String token_type;
    private String refresh_token;
    private Date acces_date;

    public Token() {
    }

    public Token(String access_token, String expires_in, String token_type/*, String refresh_token*/) {
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.token_type = token_type;
        this.acces_date=new Date();
        // this.refresh_token = refresh_token;
    }

    @Override
    public String toString() {
        return "\nToken{" +
                "\naccess_token='" + access_token + '\'' +
                ",\nexpires_in='" + expires_in + '\'' +
                ",\ntoken_type='" + token_type + '\'' +
                ",\nrefresh_token='" + refresh_token + '\'' +
                ",\nacces_date=" + acces_date +
                '}';
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public Date getAcces_date() {
        return acces_date;
    }
}
