package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import com.example.musicplayer.databinding.ActivityMainBinding;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.slider.Slider;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements MusicAdapter.MusicEventListener {
    private ActivityMainBinding binding;
    private List<Music> musicList;
    private MediaPlayer mediaPlayer;
    private MusicState musicState = MusicState.STOPPED;
    private MusicAdapter musicAdapter;
    private Timer timer;
    private int currentPosition = 0;
    private boolean isDragging;


    enum MusicState {
        PLAYING, PAUSED, STOPPED
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(this);
        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setContentView(binding.getRoot());
        musicList = Music.getList();

        RecyclerView recyclerView = findViewById(R.id.rv_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        musicAdapter = new MusicAdapter(musicList, this);
        recyclerView.setAdapter(musicAdapter);

        onMusicChange(musicList.get(currentPosition));


        // Music State
        binding.btnPlayMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (musicState) {

                    case PAUSED:
                    case STOPPED:
                        mediaPlayer.pause();
                        musicState = MusicState.PLAYING;
                        binding.btnPlayMusic.setImageResource(R.drawable.ic_play_32dp);

                        break;
                    case PLAYING:
                        mediaPlayer.start();
                        musicState = MusicState.STOPPED;
                        binding.btnPlayMusic.setImageResource(R.drawable.ic_pause_24);

                        break;
                }
            }
        });

        binding.musicSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                binding.positionTv.setText(Music.convertMillToMin((long) value));
            }
        });

        binding.musicSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
                isDragging = true;
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                isDragging = false;
                mediaPlayer.seekTo((int) slider.getValue());
            }
        });

        // Next Music
        binding.btnGoNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNext();
            }
        });

        // Previous Music
        binding.btnGoPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPrevious();
            }
        });
    }

    // Change Music
    private void onMusicChange(Music music) {

        musicAdapter.notifyMusicItemChanged(music);
        binding.musicSlider.setValue(0);


        mediaPlayer = MediaPlayer.create(this, music.getMusicResId());
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.positionTv.setText(Music.convertMillToMin(mediaPlayer.getCurrentPosition()));
                                if (!isDragging) {
                                    binding.musicSlider.setValue(mediaPlayer.getCurrentPosition());
                                }
                            }
                        });
                    }
                }, 1000, 1000);
                binding.durationTv.setText(Music.convertMillToMin(mediaPlayer.getDuration()));
                binding.musicSlider.setValueTo(mediaPlayer.getDuration());
                // musicState = MusicState.PLAYING;
                binding.btnPlayMusic.setImageResource(R.drawable.ic_pause_24);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        goNext();
                    }
                });
            }

        });


        binding.artistIv.setActualImageResource(music.getArtistResId());
        binding.artistNameTv.setText(music.getArtist());
        binding.coverIv.setActualImageResource(music.getCoverResId());
        binding.musicTv.setText(music.getName());
    }

    @Override
    public void onMusicClickListener(Music music) {
        timer.cancel();
        mediaPlayer.release();
        mediaPlayer = null;
        onMusicChange(music);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    public void goNext() {
        timer.cancel();
        timer.purge();
        mediaPlayer.release();
        if (currentPosition < musicList.size() - 1) {
            currentPosition += 1;
        } else
            currentPosition = 0;

        onMusicChange(musicList.get(currentPosition));

    }

    public void goPrevious() {
        timer.cancel();
        timer.purge();
        mediaPlayer.release();
        if (currentPosition >= 1) {
            currentPosition -= 1;
        } else
            currentPosition = 2;

        onMusicChange(musicList.get(currentPosition));

    }


}