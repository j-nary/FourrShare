package com.signpe.fourrshare;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.signpe.fourrshare.model.ImageDTO;

import java.util.ArrayList;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {
    private Context context;
    private ArrayList<RankItem> items;
    private int lastPosition = -1;

    private FirebaseFirestore firestore;
    ArrayList<ImageDTO> imageDTOs;
    public ArrayList<String> imageUidList = new ArrayList<>();

    public RankAdapter(Context context,ArrayList<ImageDTO> imageDTOS) {
        this.items = items;
        this.context = context;

        this.imageDTOs=imageDTOS;
        firestore = FirebaseFirestore.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firestore.collection("images").whereEqualTo("isUpload",true).orderBy("timeStamp").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots==null)
                            return;
                        try{
                            for(DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                ImageDTO item = snapshot.toObject(ImageDTO.class);
                                imageDTOS.add(item);
                                imageUidList.add(snapshot.getId());
                            }
                        }
                        catch (Exception e){

                        }
                        notifyDataSetChanged();
                    }
                });
    }

    //뷰 바인딩 부분을 한번만 하도록, ViewHolder 패턴 의무화
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(View view) {
            super(view);

            imageView = (ImageView) view.findViewById(R.id.image_view);
            textView = (TextView) view.findViewById(R.id.text_view);
        }
    }

    //새로운 뷰 생성
    @NonNull
    @Override
    public RankAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    //RecyclerView의 getView 부분을 담당
    @Override
    public void onBindViewHolder(@NonNull RankAdapter.ViewHolder holder, int position) {
        Glide.with(holder.itemView).load(imageDTOs.get(position).getImageUri()).into(holder.imageView);
        // 아래 코드 뭔지 모르는데 터지길래 버림
        holder.textView.setText(String.valueOf(imageDTOs.get(position).getLikeCount()) );

        setAnimation(holder.imageView, position);
    }

    //Item 개수 반환
    @Override
    public int getItemCount() {
        return imageDTOs.size();
    }

    //View 나올 때 Animation 주기
    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.setAnimation(animation);
            lastPosition = position;
        }
    }
}