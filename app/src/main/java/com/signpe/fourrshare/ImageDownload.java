package com.signpe.fourrshare;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

public class ImageDownload extends AsyncTask<Void, Void, Bitmap> {
    private String urlStr;
    private ImageView imageView;
    protected Bitmap resbitmap;

    // 요청 url과 비트맵 객체 매핑, 메모리 관리 위한 것
    private static HashMap<String, Bitmap> bitmapHash = new HashMap<>();

    public ImageDownload(String urlStr, ImageView imageView) {
        this.urlStr = urlStr;
        this.imageView = imageView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        /*
         * 해당 url 에 접속하여 스트림을 받음
         * 이미지일 경우 이미지 스트림 그대로 넘어오고
         * decodeStream 은 이미지 스트림을 비트맵으로 바꿔줌
         * (주소에 해당하는 이미지를 스트림(byte array)으로 가져옴!)
         */

        Bitmap bitmap = null;
        try {
            if(bitmapHash.containsKey(urlStr)){ // 요청 주소가 들어있다면
                Bitmap oldBitmap = bitmapHash.remove(urlStr);
                if(oldBitmap != null){
                    oldBitmap.recycle();
                    oldBitmap = null;
                }
            }
            URL url = new URL(urlStr);
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            /*
             * 라이브러리들은 한번 받아놓은 이미지가 있으면 단말에 캐싱해놓고
             * 같은 URL로 요청할 경우 캐싱된 로컬 이미지를 그대로 사용한다 */

            bitmapHash.put(urlStr, bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        imageView.setImageBitmap(bitmap);
        imageView.invalidate();
        resbitmap=bitmap;
    }
}

