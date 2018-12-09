package com.example.chan.firstlevelproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;



public class WishesAdapter extends ArrayAdapter<ShowWishes> {

    private int resourceId;

    public WishesAdapter(Context context, int textViewResourceId, List<ShowWishes>objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ShowWishes showWishes=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView showWishName=(TextView) view.findViewById(R.id.show_wish_name);
        TextView showWishText=(TextView) view.findViewById(R.id.show_wish_text);
        ImageView showWishesImage=(ImageView) view.findViewById(R.id.show_wishes);
        TextView showReleaseTime=(TextView) view.findViewById(R.id.show_release_time);
        TextView showContact=(TextView) view.findViewById(R.id.contact_information);
        showWishName.setText("发布者："+showWishes.getWishedName());
        showWishText.setText("愿望描述："+showWishes.getWishedText());
        byte[]images=showWishes.getWishedImage();
        Bitmap bitmap= BitmapFactory.decodeByteArray(images,0,images.length);
        showWishesImage.setImageBitmap(bitmap);
        showReleaseTime.setText("发布时间："+showWishes.getPublishedTime());
        showContact.setText("联系方式："+showWishes.getEmail());
        return view;
    }
}

