package com.android.yitianspider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Request;
import okhttp3.Response;

public class
SpiderNet {
    private static String TAG = "test";
    private OkHttpClient okHttpClient = new OkHttpClient();


    private byte[] getImage(String img) {
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(img);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public  void  upLoadImg(String  img) {
        String url = "https://m2u-api.getkwai.com/api-server/api/v1/genericProcess?device=vivo%2BY55&od=&deviceId=1e355c3cf97e709ee24a6fb8f9066f7e&mi=865226030586858&fr=ANDROID&ch=ALIBABA&umid=&noReco=0&egid=DFP66AD41BCEF72E3147882C5004A3305BEEC0F63D9CE14DF52FE0F3CBB50358&md=vivo+Y55&appver=2.5.8.20580&ve=2.5.8.20580&channel=ALIBABA&type=fairyTale&sr=720%2A1280&wifi=%22APUS-Vip%22&isdg=0&ver_code=20580&did=ANDROID_ba45c96011b25866&boardPlatform=msm8937&app=m2u&os=ANDROID_6.0.1&platform=android&brand=vivo&globalid=DFP66AD41BCEF72E3147882C5004A3305BEEC0F63D9CE14DF52FE0F3CBB50358";
        byte[] image = getImage(img);
        RequestBody fileBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addPart(Headers.of("Content-Disposition", "form-data;name =beforeProcess;filename=beforeProcess.jpeg")
                        , RequestBody.create(MediaType.parse("image/png"), image)).build();

        Request request = new Request.Builder().url(url).post(fileBody).build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            String body = response.body().string();
            JSONObject jsonObject = new JSONObject(body);

            String data = jsonObject.getString("data");
            JSONObject dataObject = new JSONObject(data);
            String afterProcess = dataObject.getString("afterProcess");
            Log.d(TAG, afterProcess.length() + "返回长度");
            Bitmap bitmap = getBitmap(afterProcess);
            String newFileName  = img.split("Camera/")[1];
            saveBitmapFile(bitmap,newFileName);
        } catch (IOException | JSONException e) {
            e.getStackTrace();
        }
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.d(TAG, "Fail");
//            }
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Headers headers = response.headers();
//                String body = response.body().string();
//                try {
//                    JSONObject jsonObject = new JSONObject(body);
//                    String data = jsonObject.getString("data");
//                    JSONObject dataObject = new JSONObject(data);
//                    afterProcess = dataObject.getString("afterProcess");
//                    Log.d(TAG,afterProcess.length()+"返回长度");
//                    Bitmap bitmap = getBitmap(afterProcess);
//                    saveBitmapFile(bitmap);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

    }

    public void saveBitmapFile(@NotNull Bitmap bitmap,String newFileName) {
        //Log.d(TAG, "文件保存路径" + "/storage/emulated/0/Pictures/origin/" + newFileName);
        File file = new File("/storage/emulated/0/Pictures/origin/" + newFileName);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getBitmap(String afterProcess) {
        byte[] bytes = decodeResponses(afterProcess);

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private byte @NotNull [] decodeResponses(String string) {
        byte[] bArr = Base64.decode(string, 0);
        Log.d(TAG, bArr.length + "base64");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            convertStream(new GZIPInputStream(new ByteArrayInputStream(bArr)), byteArrayOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, byteArrayOutputStream.size() + "字节长度");
        return byteArrayOutputStream.toByteArray();

    }

    private void convertStream(@org.jetbrains.annotations.NotNull InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[8192];
        while (true) {
            int read = 0;
            try {
                read = inputStream.read(bArr);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (read == -1) {
                break;
            }
            outputStream.write(bArr, 0, read);
        }
        outputStream.flush();
    }

}
