package com.soundhub.ricardo.soundhub;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.software.shell.fab.ActionButton;
import com.soundhub.ricardo.soundhub.fragments.GenresListFragment;
import com.soundhub.ricardo.soundhub.interfaces.OnPlayerStatusChanged;
import com.soundhub.ricardo.soundhub.models.TrackLookupResponse;


public class MainActivity extends Activity implements OnPlayerStatusChanged, View.OnClickListener, View.OnLongClickListener {


    private CardView playerContainer;
    private ActionButton actionButton;
    private TextView playerMessage;
    private ImageView trackCover;
    private GenresListFragment genresListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerMessage = (TextView) findViewById(R.id.player_status);
        playerContainer = (CardView) findViewById(R.id.player_layout);
        actionButton = (ActionButton) findViewById(R.id.action_button_play);
        trackCover = (ImageView) findViewById(R.id.track_cover);

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
    public void onPlayerStart(TrackLookupResponse trackLookupResponse) {
        actionButton.setEnabled(true);
        actionButton.show();
        actionButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause));
        playerMessage.setText(trackLookupResponse.getTitle() + "\n" + trackLookupResponse.getTag_list());

        if (trackLookupResponse.getArtwork_url() != null &&
                trackLookupResponse.getArtwork_url() != "") {
            trackCover.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(trackLookupResponse.getArtwork_url())
                    .placeholder(android.R.drawable.ic_dialog_info)
                    .crossFade()
                    .into(trackCover);

        } else {
            trackCover.setVisibility(View.GONE);
            trackCover.setBackground(getResources().getDrawable(android.R.drawable.btn_radio));
        }

    }

    @Override
    public void onPlayerBuffering() {
        actionButton.setEnabled(false);
    }

    @Override
    public void onPlayerPaused() {
        actionButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
    }

    @Override
    public void onPlayerStopped() {
        trackCover.setVisibility(View.GONE);
        actionButton.hide();
        playerMessage.setText("Player stopped");
    }

    @Override
    public void onListScroll(final int visibility) {
        //playerContainer.setVisibility(visibility);
    }


    @Override
    public void onClick(View v) {
        if (genresListFragment != null) {
            genresListFragment.onFabPlayTap();
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
