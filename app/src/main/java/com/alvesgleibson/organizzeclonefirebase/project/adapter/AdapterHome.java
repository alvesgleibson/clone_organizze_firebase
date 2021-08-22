package com.alvesgleibson.organizzeclonefirebase.project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alvesgleibson.organizzeclonefirebase.R;
import com.alvesgleibson.organizzeclonefirebase.project.entities.FinancialMovementUser;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterHome extends RecyclerView.Adapter<AdapterHome.MyViewHolder> {

    List<FinancialMovementUser> movimentacaoList;
    Context context;

    public AdapterHome(List<FinancialMovementUser> movimentacaoList, Context context) {
        this.movimentacaoList = movimentacaoList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View viewList = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_home_page, parent, false);
        return new MyViewHolder(viewList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        FinancialMovementUser ta = movimentacaoList.get(position);

        holder.txtName.setText(ta.getCategory());
        holder.txtDescription.setText(ta.getDescription());
        holder.txtValue.setText(ta.getValue().toString());

        if (ta.getType().equals("d")){
            holder.txtValue.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            holder.txtValue.setText("R$ -"+ta.getValue());

        }else if (ta.getType().equals("r")){
            holder.txtValue.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
            holder.txtValue.setText("R$ "+ta.getValue());
        }

    }

    @Override
    public int getItemCount() {
        return movimentacaoList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName, txtDescription, txtValue;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtAdapterNameTrasation);
            txtDescription = itemView.findViewById(R.id.txtAdapterDescripition);
            txtValue = itemView.findViewById(R.id.txtAdapterValueTrasation);

        }
    }


}
