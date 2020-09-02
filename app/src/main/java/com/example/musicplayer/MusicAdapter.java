package com.example.musicplayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {
    private List<Music> musicList;
    private MusicEventListener musicEventListener;
    private int platingMusicPos = -1;

    public MusicAdapter(List<Music> musicList, MusicEventListener musicEventListener) {
        this.musicList = musicList;
        this.musicEventListener = musicEventListener;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MusicViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.music_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        holder.bindMusic(musicList.get(position));
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    public class MusicViewHolder extends RecyclerView.ViewHolder {

        private SimpleDraweeView simpleDraweeView;
        private TextView artistNameTv;
        private TextView musicNameTv;
        private LottieAnimationView lottieAnimationView;


        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);

            simpleDraweeView = itemView.findViewById(R.id.iv_music);
            artistNameTv = itemView.findViewById(R.id.tv_artist_name);
            musicNameTv = itemView.findViewById(R.id.tv_music_name);
            lottieAnimationView = itemView.findViewById(R.id.play_animation);


        }

        public void bindMusic(final Music music) {
            simpleDraweeView.setActualImageResource(music.getCoverResId());
            artistNameTv.setText(music.getArtist());
            musicNameTv.setText(music.getName());

            if (getAdapterPosition() == platingMusicPos) {
                lottieAnimationView.setVisibility(View.VISIBLE);

            } else
                lottieAnimationView.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    musicEventListener.onMusicClickListener(music);
                }
            });

        }

    }

    public void notifyMusicItemChanged(Music music) {
        int index = musicList.indexOf(music);
        if (index != -1) {
            if (index != platingMusicPos) {
                notifyItemChanged(platingMusicPos);
                platingMusicPos = index;
                notifyItemChanged(platingMusicPos);
            }

        }
    }

    public interface MusicEventListener {
        void onMusicClickListener(Music music);
    }

}
