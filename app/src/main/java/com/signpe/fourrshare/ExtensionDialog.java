package com.signpe.fourrshare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

public class ExtensionDialog extends AppCompatActivity {
    private Context context;
    private ImageView getImage;

    public ExtensionDialog(Context context, ImageView image) {
        this.context = context;
        getImage = image;
    }

    //호출할 다이얼로그 함수 정의
    public void callFunction() {
        //커스텀 다이얼로그 정의하기 위한 dialog클래스 생성
        final Dialog dlg;
        dlg = new Dialog(context);
        Window window = dlg.getWindow();

        Rect displayRectangle = new Rect();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        Toast.makeText(context, "gogogogo", Toast.LENGTH_SHORT).show();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.activity_extension_dialog,null);
        layout.setMinimumWidth((int)(displayRectangle.width() * 0.9f));
        layout.setMinimumHeight((int)(displayRectangle.height() * 0.9f));
        dlg.setContentView(layout);
        dlg.show();

        ImageView iv = dlg.findViewById(R.id.extensionImage);

        Switch switchButton =dlg. findViewById(R.id.sw);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // switchButton이 체크된 경우
                } else {
                    // switchButton이 체크되지 않은 경우
                }
            }
        });

        ImageView downBtn =dlg.findViewById(R.id.dialog_download);
        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //다운로드 버튼 클릭시
            }
        });

        ImageView deleteBtn = dlg.findViewById(R.id.dialog_delete);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //삭제 버튼 클릭시
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