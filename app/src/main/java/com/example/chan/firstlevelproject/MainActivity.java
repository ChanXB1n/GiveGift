package com.example.chan.firstlevelproject;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.List;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import org.litepal.crud.DataSupport;


public class MainActivity extends AppCompatActivity {
    private ImageView headImage;
    private ImageView wishesImage;
    private TextView showWishesText;
    private TextView showWishesName;
    private TextView showWishesTime;
    public String name;
    public static final int CHOOSE_PHOTO=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button logOff=(Button) findViewById(R.id.log_off);
        headImage=(ImageView) findViewById(R.id.iv_avatar);
        wishesImage=(ImageView) findViewById(R.id.show_wishes);
        showWishesText=(TextView) findViewById(R.id.show_wish_text);
        showWishesName=(TextView) findViewById(R.id.show_wish_name);
        showWishesTime=(TextView) findViewById(R.id.show_release_time);
        Button editHeadImage=(Button) findViewById(R.id.edit_head_image);
        Button seeWishes=(Button) findViewById(R.id.see_wishes);
        Button seeOwnWishes=(Button) findViewById(R.id.see_own_wishes);
        SharedPreferences checkLogin=getSharedPreferences("data",MODE_PRIVATE);
        name=checkLogin.getString("name",null);
        Button releaseWishes=(Button) findViewById(R.id.release_wishes);

        List<Account> accounts= DataSupport.findAll(Account.class);
            for(Account account:accounts){
                if(account.getName().equals(name)){
                    if(account.getHeadImage()!=null){
                        byte[]images=account.getHeadImage();
                        Bitmap bitmap=BitmapFactory.decodeByteArray(images,0,images.length);
                        headImage.setImageBitmap(bitmap);
                    }
                }
            }




        logOff.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
                editor.putBoolean("isFirstLogin",false);
                editor.apply();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        editHeadImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else{
                    openAlbum();
                }
            }
        });

        releaseWishes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this,ReleaseWishActivity.class);
                startActivity(intent);
            }
        });

        seeWishes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this,ShowWishesActivity.class);
                startActivity(intent);
            }
        });

        seeOwnWishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,OwnWishesActivity.class);
                startActivity(intent);
            }
        });
    }

    private void openAlbum(){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else{
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch (requestCode){
            case CHOOSE_PHOTO:
                if(resultCode==RESULT_OK){
                    if(Build.VERSION.SDK_INT>=19){
                        handleImageOnKitKat(data);
                    }else{
                        handleImageBeforeKitKat(data);
                    }
                }
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];
                String selection= MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath=uri.getPath();
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath=uri.getPath();
        }
        displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data){
        Uri uri=data.getData();
        String imagePath=getImagePath(uri,null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri,String selection){
        String path=null;
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath){
        if(imagePath!=null){
            Bitmap bitmap= BitmapFactory.decodeFile(imagePath);
            headImage.setImageBitmap(bitmap);
            byte[]images=img(bitmap);
            Account account=new Account();
            account.setHeadImage(images);
            account.updateAll("name = ?",name);
        }else{
            Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
        }
    }

    private byte[]img(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}

