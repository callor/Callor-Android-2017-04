package com.callor.hello.myphonebook;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by callor on 2017-04-20.
 */

public class RcAdapter extends RecyclerView.Adapter<RcAdapter.VHolder>{

    private List<PhoneVO> phoneDTO;

    public RcAdapter(Context context, List<PhoneVO> phoneDTO) {
        this.phoneDTO = phoneDTO ;
    }

    @Override
    public VHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_list,null);
        VHolder vHolder = new VHolder(item);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(VHolder holder, int position) {
        holder.name.setText(phoneDTO.get(position).getpName());
        holder.number.setText(phoneDTO.get(position).getpNumber());
        if(phoneDTO.get(position).getImage() != null) {
            holder.photo.setImageBitmap(phoneDTO.get(position).getImage());
        }
    }

    @Override
    public int getItemCount() {
        return phoneDTO.size();
    }

    public class VHolder extends RecyclerView.ViewHolder {
        TextView name,number ;
        ImageView photo;
        public VHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.list_name);
            number = (TextView)itemView.findViewById(R.id.list_phone);
            photo = (ImageView)itemView.findViewById(R.id.list_image);
        }
    }
}
