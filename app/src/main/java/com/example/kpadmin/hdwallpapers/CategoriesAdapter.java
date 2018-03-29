package com.example.kpadmin.hdwallpapers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.kpadmin.hdwallpapers.Database.CategoryContract;
import com.example.kpadmin.hdwallpapers.Database.DbHelper;

/**
 * Created by kpchandora on 31/1/18.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyHolder> {

    private ArrayList<CategoryModel> categoryModels;
    private Context context;
    private int[] tempFlag;
    private ArrayList<Integer> arrayList;

    private int[] flagArray ;

    private int count = 0;

    private HashMap<String , Integer> map;

    private static final String TAG = "CategoryAdapter";

    public CategoriesAdapter(Context context, ArrayList<CategoryModel> categoryModels) {
        this.context = context;
        this.categoryModels = categoryModels;
        tempFlag = new int[categoryModels.size()];
        arrayList = new ArrayList<>();
        flagArray = new int[categoryModels.size()];

        map = new HashMap<>();

        for(int i = 0; i < categoryModels.size(); i++){
            flagArray[i] = i;
        }

        for(int i = 0; i < categoryModels.size(); i++){
            map.put(categoryModels.get(i).getImageTitle(), categoryModels.get(i).getFlag());
        }

        Log.i(TAG, "CategoriesAdapter: ");

        for(int i = 0; i < map.size(); i++){
            Log.i(TAG, "HashMap: "+map);
        }

    }


    @Override
    public long getItemId(int position) {
        Log.i(TAG, "getItemId: ");
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        Log.i(TAG, "getItemViewType: ");
        return position;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.category_layout, null);

        Log.i("onCreateViewHolder", "onCreateViewHolder: ");
        return new MyHolder(view);
    }


    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {

        final CategoryModel model = categoryModels.get(position);
        final String title = model.getImageTitle();
        final int flag = model.getFlag();

        final String url = model.getImageUrl();
        final String id = model.getCategoryId();
        final Button button = holder.categoryButton;

        button.setText(title);
        tempFlag[position] = flag;
//        Log.i(TAG, "onBindViewHolder: " + tempFlag[position]);

        Log.i(TAG, "onBindViewHolder: Position "+position);

        if (flag == 1) {
            Log.i(TAG, "Hashkey: "+title);
            Log.i(TAG, "onBindViewHolder: Pos 1 "+position);
            button.setBackgroundResource(R.drawable.category_button_click_bg);
            button.setTextColor(Color.BLACK);
            updateTitleAndUrl(title, url);
        }

//        Log.i(TAG, "onBindViewHolder: Pos " + position);
//        Log.i(TAG, "onBindViewHolder: Count " + count);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tempFlag[position] != 1) {
                    button.setBackgroundResource(R.drawable.category_button_click_bg);
                    button.setTextColor(Color.BLACK);
                    insertDataIntoFrontTable(title, url);
                    model.setFlag(1);
                    tempFlag[position] = 1;
                    Log.i(TAG, "onClick: 0");
                } else {
                    button.setBackgroundResource(R.drawable.category_button_bg);
                    button.setTextColor(Color.WHITE);
                    deleteDataFromFrontTable(title);
                    model.setFlag(0);
                    tempFlag[position] = 0;
                    Log.i(TAG, "onClick: 1");
                }
            }
        });


    }


    private void deleteDataFromFrontTable(String title) {

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = CategoryContract.FRONT_ID + " =? ";
        long numRow = db.delete(CategoryContract.FRONT_TABLE, selection, new String[]{title});
        Log.i(TAG, "deleteDataFromFrontTable: " + numRow);

    }

    private void insertDataIntoFrontTable(String title, String url) {

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CategoryContract.FRONT_TITLE, title);
        values.put(CategoryContract.FRONT_ID, title);
        values.put(CategoryContract.FRONT_IMAGE_URL, url);

        long numRows = db.insert(CategoryContract.FRONT_TABLE, null, values);
        Log.i("Category", "insertDataIntoFrontTable: " + numRows);

    }

    private void updateTitleAndUrl(String id, String url) {

        DbHelper helper = new DbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CategoryContract.FRONT_IMAGE_URL, url);

        long numRows = db.update(CategoryContract.FRONT_TABLE, values, CategoryContract.FRONT_ID + "=?",
                new String[]{id});


        Log.i(TAG, "updateTitleAndUrl: " + numRows);
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private Button categoryButton;
        private View itemView;

        public MyHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            categoryButton = itemView.findViewById(R.id.category_button_id);
        }
    }
}
