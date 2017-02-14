package com.example.yamirtainwala.cis350hw2travelingsalesman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/*
 * Created by yamirtainwala on 2/6/17.
 */

public class GameActivity extends AppCompatActivity {
    private int num_locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_game);
        num_locations = Integer.parseInt(getIntent().getStringExtra("Num_locations"));
        GameView gv = (GameView) findViewById(R.id.gameView);
        gv.setSpinner_num(num_locations);
    }

    public void endGame(View view) {
        Intent i = new Intent();
//        i.putExtra(“NUM_CLICKS”, num_clicks);
        setResult(RESULT_OK, i);
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        return true;
    }

    // Response to menu item selection
    public boolean onOptionsItemSelected(MenuItem item) {
        GameView gv = (GameView) findViewById(R.id.gameView);
        switch (item.getItemId()) {
            case R.id.about:
                Toast.makeText(
                        this,
                        "Created for CIS 350 by Yamir Tainwala" +
                                "\n\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnjoy!!!",
                        Toast.LENGTH_LONG)
                        .show();
                return true;
            case R.id.clear:
                gv.clear();
                return true;
            case R.id.undo:
                if (!gv.undo()) {
                    Toast.makeText(
                            this,
                            "No Journeys Made",
                            Toast.LENGTH_LONG)
                            .show();
                }
                return true;
            case R.id.quit:
                endGame(gv);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
