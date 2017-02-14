package com.example.yamirtainwala.cis350hw2travelingsalesman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final int GameActivity_ID = 1;
    private Spinner locations_spinner;
    //private Button play_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButtonPress(View view) {
        locations_spinner = (Spinner) findViewById(R.id.spinner);
        int n = Integer.parseInt(String.valueOf(locations_spinner.getSelectedItem()));
        Intent i = new Intent(this, GameActivity.class);
        i.putExtra("Num_locations", "" + n);
        startActivityForResult(i, GameActivity_ID);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        setContentView(R.layout.activity_main);
        Toast.makeText(
                this,
                "Play again?",
                Toast.LENGTH_LONG)
                .show();
    }
}
