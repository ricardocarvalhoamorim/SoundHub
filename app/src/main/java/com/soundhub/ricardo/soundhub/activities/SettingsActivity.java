package com.soundhub.ricardo.soundhub.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.soundhub.ricardo.soundhub.R;

public class SettingsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        TextView libraries = (TextView) findViewById(R.id.settings_libraries);
        libraries.setText(Html.fromHtml(
                "<a href=https://github.com/shell-software/fab>Floating Action Button</a> for displaying the media player buttons as well as their show/hide animations" +
                "<br /><br />" +
                "<a href=http://developer.android.com/training/volley/index.html>Volley library</a> to streamline the network-based application development" +
                "<br /><br />" +
                "<a href=https://github.com/google/ExoPlayer>ExoPlayer</a> A newly announced player to make ir easy (meh) to stream both audio and video from different sources"+
                "<br /><br /> ... as well as other important libraries such as Gson, Appcompat, CardView and RecyclerView"
        ));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
