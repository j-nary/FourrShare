package com.signpe.fourrshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

public class BigImageDialog extends AppCompatActivity {
    private final Context context;
    private ImageView getImage;
    Dialog dlg;

    public BigImageDialog(Context context,ImageView getImage){
        this.context=context;
        this.getImage=getImage;
    }

    public void callFunction() {
        //커스텀 다이얼로그 정의하기 위한 dialog클래스 생성
        dlg = new Dialog(context);
        Window window = dlg.getWindow();

        Rect displayRectangle = new Rect();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.activity_big_image_dialog,null);
        layout.setMinimumWidth((int)(displayRectangle.width() * 0.9f));
        layout.setMinimumHeight((int)(displayRectangle.height() * 0.7f));
        dlg.setContentView(layout);
        dlg.show();

        ImageView iv = dlg.findViewById(R.id.BigImage);
        iv.setImageDrawable(getImage.getDrawable());


        ImageView okBtn = dlg.findViewById(R.id.dialog_ok);
        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
    }
}