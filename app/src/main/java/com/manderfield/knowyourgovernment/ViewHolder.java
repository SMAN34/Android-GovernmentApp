package com.manderfield.knowyourgovernment;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder{
    public TextView row_name;
    public TextView row_office;

    public ViewHolder(View itemView) {
        super(itemView);
        row_name = (TextView) itemView.findViewById(R.id.recycle_row_name);
        row_office = (TextView) itemView.findViewById(R.id.recycle_row_office);
    }
}