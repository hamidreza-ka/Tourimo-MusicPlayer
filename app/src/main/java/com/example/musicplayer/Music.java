package com.example.musicplayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Music {
    private int musicResId;
    private String name;
    private String artist;
    private int coverResId;
    private int artistResId;
    private String backgroundColor;

    public int getMusicResId() {
        return musicResId;
    }

    public void setMusicResId(int musicResId) {
        this.musicResId = musicResId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getCoverResId() {
        return coverResId;
    }

    public void setCoverResId(int coverResId) {
        this.coverResId = coverResId;
    }

    public int getArtistResId() {
        return artistResId;
    }

    public void setArtistResId(int artistResId) {
        this.artistResId = artistResId;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public static List<Music> getList() {
        List<Music> musicList = new ArrayList<>();
        Music music1 = new Music();
        music1.setMusicResId(R.raw.music_1);
        music1.setArtist("Evan Band");
        music1.setName("Chehel Gis");
        music1.setArtistResId(R.drawable.music_1_artist);
        music1.setCoverResId(R.drawable.music_1_cover);
        music1.setBackgroundColor("R.color.background_color");



        Music music2= new Music();
        music2.setMusicResId(R.raw.music_2);
        music2.setArtist("Reza Sadeghi");
        music2.setName("Tanha Tarin");
        music2.setArtistResId(R.drawable.music_2_artist);
        music2.setCoverResId(R.drawable.music_2_cover);



        Music music3 = new Music();
        music3.setMusicResId(R.raw.music_3);
        music3.setArtist("Reza Bahram");
        music3.setName("Hich");
        music3.setArtistResId(R.drawable.music_3_artist);
        music3.setCoverResId(R.drawable.music_3_cover);

        musicList.add(music1);
        musicList.add(music2);
        musicList.add(music3);

        return musicList;
    }

    public static String convertMillToMin(long milliSecondDuration){
        long second = (milliSecondDuration / 1000) % 60;
        long minute = (milliSecondDuration / (1000 * 60)) % 60;

        return String.format(Locale.US,"%02d:%02d",minute,second);
    }



}
