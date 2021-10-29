package com.hrsh.seedtherise;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class InstListAdapter extends RecyclerView.Adapter<InstListAdapter.Viewholder> {

    private Context context;
    private List<InstanceListByNameModel> instanceListByNameModelArrayList;

    // Constructor
    public InstListAdapter(Context context, List<InstanceListByNameModel> instModelArrayList) {
        this.context = context;
        this.instanceListByNameModelArrayList = instModelArrayList;
    }

    @NonNull
    @Override
    public InstListAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.instance_list_card_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstListAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
//        if(position==getItemCount()){
//            holder.divider.setBackgroundColor(Color.WHITE);
//        }
        InstanceListByNameModel model = instanceListByNameModelArrayList.get(position);
        holder.instName.setText(model.getInstName());
        holder.instLogoTextView.setText(model.getInstLogo());
        holder.instLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cropHealthActivity = new Intent(context, CropHealthActivity.class);
                cropHealthActivity.putExtra("instName", holder.instName.getText().toString());
                context.startActivity(cropHealthActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return instanceListByNameModelArrayList.size();
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView instLogoTextView;
        private TextView instName;
        private View divider;
        LinearLayout instLinearLayout;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            instLogoTextView = itemView.findViewById(R.id.instCardLogo);
            instName = itemView.findViewById(R.id.instCardName);
            divider = itemView.findViewById(R.id.divider);
            instLinearLayout = itemView.findViewById(R.id.instLinearLayout);
        }
    }
}
