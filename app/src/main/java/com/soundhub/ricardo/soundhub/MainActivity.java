package com.soundhub.ricardo.soundhub;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.software.shell.fab.ActionButton;
import com.soundhub.ricardo.soundhub.fragments.GenresListFragment;
import com.soundhub.ricardo.soundhub.interfaces.OnPlayerStatusChanged;

import java.util.Random;


public class MainActivity extends Activity implements OnPlayerStatusChanged, View.OnClickListener, View.OnLongClickListener {


    private LinearLayout playerContainer;
    private ActionButton actionButton;
    private TextView playerMessage;
    private GenresListFragment genresListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerMessage = (TextView) findViewById(R.id.player_status);
        actionButton = (ActionButton) findViewById(R.id.action_button);

        actionButton.hide();
        actionButton.setOnClickListener(this);
        actionButton.setOnLongClickListener(this);

        genresListFragment = new GenresListFragment();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, genresListFragment)
                    .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFeedbackAvailable(String message) {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        actionButton.show();
        playerMessage.setText(message);
        playerMessage.setBackgroundColor(color);
    }

    @Override
    public void onPlayerStart() {
        actionButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause));
    }

    @Override
    public void onPlayerPaused() {
        actionButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
    }

    @Override
    public void onPlayerStopped() {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        actionButton.hide();
        playerMessage.setText("STOPPED");
        playerMessage.setBackgroundColor(color);
    }

    @Override
    public void onClick(View v) {
        if (genresListFragment != null) {
            genresListFragment.onFabButtonTap();
        } else {
            Toast.makeText(this, "onTap error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (genresListFragment != null) {
            genresListFragment.onFabButtonLongTap();
        } else {
            Toast.makeText(this, "onTap error", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
