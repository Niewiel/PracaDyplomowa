package pl.niewiel.pracadyplomowa.fragments.add_edit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import pl.niewiel.pracadyplomowa.R;

public class AddOrEditComponent extends AppCompatActivity {
    private static final int PICKFILE_REQUEST_CODE = 3;
    private TextInputEditText name;
    private Button add;
    private Button chose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_component);
        name = findViewById(R.id.name);
        add = findViewById(R.id.add_photo_button);
        chose = findViewById(R.id.chose_file_button);
        chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*.");
                startActivityForResult(intent, 1);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("DATA", data.toString());
    }
}
