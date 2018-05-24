package pl.niewiel.pracadyplomowa.view.token;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.orm.SugarRecord;

import java.util.List;
import java.util.Objects;

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.database.model.Token;

public class TokenView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.list);
        List<Token> tokens = SugarRecord.listAll(Token.class);
        if (!tokens.isEmpty()) {
            final ListView listView = findViewById(R.id.list);
            TokenAdapter tokenAdapter = new TokenAdapter(this, R.layout.token_row, tokens);
            listView.setAdapter(tokenAdapter);
        }else {
            Toast.makeText(this,"No results",Toast.LENGTH_LONG).show();
            finish();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
