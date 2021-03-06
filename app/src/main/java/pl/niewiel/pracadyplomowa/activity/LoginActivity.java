package pl.niewiel.pracadyplomowa.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.orm.SugarRecord;

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.database.model.User;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    User user;
    // UI references.
    private TextInputEditText mUsernamelView;
    private TextInputEditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Set up the login form.
        mUsernamelView = findViewById(R.id.login);
        mLoginFormView = findViewById(R.id.login_form);
        mPasswordView = findViewById(R.id.password);
        mProgressView = findViewById(R.id.login_progress);
        Button loginButton = findViewById(R.id.login_button);
        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                goRegister();
            }
        });

        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUsernameValid(mUsernamelView.getText().toString()))
                    if (isPasswordValid()) {
                        user.setLoggedIn(1);
                        SugarRecord.save(user);
                        showProgress(isPasswordValid());
                    }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    private boolean isUsernameValid(String username) {
        try {
            user = SugarRecord.find(User.class, "name = ?", username).get(0);
            return true;
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            mUsernamelView.setError("username invalid");
            mPasswordView.setError("password invalid");
            Toast.makeText(getApplicationContext(), "username or password invalid", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private boolean isPasswordValid() {
        if (user.getPassword().equals(mPasswordView.getText().toString()))
            return true;
        else {
            mUsernamelView.setError("username invalid");
            mPasswordView.setError("password invalid");
            Toast.makeText(getApplicationContext(), "username or password invalid", Toast.LENGTH_LONG).show();
            return false;
        }
    }


    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                finish();

            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                finish();
            }
        });
    }

    private void goRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}

