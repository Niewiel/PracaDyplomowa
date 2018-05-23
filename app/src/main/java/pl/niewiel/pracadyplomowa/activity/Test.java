package pl.niewiel.pracadyplomowa.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.orm.SugarRecord;

import java.util.List;

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.adapters.TokenAdapter;
import pl.niewiel.pracadyplomowa.database.model.Token;

public class Test extends AppCompatActivity {

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        List<Token> tokens = SugarRecord.listAll(Token.class);
        if (!tokens.isEmpty()) {
            if (tokens.size() > 1) {
                final ListView listView = (ListView) findViewById(R.id.test_list);
                TokenAdapter tokenAdapter = new TokenAdapter(this, R.layout.row, tokens);
                listView.setAdapter(tokenAdapter);
            }
        }else {
            Toast.makeText(this,"No results",Toast.LENGTH_LONG).show();
            finish();
        }


    }
}
