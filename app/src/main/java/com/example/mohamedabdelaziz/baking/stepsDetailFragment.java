package com.example.mohamedabdelaziz.baking;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import static android.content.Context.NOTIFICATION_SERVICE;

public class stepsDetailFragment extends Fragment implements ExoPlayer.EventListener {

    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM_RECIPE = "item_recipe";
    ArrayList<steps> stepsArrayList;
    TextView descrp;
    public int index, recipe;
    Button next, prev;
    SimpleExoPlayerView simpleExoPlayerView;
    SimpleExoPlayer simpleExoPlayer;
    MediaSessionCompat mediaSessionCompat;
    PlaybackStateCompat.Builder builder;
    boolean tablet = false;
    AppBarLayout appBar;
    NotificationManager mNotificationManager;
    FloatingActionButton ingredents;

    public stepsDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        index = Integer.parseInt(bundle.getString(ARG_ITEM_ID, "1"));
        recipe = Integer.parseInt(bundle.getString(ARG_ITEM_RECIPE, "1"));
        database db = new database(getContext());
        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
        stepsArrayList = db.restore_steps(recipe);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            Activity activity = this.getActivity();
            appBar = (AppBarLayout) activity.findViewById(R.id.app_bar);
        }

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.steps_detail, container, false);
        next = (Button) view.findViewById(R.id.next_B);
        prev = (Button) view.findViewById(R.id.prev_B);
        descrp = (TextView) view.findViewById(R.id.description);
        ingredents = (FloatingActionButton) view.findViewById(R.id.ingredents);
        simpleExoPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.player);
        initializeMediaSession();
        initializePlayer();

        if (stepsListActivity.mTwoPane)
            tablet = true;


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index < stepsArrayList.size() - 1) {
                    index++;
                    setvediourl();
                }
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index > 0) {
                    index--;
                    setvediourl();
                }
            }
        });
        ingredents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), IngredentActivity.class);
                intent.putExtra(ARG_ITEM_RECIPE, recipe);
                startActivity(intent);
            }
        });
        return view;
    }

    private void setvediourl() {
        descrp.setText("" + stepsArrayList.get(index).getDescription());
        if (stepsArrayList.get(index).getVideoURL().isEmpty() || stepsArrayList.get(index).getVideoURL() == "")
            Toast.makeText(getContext(), "Url Not Detected", Toast.LENGTH_SHORT).show();
        else {
            String userAgent = Util.getUserAgent(getContext(), "baking");
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(stepsArrayList.get(index).getVideoURL())
                    , new DefaultDataSourceFactory(getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(true);
        }
    }


    private void initializePlayer() {
        if (simpleExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(simpleExoPlayer);
            setvediourl();
        }

    }

    private void initializeMediaSession() {
        String TAG = MainActivity.class.getSimpleName();
        mediaSessionCompat = new MediaSessionCompat(getContext(), TAG);

        mediaSessionCompat.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSessionCompat.setMediaButtonReceiver(null);
        builder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mediaSessionCompat.setPlaybackState(builder.build());
        mediaSessionCompat.setCallback(new mediasessioncall());
        mediaSessionCompat.setActive(true);

    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object o) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroupArray, TrackSelectionArray trackSelectionArray) {

    }

    @Override
    public void onLoadingChanged(boolean b) {

    }

    private void releasePlayer() {

        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
        }
        simpleExoPlayer = null;
    }

    @Override
    public void onPlayerError(ExoPlaybackException e) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            builder.setState(PlaybackStateCompat.STATE_PLAYING,
                    simpleExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            builder.setState(PlaybackStateCompat.STATE_PAUSED,
                    simpleExoPlayer.getCurrentPosition(), 1f);
        }
        mediaSessionCompat.setPlaybackState(builder.build());

    }

    @Override
    public void onPositionDiscontinuity() {
    }

    @Override
    public void onPause() {
        super.onPause();
        mediaSessionCompat.setActive(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeMediaSession();
        initializePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
        mediaSessionCompat.setActive(false);
    }


    class mediasessioncall extends MediaSessionCompat.Callback {
        public mediasessioncall() {
            super();

        }

        @Override
        public void onPlay() {
            super.onPlay();
            simpleExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            super.onPause();
            simpleExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            simpleExoPlayer.seekTo(0);
        }
    }

    public class myreciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mediaSessionCompat, intent);
        }
    }
}
