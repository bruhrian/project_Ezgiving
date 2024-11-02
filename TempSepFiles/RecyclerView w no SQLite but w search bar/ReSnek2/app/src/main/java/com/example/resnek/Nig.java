package com.example.resnek;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Nig extends AppCompatActivity implements RecycleViewInterface{
    private SearchView searchView;



    ArrayList<OrganisationModel> organisationModels = new ArrayList<>();
    Org_RecycleViewAdapter adapter;

    int[] orgImages ={R.drawable.communitychest, R.drawable.ffth, R.drawable.muhammadiassociationsg, R.drawable.ntuc, R.drawable.sgheartfoundationn,
    R.drawable.singaporecancersociety, R.drawable.vegetariansocietysg, R.drawable.willinghearts, R.drawable.singaporebuddhistfreeclinic};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nig);

        RecyclerView recyclerView = findViewById(R.id.orgRecycleView);

        setUpOrganisationModels();


        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });


        adapter = new Org_RecycleViewAdapter(this, organisationModels, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void filterList(String newText) {
        List<OrganisationModel> filteredList = new ArrayList<>();
        for (OrganisationModel item : organisationModels){
            if(item.getOrgName().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(item);
            }
        }

        if (filteredList.isEmpty()){
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }
        else{adapter.setFilteredList((ArrayList<OrganisationModel>) filteredList);}
    }

    private void setUpOrganisationModels() {
        String[] orgNames = getResources().getStringArray(R.array.organisation_names);
        String[] orgTaskAct = getResources().getStringArray(R.array.type_of_activity);
        String[] orgDesc = getResources().getStringArray(R.array.desc);

        for (int i = 0; i < orgNames.length; i++) {
            organisationModels.add(new OrganisationModel(orgNames[i], orgTaskAct[i], orgImages[i], orgDesc[i]));
        }
    }

    @Override
    public void onItemClick(int pos) {
        Intent intent = new Intent(Nig.this, Nig2.class);

        intent.putExtra("NAME", organisationModels.get(pos).getOrgName());
        intent.putExtra("TASKACTIVITY", organisationModels.get(pos).getOrgTaskActivity());
        intent.putExtra("IMAGE", organisationModels.get(pos).getImage());
        intent.putExtra("DESC", organisationModels.get(pos).getDesc());

        startActivity(intent);

    }

    @Override
    public void onItemLongClick(int pos) {
        organisationModels.remove(pos);
        adapter.notifyItemRemoved(pos);
    }
}