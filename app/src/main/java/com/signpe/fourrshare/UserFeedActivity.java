package com.signpe.fourrshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.signpe.fourrshare.model.ImageDTO;

import java.util.ArrayList;

public class UserFeedActivity extends AppCompatActivity {
    public RecyclerView feedRecyclerView;
    private RecyclerView.Adapter feedAdapter;
    private RecyclerView.LayoutManager feedLayoutManager;


    //FireBase 관련 선언. 건들면 어플 죽어요 !
    ArrayList<ImageDTO> imageDTOS = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);
        Intent intent = getIntent();


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        feedRecyclerView = findViewById(R.id.feed_recycler_view);
        feedRecyclerView.setHasFixedSize(true);

        feedLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        feedRecyclerView.setLayoutManager(feedLayoutManager);
        String uid= intent.getStringExtra("uid");
        TextView heartCount = findViewById(R.id.heart_counter);

        feedAdapter = new UserFeedAdapter(getApplicationContext(), imageDTOS,uid,heartCount);
        feedRecyclerView.setAdapter(feedAdapter);

        ImageView usrProfile = findViewById(R.id.feedIcon);
        StorageReference ImageRef = storageRef.child("profile").child(uid).child(uid+".png");
        ImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(UserFeedActivity.this).load(uri).into(usrProfile);
            }
        });
        String name = intent.getStringExtra("nickName");
        TextView usrNick = findViewById(R.id.feed_author);
        usrNick.setText(name+"님의 피드");

        TextView userNick2 = findViewById(R.id.feed_author_2);
        userNick2.setText(name+"님이 받은 총");







//        heartCount.setText(Integer.parseInt(feedAdapter.getHeartCnt()));
    }
}