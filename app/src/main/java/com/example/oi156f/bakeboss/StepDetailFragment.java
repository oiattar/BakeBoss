package com.example.oi156f.bakeboss;


import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oi156f.bakeboss.components.Recipe;
import com.example.oi156f.bakeboss.components.Step;
import com.example.oi156f.bakeboss.utilities.RecipeUtils;
import com.google.android.exoplayer2.C;
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
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailFragment extends Fragment implements ExoPlayer.EventListener, View.OnClickListener {

    private static final String TAG = StepDetailFragment.class.getSimpleName();

    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";
    private final String STATE_PLAY_READY = "playWhenReady";

    @BindView(R.id.step_title)
    TextView stepTitle;
    @BindView(R.id.step_instruction)
    TextView stepInstruction;
    @BindView(R.id.video_frame)
    FrameLayout videoFrame;
    @BindView(R.id.step_video)
    SimpleExoPlayerView stepVideo;
    @BindView(R.id.previous_step_button)
    Button previousButton;
    @BindView(R.id.next_step_button)
    Button nextButton;
    @BindView(R.id.step_image)
    ImageView stepImage;

    private SimpleExoPlayer mExoPlayer;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private boolean mExoPlayerFullscreen = false;
    private Dialog mFullScreenDialog;

    private int mResumeWindow;
    private long mResumePosition;

    private Unbinder unbinder;

    private Recipe recipe = null;
    int position = 0;
    private Step[] steps = null;
    private Step selectedStep = null;
    private String videoSource = null;
    private boolean mTwoPane = false;
    private boolean playWhenReady = true;

    public StepDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
            selectedStep = savedInstanceState.getParcelable(getString(R.string.selected_step_intent_tag));
            playWhenReady = savedInstanceState.getBoolean(STATE_PLAY_READY);
        }
        initFullscreenDialog();
        Intent intent = getActivity().getIntent();
        if(!mTwoPane && intent.hasExtra(getString(R.string.selected_recipe_intent_tag))) {
            recipe = intent.getParcelableExtra(getString(R.string.selected_recipe_intent_tag));
            getActivity().setTitle(recipe.getName());
            position = intent.getIntExtra(getString(R.string.selected_step_intent_tag), 0);
        }
        if (recipe != null) {
            steps = recipe.getSteps();
            setupStepDetails();
            previousButton.setOnClickListener(this);
            nextButton.setOnClickListener(this);
            initializeMediaSession();
            videoSource = selectedStep.getVideoUrl();
            initializePlayer(videoSource);
        }
        return rootView;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.previous_step_button)
            position--;
        else if (view.getId() == R.id.next_step_button)
            position++;

        setupStepDetails();
        if (mExoPlayer != null)
            releasePlayer();
        initializePlayer(videoSource);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);
        outState.putBoolean(STATE_PLAY_READY, playWhenReady);
        outState.putParcelable(getString(R.string.selected_step_intent_tag), selectedStep);

        super.onSaveInstanceState(outState);
    }

    private void setupStepDetails() {
        selectedStep = steps[position];
        stepTitle.setText(selectedStep.getTitle());
        stepInstruction.setText(selectedStep.getDescription());
        videoSource = selectedStep.getVideoUrl();
        changeButtonState(previousButton, position != 0);
        changeButtonState(nextButton, position != steps.length - 1);
    }

    public void setTwoPane(boolean twoPane) {
        mTwoPane = twoPane;
    }

    private void changeButtonState(Button button, boolean enable) {
        button.setEnabled(enable);
        if (enable)
            button.getBackground().clearColorFilter();
        else
            button.getBackground().setColorFilter
                    (getResources().getColor(android.R.color.darker_gray), PorterDuff.Mode.MULTIPLY);
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            stepTitle.setVisibility(View.GONE);
            stepInstruction.setVisibility(View.GONE);
            previousButton.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
            openFullscreenDialog();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            stepTitle.setVisibility(View.VISIBLE);
            stepInstruction.setVisibility(View.VISIBLE);
            previousButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);
            closeFullscreenDialog();
        }
    }

    private void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }

    private void openFullscreenDialog() {

        ((ViewGroup) stepVideo.getParent()).removeView(stepVideo);
        mFullScreenDialog.addContentView(stepVideo, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }


    private void closeFullscreenDialog() {

        ((ViewGroup) stepVideo.getParent()).removeView(stepVideo);
        videoFrame.addView(stepVideo);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
    }

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getActivity(), TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    /**
     * Initialize ExoPlayer.
     * @param videoSource The URL of the sample to play.
     */
    private void initializePlayer(String videoSource) {
        if (videoSource.isEmpty()) {
            String imageUrl = selectedStep.getThumbnailUrl();
            if (imageUrl.isEmpty())
                imageUrl = RecipeUtils.NO_VIDEO_URL;
            Picasso.with(getActivity())
                    .load(imageUrl)
                    .error(R.drawable.image_error)
                    .into(stepImage);
            videoFrame.setVisibility(View.INVISIBLE);
            stepImage.setVisibility(View.VISIBLE);
        } else if (mExoPlayer == null) {
            videoFrame.setVisibility(View.VISIBLE);
            stepImage.setVisibility(View.GONE);
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            stepVideo.setPlayer(mExoPlayer);

            mExoPlayer.addListener(this);
            boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;

            if (haveResumePosition) {
                stepVideo.getPlayer().seekTo(mResumeWindow, mResumePosition);
            }

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getActivity(), "BakeBoss");
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoSource), new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onResume() {

        super.onResume();

        if (stepVideo == null) {
            initFullscreenDialog();
        }

        initializePlayer(videoSource);

        if (mExoPlayerFullscreen) {
            ((ViewGroup) stepVideo.getParent()).removeView(stepVideo);
            mFullScreenDialog.addContentView(stepVideo, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mFullScreenDialog.show();
        }
    }


    @Override
    public void onPause() {

        super.onPause();

        if (stepVideo != null && stepVideo.getPlayer() != null) {
            mResumeWindow = stepVideo.getPlayer().getCurrentWindowIndex();
            mResumePosition = Math.max(0, stepVideo.getPlayer().getCurrentPosition());
            playWhenReady = stepVideo.getPlayer().getPlayWhenReady();

            stepVideo.getPlayer().release();
        }

        if (mFullScreenDialog != null)
            mFullScreenDialog.dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mExoPlayer != null)
            releasePlayer();
        mMediaSession.setActive(false);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }
}
