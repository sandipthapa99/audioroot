package com.example.splashscreen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.example.splashscreen.LocalMusicMain.musicFiles;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumFragment extends Fragment {
    RecyclerView recyclerView;
    MostPlayedAdapter MostPlayedAdapter;


    public AlbumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_album, container, false);
        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        if (!(musicFiles.size()<1)){
            MostPlayedAdapter = new MostPlayedAdapter(getContext(),musicFiles);
            recyclerView.setAdapter(MostPlayedAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        }
        return view;
    }
}
