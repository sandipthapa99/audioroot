package com.example.splashscreen;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.example.splashscreen.LocalMusicMain.musicFiles;

public class PlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    TextView song_name, artist_name, duration_played, duration_total;
    ImageView album_art, nextBtn, previousBtn,shuffleBtn, repeatBtn,back_button;
    FloatingActionButton playPauseBtn;
    SeekBar seekbar;

    int position=-1;
    static ArrayList<MusicFiles> songsList =new ArrayList<>();
    static Uri uri;
    static MediaPlayer mediaPlayer;

    //to add delay on seek
    private Handler handler = new Handler();

    private Thread playThread, nextThread, previousThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initView();
        getIntentMethod();
        song_name.setText(songsList.get(position).getTitle());
        artist_name.setText(songsList.get(position).getArtist());
        mediaPlayer.setOnCompletionListener(this);

        back_button=findViewById(R.id.back_button);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //changing seek bar by user
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //seeking by user
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress*1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //Automatically Seek bar progress with time
        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null){
                    int myCurrentPosition =mediaPlayer.getCurrentPosition() / 1000;
                    seekbar.setProgress(myCurrentPosition);
                    duration_played.setText(formattedTime(myCurrentPosition));
                }
                handler.postDelayed(this,1000);
            }
        });
    }

    @Override
    protected void onResume() {
        playThreadBtn();
        nextThreadBtn();
        previousThreadBtn();
        super.onResume();
    }

    private void previousThreadBtn() {
        previousThread=new Thread(){
            @Override
            public void run() {
                super.run();
                previousBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        previousBtnClicked();
                    }
                });
            }
        };
        previousThread.start();
    }

    private void previousBtnClicked() {
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            position =((position - 1) < 0 ? (songsList.size()-1) : (position - 1));
            uri = Uri.parse(songsList.get(position).getPath());
            mediaPlayer=MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            song_name.setText(songsList.get(position).getTitle());
            artist_name.setText(songsList.get(position).getArtist());

            seekbar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null){
                        int myCurrentPosition =mediaPlayer.getCurrentPosition() / 1000;
                        seekbar.setProgress(myCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playPauseBtn.setBackgroundResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }
        else{
            mediaPlayer.stop();
            mediaPlayer.release();
            position =((position - 1) < 0 ? (songsList.size()-1) : (position - 1));
            uri = Uri.parse(songsList.get(position).getPath());
            mediaPlayer=MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            song_name.setText(songsList.get(position).getTitle());
            artist_name.setText(songsList.get(position).getArtist());

            seekbar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null){
                        int myCurrentPosition =mediaPlayer.getCurrentPosition() / 1000;
                        seekbar.setProgress(myCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playPauseBtn.setBackgroundResource(R.drawable.ic_play_arrow);

        }
    }

    private void nextThreadBtn() {
        nextThread=new Thread(){
            @Override
            public void run() {
                super.run();
                nextBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        nextBtnClicked();
                    }
                });
            }
        };
        nextThread.start();

    }

    private void nextBtnClicked() {
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            position = (position + 1) % songsList.size();
            uri = Uri.parse(songsList.get(position).getPath());
            mediaPlayer=MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            song_name.setText(songsList.get(position).getTitle());
            artist_name.setText(songsList.get(position).getArtist());

            seekbar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null){
                        int myCurrentPosition =mediaPlayer.getCurrentPosition() / 1000;
                        seekbar.setProgress(myCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playPauseBtn.setBackgroundResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }
        else{
            mediaPlayer.stop();
            mediaPlayer.release();
            position = (position + 1) % songsList.size();
            uri = Uri.parse(songsList.get(position).getPath());
            mediaPlayer=MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            song_name.setText(songsList.get(position).getTitle());
            artist_name.setText(songsList.get(position).getArtist());

            seekbar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null){
                        int myCurrentPosition =mediaPlayer.getCurrentPosition() / 1000;
                        seekbar.setProgress(myCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playPauseBtn.setBackgroundResource(R.drawable.ic_play_arrow);

        }
    }

    private void playThreadBtn() {
        playThread=new Thread(){
            @Override
            public void run() {
                super.run();
                playPauseBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        playPauseBtnClicked();
                    }
                });
            }
        };
        playThread.start();
    }

    private void playPauseBtnClicked() {
        if (mediaPlayer.isPlaying()){
            playPauseBtn.setImageResource(R.drawable.ic_play_arrow);
            mediaPlayer.pause();

            seekbar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null){
                        int myCurrentPosition =mediaPlayer.getCurrentPosition() / 1000;
                        seekbar.setProgress(myCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
        }
        else{
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
            seekbar.setMax(mediaPlayer.getDuration()/1000);

            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null){
                        int myCurrentPosition =mediaPlayer.getCurrentPosition() / 1000;
                        seekbar.setProgress(myCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
        }

    }

    private String formattedTime(int myCurrentPosition) {
        String totalOut="";
        String totalNew="";
        String seconds=String.valueOf(myCurrentPosition % 60);
        String minutes =String.valueOf(myCurrentPosition / 60);
        totalOut = minutes + ":" +seconds;
        totalNew = minutes + ":" + "0" +seconds;
        if (seconds.length()==1){
            return totalNew;
        }
        else{
            return totalOut;
        }
    }

    private void getIntentMethod() {
        position= getIntent().getIntExtra("position",-1);
        songsList = musicFiles;
        if (songsList != null){
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            uri = Uri.parse(songsList.get(position).getPath());
        }
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer= MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }
        else{
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }
        seekbar.setMax(mediaPlayer.getDuration() / 1000);
        metaData(uri);
    }

    private void initView() {
        song_name = findViewById(R.id.song_name);
        artist_name= findViewById(R.id.song_artist);
        duration_played = findViewById(R.id.duration_played);
        duration_total = findViewById(R.id.duration_Total);

        album_art = findViewById(R.id.album_art);
        nextBtn = findViewById(R.id.next);
        previousBtn = findViewById(R.id.previous);
        shuffleBtn = findViewById(R.id.shuffle);
        repeatBtn = findViewById(R.id.repeat);
        playPauseBtn = findViewById(R.id.play_pause);
        seekbar = findViewById(R.id.seek_bar);

    }

    private void metaData(Uri uri){
        MediaMetadataRetriever retriever =new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int durationTotal = Integer.parseInt(songsList.get(position).getDuration()) / 1000;
        duration_total.setText(formattedTime(durationTotal));
        byte[] albumArt =retriever.getEmbeddedPicture();
        Bitmap bitmap;

        if (albumArt != null){

            //To change colour of player according to album art
            bitmap = BitmapFactory.decodeByteArray(albumArt,0,albumArt.length);
            ImageAnimation(this,album_art, bitmap);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@Nullable Palette palette) {
                    Palette.Swatch swatch = palette.getDominantSwatch();
                    if (swatch != null){
                        ImageView gradient = findViewById(R.id.album_art_gradient);
                        RelativeLayout myContainer = findViewById(R.id.myContainer);
                        gradient.setBackgroundResource(R.drawable.gradient_bg);
                        myContainer.setBackgroundResource(R.drawable.main_bg);
                        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{swatch.getRgb(),0x00000000});
                        gradient.setBackground(gradientDrawable);

                        GradientDrawable gradientDrawableBg = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{swatch.getRgb(),swatch.getRgb()});
                        myContainer.setBackground(gradientDrawableBg);
                        //changing text color
                        song_name.setTextColor(swatch.getTitleTextColor());
                        artist_name.setTextColor(swatch.getBodyTextColor());
                    }
                    else{
                        ImageView gradient = findViewById(R.id.album_art_gradient);
                        RelativeLayout myContainer = findViewById(R.id.myContainer);
                        gradient.setBackgroundResource(R.drawable.gradient_bg);
                        myContainer.setBackgroundResource(R.drawable.main_bg);
                        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{0xff000000,0x00000000});
                        gradient.setBackground(gradientDrawable);

                        GradientDrawable gradientDrawableBg = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{0xff000000,0xff000000});
                        myContainer.setBackground(gradientDrawableBg);
                        //changing text color
                        song_name.setTextColor(Color.WHITE);
                        artist_name.setTextColor(Color.DKGRAY);
                    }
                }
            });
        }
        else{
            Glide.with(this).asBitmap().load(R.drawable.album_art).into(album_art);
            ImageView gradient = findViewById(R.id.album_art_gradient);
            RelativeLayout myContainer = findViewById(R.id.myContainer);
            gradient.setBackgroundResource(R.drawable.gradient_bg);
            myContainer.setBackgroundResource(R.drawable.main_bg);
            song_name.setTextColor(Color.WHITE);
            artist_name.setTextColor(Color.DKGRAY);
        }
    }

    //Fade in and fade out on song change
    public void ImageAnimation(final Context context, final ImageView imageView, final Bitmap bitmap){
        Animation animOut = AnimationUtils.loadAnimation(context,android.R.anim.fade_out);
        final Animation animIn = AnimationUtils.loadAnimation(context,android.R.anim.fade_in);

        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Glide.with(context).load(bitmap).into(imageView);
                animIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                imageView.startAnimation(animIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(animOut);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        nextBtnClicked();
        if (mediaPlayer != null){
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(this);
        }
    }
}
