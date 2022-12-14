package com.signpe.fourrshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.signpe.fourrshare.model.ImageDTO;

import java.util.ArrayList;

public class ScrapActivity extends AppCompatActivity {
    private RecyclerView scrapRecyclerView;
    private ScrapAdapter scrapAdapter;
    private RecyclerView.LayoutManager scrapLayoutManager;
    Intent mintent;

    //FireBase 관련 선언. 건들면 어플 죽어요 !
    ArrayList<ImageDTO> imageDTOS = new ArrayList<>();
    ArrayList<ImageInfo> imageInfos = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrap);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        scrapRecyclerView = findViewById(R.id.scrap_recycler_view);
        scrapRecyclerView.setHasFixedSize(true);
        getIntent().setAction("Already created");

        scrapLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        scrapRecyclerView.setLayoutManager(scrapLayoutManager);

        scrapAdapter = new ScrapAdapter(getApplicationContext(),imageInfos);
        scrapRecyclerView.setAdapter(scrapAdapter);

        ((ScrapAdapter)scrapRecyclerView.getAdapter()).setScrapInterface(new ScrapAdapter.ScrapInterface() {
            @Override
            public void getIntent(Intent intent) {
                mintent=intent;
                startActivity(mintent);
            }
        });
}
    @Override
    protected void onResume() {
        Log.v("Example", "onResume");

        String action = getIntent().getAction();
        // Prevent endless loop by adding a unique action, don't restart if action is present
        if(action == null || !action.equals("Already created")) {
            Log.v("Example", "Force restart");
            Intent intent = new Intent(this, ScrapActivity.class);
            startActivity(intent);

            finish();
            overridePendingTransition(0,0);
        }
        // Remove the unique action so the next time onResume is called it will restart
        else
            getIntent().setAction(null);

        super.onResume();
    }

    // 네비게이션 바
    public void onClickNavigationBar(View v){
        if(v.getId() == R.id.galleryClickButton){

            startActivity(new Intent(getApplicationContext(),GalleryActivity.class));
            finish();
            overridePendingTransition(0,0);
        }
        else if(v.getId() == R.id.scanClickButton){
            startActivity(new Intent(getApplicationContext(),ScanActivity.class));
            finish();
            overridePendingTransition(0,0);
        }
        else if(v.getId() == R.id.rankClickButton){

            startActivity(new Intent(getApplicationContext(),RankActivity.class));
            finish();
            overridePendingTransition(0,0);
        }
        else if(v.getId() == R.id.scrapClickButton){
            startActivity(new Intent(getApplicationContext(),ScrapActivity.class));
            finish();
            overridePendingTransition(0,0);
        }
        else if(v.getId() == R.id.myinfoClickButton){
            startActivity(new Intent(getApplicationContext(),MyInfoActivity.class));
            finish();
            overridePendingTransition(0,0);
        }
    }


}
