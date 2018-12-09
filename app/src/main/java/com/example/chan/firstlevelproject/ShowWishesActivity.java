package com.example.chan.firstlevelproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class ShowWishesActivity extends AppCompatActivity {
    private List<ShowWishes> wishesList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_wishes);
        initWishes();
        WishesAdapter adapter=new WishesAdapter(ShowWishesActivity.this,R.layout.wishes_item,wishesList);
        ListView listView=(ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
    }

    private void initWishes(){
        List<Wishes> wishes= DataSupport.findAll(Wishes.class);
        List<Account> accounts=DataSupport.findAll(Account.class);
        for(Wishes wish:wishes){
            for(Account account:accounts){
                if(wish.getWishedImage()!=null&&wish.getWishedText()!=null&&wish.getWishedName()!=null&&wish.getPublishedTime()!=null&&account.getName().equals(wish.getWishedName())){
                    ShowWishes wishes1=new ShowWishes(wish.getWishedName(),wish.getWishedText(),wish.getWishedImage(),wish.getPublishedTime(),account.getEmail());
                    wishesList.add(wishes1);
                }
            }
        }
    }
}
