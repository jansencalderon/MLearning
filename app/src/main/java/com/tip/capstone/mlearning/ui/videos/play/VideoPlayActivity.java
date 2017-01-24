package com.tip.capstone.mlearning.ui.videos.play;

import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;

import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.Constant;
import com.tip.capstone.mlearning.databinding.ActivityVideoPlayBinding;

public class VideoPlayActivity extends AppCompatActivity {

    private static final String TAG = VideoPlayActivity.class.getSimpleName();
    private static final String POSITION = "video_current_position";
    private ActivityVideoPlayBinding binding;
    private MediaController mediaControls;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_play);
        int resourceId = getIntent().getIntExtra(Constant.RES_ID, -1);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) binding.activityVideoPlay.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.leftMargin = 0;
        binding.activityVideoPlay.setLayoutParams(params);

        //set the media controller buttons
        if (mediaControls == null) {
            mediaControls = new MediaController(this);
        }
        try {
            binding.activityVideoPlay.setMediaController(mediaControls);
            binding.activityVideoPlay.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + resourceId));
        } catch (Exception e) {
            Log.e(TAG, "onCreate: Error setting video", e);
        }

        binding.activityVideoPlay.requestFocus();
        //we also set an setOnPreparedListener in order to know when the video file is ready for playback
        binding.activityVideoPlay.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                binding.activityVideoPlay.seekTo(position);
                // TODO: 29/11/2016 videoview starts before seekTo is complete
                // TODO: 29/11/2016 http://stackoverflow.com/questions/7990784/videoview-not-playing-video-from-desired-position/11938019#11938019
                if (position == 0) {
                    binding.activityVideoPlay.start();
                } else {
                    binding.activityVideoPlay.pause();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        binding.activityVideoPlay.stopPlayback();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, binding.activityVideoPlay.getCurrentPosition());
        binding.activityVideoPlay.pause();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position = savedInstanceState.getInt(POSITION);
        binding.activityVideoPlay.seekTo(position);
    }

}
