package com.example.chan.firstlevelproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class OwnWishesActivity extends AppCompatActivity {

    private List<ShowWishes> wishesList=new ArrayList<>();
    public String name;
    ShowWishes showWishes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_wishes);
        initWishes();
        WishesAdapter adapter=new WishesAdapter(OwnWishesActivity.this,R.layout.own_wishes_item,wishesList);
        ListView listView=(ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position,long id){
                showWishes=wishesList.get(position);
                AlertDialog.Builder builder=new AlertDialog.Builder(OwnWishesActivity.this);
                builder.setMessage("确认删除吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataSupport.deleteAll(Wishes.class,"publishedTime=?",showWishes.getPublishedTime());
                        Toast.makeText(getApplicationContext(), " 删除成功", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(OwnWishesActivity.this, OwnWishesActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }


    private void initWishes(){
        SharedPreferences checkLogin=getSharedPreferences("data",MODE_PRIVATE);
        name=checkLogin.getString("name",null);
        List<Wishes> wishes= DataSupport.findAll(Wishes.class);
        List<Account> accounts=DataSupport.findAll(Account.class);
        for(Wishes wish:wishes){
            for(Account account:accounts){
                if(wish.getWishedImage()!=null&&wish.getWishedText()!=null&&wish.getWishedName()!=null&&wish.getPublishedTime()!=null&&wish.getWishedName().equals(name)&&account.getName().equals(name)){
                    ShowWishes wishes1=new ShowWishes(wish.getWishedName(),wish.getWishedText(),wish.getWishedImage(),wish.getPublishedTime(),account.getEmail());
                    wishesList.add(wishes1);
                }
            }
        }
    }
}
