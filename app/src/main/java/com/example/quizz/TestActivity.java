package com.example.quizz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private RecyclerView quizView;
    private Toolbar toolbar;
    private List<TestModel> testList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("");
        quizView=findViewById(R.id.quiz_recycler_view);

        toolbar=findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        int cat_index=getIntent().getIntExtra("CAT_INDEX",0);

        getSupportActionBar().setTitle(DbQuery.g_catList .get(cat_index).getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        quizView=findViewById(R.id.quiz_recycler_view);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        quizView.setLayoutManager(layoutManager);

        loadTestData();

        TestAdapter adapter=new TestAdapter(testList);
        quizView.setAdapter(adapter);

    }

    private  void loadTestData()
    {
        testList=new ArrayList<>();
        testList.add(new TestModel("1",50,10));
        testList.add(new TestModel("2",80,20));
        testList.add(new TestModel("3",0,25));
        testList.add(new TestModel("4",20,30));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home);
        {
            TestActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}