package pl.niewiel.pracadyplomowa.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.SugarRecord;

import java.util.Objects;

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.database.model.User;

public class MainActivity extends AppCompatActivity {

    private User user;

    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button tokens=findViewById(R.id.tokens);
        tokens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Test.class));
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Button login = findViewById(R.id.button);
        if (isLoggedIn()) {
            login.setText("Logout");
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.setLoggedIn(0);
                    SugarRecord.save(user);
                    recreate();

                }
            });
        } else
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToLogin();
                }
            });
    }

    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private boolean isLoggedIn() {
        try {
            user = SugarRecord.find(User.class, "is_logged_in=?", "1").get(0);
            Toast.makeText(getApplicationContext(), "hello " + user.getName(), Toast.LENGTH_LONG).show();
            return true;
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return false;
        }
    }

}
