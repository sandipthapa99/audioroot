package com.example.splashscreen;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MostPlayedAdapter extends RecyclerView.Adapter<MostPlayedAdapter.MyViewHolder> {
    private Context myContext;
    private ArrayList<MusicFiles> myFiles;

    MostPlayedAdapter(Context myContext, ArrayList<MusicFiles> myFiles){
        this.myContext= myContext;
        this.myFiles=myFiles;
    }

    @NonNull
    @Override
    public MostPlayedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.music_items,parent,false);
        return new MostPlayedAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MostPlayedAdapter.MyViewHolder holder, final int position) {
        holder.file_name.setText(myFiles.get(position).getTitle());
        byte[] image= getAlbumArt(myFiles.get(position).getPath());

        if (image != null){
            Glide.with(myContext).asBitmap().load(image).into(holder.album_art);
        }
        else{
            Glide.with(myContext). load(R.drawable.album_art).into(holder.album_art);
        }
        //opening main player on clicking song in recycler view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myContext, PlayerActivity.class);
                intent.putExtra("position",position);
                myContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView file_name;
        ImageView album_art;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            file_name=itemView.findViewById(R.id.music_file_name);
            album_art=itemView.findViewById(R.id.music_image);
        }
    }

    private byte[] getAlbumArt(String uri){
        MediaMetadataRetriever retriever =new MediaMetadataRetriever();
        retriever.setDataSource(uri);

        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}
