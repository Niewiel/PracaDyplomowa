package pl.niewiel.pracadyplomowa.activity;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.orm.SugarRecord;

import java.util.List;
import java.util.Objects;

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.database.model.User;

public class RegisterActivity extends AppCompatActivity {
    String REGEX_PATTERN = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$";
    Button registerButton;
    private TextInputEditText usernameView;
    private TextInputEditText passwordView;
    private TextInputEditText emailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        usernameView = findViewById(R.id.login);
        passwordView = findViewById(R.id.password);
        emailView = findViewById(R.id.email);
        registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                        register();


            }
        });

    }

    private void register() {
        String username, password, email;
        username = usernameView.getText().toString();
        password = passwordView.getText().toString();
        email = emailView.getText().toString();
        User user;

        try {
            List<User> users = SugarRecord.find(User.class, "name=? ", username);
            user = users.get(0);
            usernameView.setError(getString(R.string.error_not_unique_username));
            emailView.setError(getString(R.string.error_not_unique_email));
        } catch (SQLiteException e) {
            Log.e("Login Error", e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            try {
                user = new User(username, password, email);
                SugarRecord.save(user);
                finish();
            } catch (SQLException f) {
                Log.e("SQL", f.getMessage());
            }
        }
    }


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        if (password.matches(REGEX_PATTERN))
            return true;
        else {
            passwordView.setError("password must contain \n1 uppercase letter\n1lowercaseletter\n1number or special character\nat least 8 characters");
            Log.e("Password", "Not valid");
            return false;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}
