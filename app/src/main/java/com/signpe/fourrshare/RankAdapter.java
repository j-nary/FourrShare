package com.signpe.fourrshare;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.signpe.fourrshare.model.ImageDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {

    interface RankInterface{
        void getIntent(Intent intent);
    }

    private Context context;
    private int lastPosition = -1;
    private boolean order;
    RankInterface rankInterface;
    private String state;
    private FirebaseFirestore firestore;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    public ArrayList<ImageInfo> imageInfos;
    public ArrayList<ImageDTO> imageDTOs;
    public ArrayList<String> imageUidList = new ArrayList<>();

    public RankAdapter(Context context,ArrayList<ImageInfo> imageInfos,boolean order) {

        this.context = context;
        this.imageInfos=imageInfos;
        this.order=order;

        if (order){
            state="likeCount";
        }
        else{
            state="timeStamp";
        }
        firestore = FirebaseFirestore.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        firestore.collection("images").whereEqualTo("isUpload",true).orderBy(state,Direction.DESCENDING).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots==null)
                            return;
                        try{
                            for(DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                ImageDTO item = snapshot.toObject(ImageDTO.class);
                                imageInfos.add(new ImageInfo(item,snapshot.getId()));
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
        ImageView likeButton;
        TextView textView;
        TextView usr_nickname;
        ImageView usr_icon;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image_view);
            likeButton = (ImageView) view.findViewById(R.id.like_button);
            textView = (TextView) view.findViewById(R.id.text_view);
            usr_nickname = view.findViewById(R.id.usr_nickname);
            usr_icon = view.findViewById(R.id.view_profile_image);
        }
    }

    //새로운 뷰 생성 2
    @NonNull
    @Override
    public RankAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public void setRankInterface(RankInterface rankInterface){
        this.rankInterface=rankInterface;
    }

    //RecyclerView의 getView 부분을 담당 3

    @Override
    public void onBindViewHolder(@NonNull RankAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Glide.with(context).load(imageInfos.get(position).getImageDTO().getImageUri()).into(holder.imageView);
        StorageReference ImageRef = storageRef.child("profile").child(imageInfos.get(position).getImageDTO().getUid()).child(imageInfos.get(position).getImageDTO().getUid()+".png");
        ImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.usr_icon);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.usr_icon.setImageResource(R.drawable.profile);
            }
        });

        holder.usr_nickname.setText(imageInfos.get(position).getImageDTO().getUserNickname());

        //아이콘, 닉네임 클릭 시 프로필로
        holder.usr_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context,UserFeedActivity.class);
                intent.putExtra("nickName",holder.usr_nickname.getText().toString());
                intent.putExtra("uid",imageInfos.get(position).getImageDTO().getUid());
                rankInterface.getIntent(intent);
            }
        });
        holder.usr_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context,UserFeedActivity.class);
                intent.putExtra("nickName",holder.usr_nickname.getText().toString());
                intent.putExtra("uid",imageInfos.get(position).getImageDTO().getUid());
                rankInterface.getIntent(intent);

            }
        });

        if(imageInfos.get(position).getImageDTO().getLikedPeople().containsKey(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            holder.likeButton.setImageResource(R.drawable.clickheart);
            holder.likeButton.setTag(R.drawable.clickheart);
        }
        else {
            holder.likeButton.setImageResource(R.drawable.nonclickheart);
            holder.likeButton.setTag(R.drawable.nonclickheart);
        }
        //다이얼로그 call
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BigImageDialog dialog = new BigImageDialog(holder.imageView.getContext(),holder.imageView);
                dialog.callFunction();
            }
        });
        //좋아요
        holder.textView.setText(String.valueOf(imageInfos.get(position).getImageDTO().getLikeCount()) );
        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //사용성 향상
                if ((int) (holder.likeButton.getTag()) == R.drawable.clickheart){
                    holder.likeButton.setImageResource(R.drawable.nonclickheart);
                    holder.textView.setText(String.valueOf(Integer.parseInt(holder.textView.getText().toString())-1));
                } else{
                    holder.likeButton.setImageResource(R.drawable.clickheart);
                    holder.textView.setText(String.valueOf(Integer.parseInt(holder.textView.getText().toString())+1));
                }

                //메인 코드
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Map<String,Boolean> like = new HashMap<>();
                like.put(uid,true);
                DocumentReference tsdoc = firestore.collection("images").document(imageInfos.get(position).getImguid());
                firestore.runTransaction(new Transaction.Function<Void>() {
                    @Nullable
                    @Override
                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                        ImageDTO imageDTO = transaction.get(tsdoc).toObject(ImageDTO.class);
                        if(imageDTO.getLikedPeople().containsKey(uid)){
                            imageDTO.setLikeCount(imageDTO.getLikeCount() - 1);
                            imageDTO.getLikedPeople().remove(uid);
                            imageInfos.get(position).setImageDTO(imageDTO);

                            firestore.collection("image").whereEqualTo("likedPeople",like).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    if (value!=null){
                                        holder.likeButton.setImageResource(R.drawable.nonclickheart);
                                        holder.likeButton.setTag(R.drawable.nonclickheart);
                                        holder.textView.setText(String.valueOf(imageDTO.getLikeCount()));
                                        return ;
                                    }

                                }
                            });
                        }
                        else{
                            imageDTO.setLikeCount(imageDTO.getLikeCount()+1);
                            imageDTO.getLikedPeople().put(uid,true);
                            imageInfos.get(position).setImageDTO(imageDTO);

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
//                            notifyDataSetChanged();

                        }

                        transaction.set(tsdoc,imageDTO);
                        return null;
                    }
                });

            }
        });
    }

    //Item 개수 반환
    @Override //1
    public int getItemCount() {
        return imageInfos.size();
    }

    //View 나올 때 Animation 주기


}