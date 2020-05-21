package com.github.P4rzival.RadiusMessage;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class PostImage {

    public String BitmapToString(Bitmap bitmap){
        ByteArrayOutputStream bitStreamOut = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 24, bitStreamOut);
        byte[] byteImageArray = bitStreamOut.toByteArray();
        String convertedImage = Base64.encodeToString(byteImageArray, Base64.URL_SAFE);
        return convertedImage;
    }

    public Bitmap StringToBitMap(String encodedImageString){
        try{
            byte [] encodeByte = Base64.decode(encodedImageString, Base64.URL_SAFE);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0 ,encodeByte.length);
            return bitmap;
        } catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    public String savePostImage(Bitmap imageToSave){
        File path = Environment.getExternalStorageDirectory();
        File dir = new File(path.getAbsolutePath()+"/RadiusMessage");
        String fileName = "";

        if (ContextCompat.checkSelfPermission(RadiusMessage.getAppInstance().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Do the file write
            dir.mkdirs();


            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date());
            fileName = timeStamp + ".png";

            File file = new File(dir, fileName);
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            OutputStream out = null;
            try {
                out = new FileOutputStream(file);
                imageToSave.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Request permission from the user
            ActivityCompat.requestPermissions((Activity) RadiusMessage.getAppInstance().getApplicationContext(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        Toast.makeText( RadiusMessage.getAppInstance().getApplicationContext()
                , "Image saved to " + dir.toString()
                , Toast.LENGTH_SHORT).show();

        return fileName;
    }
}
