package com.example.marketplaceproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class myadapter2 extends RecyclerView.Adapter<myadapter2.myviewholder2> {

    ArrayList<datamodel2> dataholder2;
    Context mContext;
    private FirebaseUser firebaseUser;
    private String uid;
    private DatabaseReference myRef;

    public myadapter2(ArrayList<datamodel2> dataholder2) {
        this.dataholder2 = dataholder2;

    }

    @NonNull
    @Override
    public myviewholder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        mContext = parent.getContext();


        return new myadapter2.myviewholder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder2 holder, int position) {
        Glide.with(holder.itemView.getContext()).load(dataholder2.get(position).getImageurl()).into(holder.CircleImg);
        holder.header.setText(dataholder2.get(position).getHeader());
        holder.descr.setText(dataholder2.get(position).getDescr());
        holder.price.setText(dataholder2.get(position).getPrec());
        holder.usuarioVenta.setText(dataholder2.get(position).getUsuario());

    }

    @Override
    public int getItemCount() {
        return dataholder2.size();
    }

    class myviewholder2 extends RecyclerView.ViewHolder{
        CircleImageView CircleImg;
        TextView header, descr, price, usuarioVenta;

        public myviewholder2(@NonNull View itemView) {
            super(itemView);
            CircleImg = itemView.findViewById(R.id.circle2);
            header = itemView.findViewById(R.id.txtheader2);
            descr = itemView.findViewById(R.id.txtdescr2);
            price = itemView.findViewById(R.id.txtprecio2);
            usuarioVenta = itemView.findViewById(R.id.txtusuario2);


        }
    }
}
