package com.signpe.fourrshare;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.signpe.fourrshare.model.ImageDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserFeedAdapter extends RecyclerView.Adapter<UserFeedAdapter.ViewHolder> {

    private TextView heartcnt;
    private int heartCnt=0;
    private Context context;
    private int lastPosition = -1;
    private boolean order;
    private String state;
    private boolean complete=false;
    private FirebaseFirestore firestore;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    ArrayList<ImageDTO> imageDTOs;
    public ArrayList<String> imageUidList = new ArrayList<>();

    public UserFeedAdapter(Context context,ArrayList<ImageDTO> imageDTOS,String uid,TextView heartcnt) {

        this.context = context;
        this.imageDTOs = imageDTOS;
        this.heartcnt=heartcnt;

        firestore = FirebaseFirestore.getInstance();
        firestore.collection("images").whereEqualTo("isUpload", true).whereEqualTo("uid",uid).orderBy("timeStamp").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots == null)
                            return;
                        try {
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                ImageDTO item = snapshot.toObject(ImageDTO.class);
                                imageDTOS.add(item);
                                imageUidList.add(snapshot.getId());
                                heartCnt += item.getLikeCount();
                                heartcnt.setText(String.valueOf(heartCnt));
                            }

                        } catch (Exception e) {

                        }
                        notifyDataSetChanged();
                    }
                });
    }

    public int getHeartCnt(){
        return heartCnt;
    }

    public boolean getcomplete(){
        return complete;
    }




    public static class ViewHolder extends RecyclerView.ViewHolder {


        ImageView imageView;
        ImageView likeButton;
        TextView textView;
        TextView usr_nickname;
        ImageView usr_icon;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image_view);
            likeButton = (ImageView) view.findViewById(R.id.like_button);
            textView = (TextView) view.findViewById(R.id.text_view);
        }
    }


    @NonNull
    @Override
    public UserFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent,false);
        UserFeedAdapter.ViewHolder viewHolder = new UserFeedAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull UserFeedAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(imageDTOs.get(position).getImageUri()).into(holder.imageView);


        if(imageDTOs.get(position).getLikedPeople().containsKey(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            holder.likeButton.setImageResource(R.drawable.clickheart);
            holder.likeButton.setTag(R.drawable.clickheart);
        }
        else {
            holder.likeButton.setImageResource(R.drawable.nonclickheart);
            holder.likeButton.setTag(R.drawable.nonclickheart);
        }
        holder.textView.setText(String.valueOf(imageDTOs.get(position).getLikeCount()) );

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BigImageDialog dialog = new BigImageDialog(holder.imageView.getContext(),holder.imageView);
                dialog.callFunction();
            }
        });

        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((int) (holder.likeButton.getTag()) == R.drawable.clickheart){
                    holder.likeButton.setImageResource(R.drawable.nonclickheart);
                    holder.textView.setText(String.valueOf(Integer.parseInt(holder.textView.getText().toString())-1));
                } else{
                    holder.likeButton.setImageResource(R.drawable.clickheart);
                    holder.textView.setText(String.valueOf(Integer.parseInt(holder.textView.getText().toString())+1));
                }

                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Map<String,Boolean> like = new HashMap<>();
                like.put(uid,true);
                DocumentReference tsdoc = firestore.collection("images").document(imageUidList.get(position));
                firestore.runTransaction(new Transaction.Function<Void>() {
                    @Nullable
                    @Override
                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                        ImageDTO imageDTO = transaction.get(tsdoc).toObject(ImageDTO.class);
                        if(imageDTO.getLikedPeople().containsKey(uid)){
                            imageDTO.setLikeCount(imageDTO.getLikeCount() - 1);
                            imageDTO.getLikedPeople().remove(uid);

                            firestore.collection("image").whereEqualTo("likedPeople",like).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    if (value!=null){
                                        holder.likeButton.setImageResource(R.drawable.nonclickheart);
                                        holder.likeButton.setTag(R.drawable.nonclickheart);
                                        holder.textView.setText(String.valueOf(imageDTO.getLikeCount()));
                                        return ;
                                    }

                                    notifyDataSetChanged();
                                }
                            });
                        }
                        else{
                            imageDTO.setLikeCount(imageDTO.getLikeCount()+1);
                            imageDTO.getLikedPeople().put(uid,true);
                            firestore.collection("images").whereEqualTo("likedPeople",like).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    if (value!=null){
                                        holder.likeButton.setImageResource(R.drawable.clickheart);
                                        holder.likeButton.setTag(R.drawable.clickheart);
                                        holder.textView.setText(String.valueOf(imageDTO.getLikeCount()));

                                        return ;
                                    }
                                    notifyDataSetChanged();
                                }
                            });
                        }

                        transaction.set(tsdoc,imageDTO);
                        return null;
                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return imageDTOs.size();
    }
}
