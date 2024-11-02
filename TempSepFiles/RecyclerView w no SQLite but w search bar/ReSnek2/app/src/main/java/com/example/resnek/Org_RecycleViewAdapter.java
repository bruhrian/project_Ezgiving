package com.example.resnek;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Org_RecycleViewAdapter extends RecyclerView.Adapter<Org_RecycleViewAdapter.MyViewHolder> {

    private final RecycleViewInterface recycleViewInterface;

    Context context;
    ArrayList<OrganisationModel> organisationModels;

    public Org_RecycleViewAdapter(Context context, ArrayList<OrganisationModel> organisationModels, RecycleViewInterface recycleViewInterface) {
        this.context = context;
        this.organisationModels = organisationModels;
        this.recycleViewInterface = recycleViewInterface;
    }


    public void setFilteredList(ArrayList<OrganisationModel> filteredList){
        this.organisationModels = filteredList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public Org_RecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // place to inflate layout(Giving a look to our rows)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycle_view_row, parent, false);

        return new Org_RecycleViewAdapter.MyViewHolder(view, recycleViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull Org_RecycleViewAdapter.MyViewHolder holder, int position) {
        // assigning values to views created in the recycle_view_row layout file
        // based on pos of recycle view

        holder.tvOrg.setText(organisationModels.get(position).getOrgName());
        holder.tvTaskAct.setText(organisationModels.get(position).getOrgTaskActivity());
        holder.imageView.setImageResource(organisationModels.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        // recycle view js wn to know no. of items uw to display
        return organisationModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        // grabbing views form our recycle_view_row layout file
        // Kinda like in the onCreate mtd.
        ImageView imageView;
        TextView tvOrg, tvTaskAct;
        public MyViewHolder(@NonNull View itemView, RecycleViewInterface recycleViewInterface) {
            super(itemView);

            imageView = itemView.findViewById(R.id.orgPic);
            tvOrg = itemView.findViewById(R.id.OrganisationName);
            tvTaskAct = itemView.findViewById(R.id.taskORactivity);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recycleViewInterface != null){
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            recycleViewInterface.onItemClick(pos);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (recycleViewInterface != null){
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            recycleViewInterface.onItemLongClick(pos);
                        }
                    }

                    return true;
                }
            });
        }
    }
}
