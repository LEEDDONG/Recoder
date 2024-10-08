package yuhan.hgcq.client.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import yuhan.hgcq.client.R;
import yuhan.hgcq.client.model.dto.album.AlbumDTO;
import yuhan.hgcq.client.model.dto.member.MemberDTO;
import yuhan.hgcq.client.model.dto.photo.PhotoDTO;
import yuhan.hgcq.client.model.dto.team.TeamDTO;
import yuhan.hgcq.client.view.Photo;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private Map<String, List<PhotoDTO>> gallery;
    private List<String> dateList;
    private OnItemClickListener listener;
    private Context context;
    private boolean isPrivate;
    private MemberDTO loginMember;
    private TeamDTO teamDTO;
    private AlbumDTO albumDTO;

    public GalleryAdapter(Map<String, List<PhotoDTO>> gallery, Context context, boolean isPrivate,
                          MemberDTO loginMember, TeamDTO teamDTO, AlbumDTO albumDTO) {
        this.gallery = gallery;
        dateList = new ArrayList<>(gallery.keySet());
        dateList.sort(Comparator.naturalOrder());
        this.context = context;
        this.isPrivate = isPrivate;
        this.loginMember = loginMember;
        this.teamDTO = teamDTO;
        this.albumDTO = albumDTO;
    }

    public GalleryAdapter(Map<String, List<PhotoDTO>> gallery, Context context, boolean isPrivate,
                          MemberDTO loginMember, AlbumDTO albumDTO) {
        this.gallery = gallery;
        dateList = new ArrayList<>(gallery.keySet());
        dateList.sort(Comparator.naturalOrder());
        this.context = context;
        this.isPrivate = isPrivate;
        this.loginMember = loginMember;
        this.albumDTO = albumDTO;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class GalleryViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public RecyclerView photoListView;

        public GalleryViewHolder(@NonNull View view, OnItemClickListener listener) {
            super(view);

            date = view.findViewById(R.id.date);
            photoListView = view.findViewById(R.id.photoList);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(v, position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View galleryView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_date, parent, false);
        return new GalleryViewHolder(galleryView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        String photoDate = dateList.get(position);
        holder.date.setText(photoDate);

        List<PhotoDTO> photoList = gallery.get(photoDate);
        if(isPrivate){
            Log.i("Private PhotoList", photoList.size() + "");
        }
        GalleryPhotoAdapter gpa = new GalleryPhotoAdapter(photoList, context, isPrivate);
        gpa.setOnItemClickListener(new GalleryPhotoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PhotoDTO photoDTO = photoList.get(position);
                Intent photoPage = new Intent(context, Photo.class);
                if (isPrivate) {
                    photoPage.putExtra("isPrivate", true);
                }
                photoPage.putExtra("loginMember", loginMember);
                photoPage.putExtra("albumDTO", albumDTO);
                photoPage.putExtra("photoDTO", photoDTO);
                if(!isPrivate) {
                    photoPage.putExtra("teamDTO", teamDTO);
                }
                context.startActivity(photoPage);
            }
        });
        holder.photoListView.setAdapter(gpa);
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }
}
