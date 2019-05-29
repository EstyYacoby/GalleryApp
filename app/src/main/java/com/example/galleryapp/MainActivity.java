package com.example.galleryapp;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String title = "Gallery";
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ImageRecAdapter adapter;
    private ArrayList<ImageItem> imagesList;
    private RequestQueue requestQueue;
    private String prefix = "https://pixabay.com/api/?key=12175339-7048b7105116d7fa1da74220c&q=";
    private String suffix = "&image_type=photo&page=";
    private String search;
    private int page=1;
    private GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //init recyclerview
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        imagesList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        adapter = new ImageRecAdapter(MainActivity.this,imagesList);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem item = menu.findItem(R.id.menuSearch);
        final SearchView searchView = (SearchView)item.getActionView();
        getSupportActionBar().setTitle(title);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                searchView.setIconified(true);
                resetImageList();
                search=query;
                loadImages(search,page);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if(gridLayoutManager.findLastCompletelyVisibleItemPosition()== imagesList.size()-1){
                        loadImages(search,++page);
                    }
                }
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void loadImages(String userRequest, final int page) {

        String updatedUserRequest = userRequest.replace(' ','+');
        String url = prefix + updatedUserRequest + suffix + page;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("hits");
                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject hit = jsonArray.getJSONObject(i);
                        String imageUrl = hit.getString("webformatURL");
                        imagesList.add(new ImageItem(imageUrl,false));
                    }
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);
    }


    private void resetImageList() {
        if(!imagesList.isEmpty()){
            imagesList.clear();
            adapter.notifyDataSetChanged();
            page=1;
        }

    }
}
