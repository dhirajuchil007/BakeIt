package com.example.android.bakeit;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class StepsFragment extends Fragment {
    private PlayerView mPlayerView;
    private SimpleExoPlayer player;
    private boolean playWhenReady=true;
    private long playBAckPosition=0;
    private int currentWindow=0;
    private String videoLink;
    private String longDesc;
    private TextView stepNo;
    private TextView longDescTextview;
    private int id;
    public static final String STEP_NO="stepnumber";
    public static final String DESC="desc";
    public static final String VIDEO_LINK="videolink";
    public static final String STEP_LIST="steplist";
    private boolean mTwoPane;
    private ArrayList<Steps> stepsArrayList;




    public StepsFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.steps_fragment,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Bundle bundle=getArguments();
        mTwoPane=bundle.getBoolean(RecipeSteps.TWO_PANE);

        stepNo=view.findViewById(R.id.step_no);
        longDescTextview=view.findViewById(R.id.step_full_desc);
        if(savedInstanceState!=null)
        {
            id=savedInstanceState.getInt(STEP_NO);
            longDesc=savedInstanceState.getString(DESC);
            videoLink=savedInstanceState.getString(VIDEO_LINK);
            stepsArrayList=savedInstanceState.getParcelableArrayList(STEP_LIST);
        }
        if(getResources().getConfiguration().orientation!= Configuration.ORIENTATION_LANDSCAPE||mTwoPane) {
            stepNo.setText(getContext().getString(R.string.steps) + String.valueOf(id));
            longDescTextview.setText(longDesc);
        }

        mPlayerView=view.findViewById(R.id.step_video_player);
        Log.d(TAG, "initializePlayer: "+videoLink);
        final Button prevButton=view.findViewById(R.id.prev_button);
        Button nextButton=view.findViewById(R.id.next_button);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id!=1)
                {
                    Steps prevStep=stepsArrayList.get(id-2);
                    StepsFragment stepsFragment=new StepsFragment();
                    stepsFragment.setId(prevStep.id);
                    stepsFragment.setVideoLink(prevStep.videoUrl);
                    stepsFragment.setLongDesc(prevStep.description);
                    stepsFragment.setArguments(bundle);
                    stepsFragment.setStepList(stepsArrayList);
                    FragmentManager fragmentManager=getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.recipe_steps,stepsFragment).commit();



                }
                else {
                    Toast.makeText(getContext(),getContext().getResources().getString(R.string.fist_step_reached),Toast.LENGTH_SHORT).show();
                }
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id<stepsArrayList.size())
                {
                    Steps prevStep=stepsArrayList.get(id);
                    StepsFragment stepsFragment=new StepsFragment();
                    stepsFragment.setId(prevStep.id);
                    stepsFragment.setVideoLink(prevStep.videoUrl);
                    stepsFragment.setLongDesc(prevStep.description);
                    stepsFragment.setArguments(bundle);
                    stepsFragment.setStepList(stepsArrayList);
                    FragmentManager fragmentManager=getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.recipe_steps,stepsFragment).commit();
                }
                else
                {
                    Toast.makeText(getContext(),getContext().getResources().getString(R.string.last_step_reached),Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STEP_NO,id);
        outState.putString(DESC,longDesc);
        outState.putString(VIDEO_LINK,videoLink);
        outState.putParcelableArrayList(STEP_LIST,stepsArrayList);
    }

    private void initializePlayer(){
        player= ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext()),new DefaultTrackSelector(),new DefaultLoadControl());
        mPlayerView.setPlayer(player);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow,playBAckPosition);
        Uri uri= Uri.parse(videoLink);

        MediaSource mediaSource=buildMediaSource(uri);
        player.prepare(mediaSource,true,true);
    }
    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE&&!mTwoPane)
       hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }
    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        mPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }
    private void releasePlayer() {
        if (player != null) {
            playBAckPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc.substring(longDesc.indexOf(".")+1);

    }

    public void setId(int id) {

        this.id = id+1;
    }
    public void setStepList(ArrayList<Steps> stepList){

        stepsArrayList=stepList;

    }
}
