package com.manderfield.knowyourgovernment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class myAdapter extends RecyclerView.Adapter<ViewHolder>{

private List<Officials> officialList;
private MainActivity mainAct;

public myAdapter(List<Officials> officialList, MainActivity mainAct) {
        this.officialList = officialList;
        this.mainAct = mainAct;
        }

@Override
public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,parent, false);
        view.setOnClickListener(mainAct);
        return new ViewHolder(view);
        }

@Override
public void onBindViewHolder(ViewHolder holder, int position) {
        Officials official = officialList.get(position);
        if (official.getParty() == null){
                holder.row_name.setText(official.getName());
        }
        else{
                holder.row_name.setText(official.getName()+'('+official.getParty()+')');
        }
        holder.row_office.setText(official.getOffice());
        }

@Override
public int getItemCount() {
        return officialList.size();
        }
        }