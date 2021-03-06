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
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ReleaseWishActivity extends AppCompatActivity {
    public static final int TAKE_PHOTO=1;
    public static final int CHOOSE_PHOTO=2;
    public String name;
    private ImageView showPhoto;
    private EditText wishesTextInput;
    private Uri imageUri;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_wish);
        Button back=(Button) findViewById(R.id.back);
        wishesTextInput=(EditText) findViewById(R.id.wishes_text);
        Button takePhoto=(Button) findViewById(R.id.take_photo);
        Button choosePhoto=(Button) findViewById(R.id.choose_photo);
        showPhoto=(ImageView) findViewById(R.id.show_photo);
        Button releaseWishes=(Button) findViewById(R.id.publish);

        SharedPreferences checkLogin=getSharedPreferences("data",MODE_PRIVATE);
        name=checkLogin.getString("name",null);

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(ReleaseWishActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        takePhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                File outputImage=new File(getExternalCacheDir(),"output_image.jpg");
                try{
                    if(outputImage.exists()){
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                }
                if(Build.VERSION.SDK_INT>=24){
                    imageUri= FileProvider.getUriForFile(ReleaseWishActivity.this,"com.example.chan.firstlevelproject.fileprovider",outputImage);
                }else{
                    imageUri=Uri.fromFile(outputImage);
                }
                Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,TAKE_PHOTO);
            }
        });

        choosePhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(ContextCompat.checkSelfPermission(ReleaseWishActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ReleaseWishActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else{
                    openAlbum();
                }
            }
        });

        releaseWishes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                SimpleDateFormat fm=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                Date curDate=new Date(System.currentTimeMillis());
                String str=fm.format(curDate);
                String wishesText = wishesTextInput.getText().toString();
                byte[]images=img(bitmap);
                Wishes wishes=new Wishes();
                wishes.setWishedName(name);
                wishes.setWishedText(wishesText);
                wishes.setWishedImage(images);
                wishes.setPublishedTime(str);
                wishes.save();
                Toast.makeText(getApplicationContext(), "发布成功", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ReleaseWishActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
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
            case TAKE_PHOTO:
                if(requestCode==RESULT_OK){
                    try{
                        Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        showPhoto.setImageBitmap(bitmap);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if(resultCode==RESULT_OK){
                    if(Build.VERSION.SDK_INT>=19){
                        handleImageOnKitKat(data);
                    }else{
                        handleImageBeforeKitKat(data);
                    }
                }
            default:
                break;
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
            bitmap= BitmapFactory.decodeFile(imagePath);
            showPhoto.setImageBitmap(bitmap);
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

