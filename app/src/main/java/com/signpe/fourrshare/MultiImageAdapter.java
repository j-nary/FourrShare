package com.signpe.fourrshare;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.signpe.fourrshare.model.ImageDTO;

import java.util.ArrayList;

public class MultiImageAdapter extends RecyclerView.Adapter<MultiImageAdapter.ViewHolder>{
    private FirebaseFirestore firestore;
    private ArrayList<Uri> mData = null ;
    private Context mContext = null ;
    private int lastPosition = -1;

    ArrayList<ImageDTO> imageDTOs;
    public ArrayList<String> imageUidList = new ArrayList<>();

    ExtensionDialog dlog;


    // 생성자에서 데이터 리스트 객체, Context를 전달받음.
    MultiImageAdapter(Context context, ArrayList<ImageDTO> imageDTOS) {
//        mData = list ;
        mContext = context;
        this.imageDTOs=imageDTOS;
        firestore = FirebaseFirestore.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firestore.collection("images").whereEqualTo("uid",uid).orderBy("timeStamp").get()
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

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;

        ViewHolder(View itemView) {
            super(itemView) ;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ExtensionDialog exDialog = new ExtensionDialog(image.getContext(), image);
                    exDialog.callFunction();
                }
            });
            // 뷰 객체에 대한 참조.
            image = itemView.findViewById(R.id.image);
        }

    }


    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    // LayoutInflater - XML에 정의된 Resource(자원) 들을 View의 형태로 반환.
    @Override
    public MultiImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;    // context에서 LayoutInflater 객체를 얻는다.
        View view = inflater.inflate(R.layout.image_item, parent, false) ;	// 리사이클러뷰에 들어갈 아이템뷰의 레이아웃을 inflate.
        MultiImageAdapter.ViewHolder vh = new MultiImageAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(MultiImageAdapter.ViewHolder holder, int position) {
//        Uri image_uri = mData.get(position) ;
        Glide.with(holder.itemView).load(imageDTOs.get(position).getImageUri()).into(holder.image);
        setAnimation(holder.image, position);
//        Glide.with(mContext)
//                .load(image_uri)
//                .into(holder.image);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
//        return mData.size() ;
        return imageDTOs.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.setAnimation(animation);
            lastPosition = position;
        }
    }

}
