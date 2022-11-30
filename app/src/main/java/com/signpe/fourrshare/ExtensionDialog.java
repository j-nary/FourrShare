package com.signpe.fourrshare;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;

public class ExtensionDialog extends AppCompatActivity {
    private final Context context;
    private ImageView getImage;
    private int position;
    public ArrayList<String> imageUidList;
    private FirebaseFirestore firestore= FirebaseFirestore.getInstance();
    private CustomDialogListener customDialogListener;
    private boolean uploaded;
    Dialog dlg;

    interface CustomDialogListener{
        void onFresh(int position);

        void saveImage(ImageView images);

        void doUpload(int position);

        void cancelUpload(int position);
    }

    //호출할 리스너 초기화
    public void setDialogListener(CustomDialogListener customDialogListener){
        this.customDialogListener = customDialogListener;
    }

    public ExtensionDialog(Context context, ImageView image, int position, ArrayList<String> imageUidList, boolean uploaded) {
        this.context = context;
        getImage = image;
        this.position= position;
        this.imageUidList = imageUidList;
        this.uploaded=uploaded;

    }

    //호출할 다이얼로그 함수 정의
    public void callFunction() {
        //커스텀 다이얼로그 정의하기 위한 dialog클래스 생성
        dlg = new Dialog(context);
        Window window = dlg.getWindow();

        Rect displayRectangle = new Rect();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.activity_extension_dialog,null);
        layout.setMinimumWidth((int)(displayRectangle.width() * 0.9f));
        layout.setMinimumHeight((int)(displayRectangle.height() * 0.9f));
        dlg.setContentView(layout);
        dlg.show();

        ImageView iv = dlg.findViewById(R.id.extensionImage);
        iv.setImageDrawable(getImage.getDrawable());

        Switch switchButton =dlg. findViewById(R.id.sw);
        if(uploaded) switchButton.setChecked(true);

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // switchButton이 체크된 경우
                    customDialogListener.doUpload(position);
                } else {
                    // switchButton이 체크되지 않은 경우

                    customDialogListener.cancelUpload(position);
                }
            }
        });

        ImageView downBtn =dlg.findViewById(R.id.dialog_download);
        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListener.saveImage(getImage);
                DynamicToast.make(context,"갤러리에 저장되었습니다.", AppCompatResources.getDrawable(context,R.drawable.ic_baseline_save_24),Color.parseColor("#FFFFFF"),Color.parseColor("#2F9CF4")).show();

                dlg.dismiss();
            }
        });

        ImageView deleteBtn = dlg.findViewById(R.id.dialog_delete);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firestore.collection("images").document(imageUidList.get(position)).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        DynamicToast.make(context,"삭제 완료.", AppCompatResources.getDrawable(context,R.drawable.ic_baseline_delete_24),Color.parseColor("#FFFFFF"),Color.parseColor("#F44E42")).show();
                        dlg.dismiss();
                        customDialogListener.onFresh(position);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "failed to delete", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        ImageView okBtn = dlg.findViewById(R.id.dialog_ok);
        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
    }


}