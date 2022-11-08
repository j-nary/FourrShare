package com.signpe.fourrshare;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImageDownload extends AsyncTask<String,String , Bitmap> {
    public interface OnPostDownLoadListener {
        void onPost(Bitmap bitmap);
    }

    private Bitmap bitmap = null;
    private OnPostDownLoadListener onPostDownLoad;

    // 리스너 세팅
    public ImageDownload(OnPostDownLoadListener paramOnPostDownLad) {
        onPostDownLoad = paramOnPostDownLad;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        try {
            // 파라미터로 받은 url로 부터 이미지 다운로드
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(strings[0]).getContent());
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        // 이미지 다운로드 완료 후 리스너 호출
        if (onPostDownLoad != null)
            onPostDownLoad.onPost(bitmap);
    }
}
