package com.example.marketplaceproject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class myadapter2 extends RecyclerView.Adapter<myadapter2.myviewholder2> {



    List<String> items;

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
        holder.useridObjeto.setText(dataholder2.get(position).getUserid());
        holder.pushidObjeto.setText(dataholder2.get(position).getPushid());
        holder.usuarioVenta.setText(dataholder2.get(position).getUsuario());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TextView mtxtPrecio = (TextView) v.findViewById(R.id.txtprecio2);
                TextView mtxtPushId = (TextView) v.findViewById(R.id.txtpushid);
                TextView mtxtNombre = (TextView) v.findViewById(R.id.txtheader2);
                TextView mtxtDescr = (TextView) v.findViewById(R.id.txtdescr2);
                TextView mtxtUserID = (TextView) v.findViewById(R.id.txtuserid);

                String precio = mtxtPrecio.getText().toString();
                String pushid = mtxtPushId.getText().toString();
                String nombre = mtxtNombre.getText().toString();
                String descr = mtxtDescr.getText().toString();
                String userid = mtxtUserID.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString("precio", precio);
                bundle.putString("pushid", pushid);
                bundle.putString("nombre", nombre);
                bundle.putString("desc", descr);
                bundle.putString("userid", userid);
                Log.d("LOGTAG", "clicked : name "+mtxtPushId.getText().toString());
                Navigation.findNavController(v).navigate(R.id.articuloVenta, bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataholder2.size();
    }

    class myviewholder2 extends RecyclerView.ViewHolder{
        CircleImageView CircleImg;
        TextView header, descr, price, usuarioVenta, pushidObjeto, useridObjeto;

        public myviewholder2(@NonNull View itemView) {
            super(itemView);
            CircleImg = itemView.findViewById(R.id.circle2);
            header = itemView.findViewById(R.id.txtheader2);
            descr = itemView.findViewById(R.id.txtdescr2);
            price = itemView.findViewById(R.id.txtprecio2);
            usuarioVenta = itemView.findViewById(R.id.txtusuario2);
            pushidObjeto = itemView.findViewById(R.id.txtpushid);
            useridObjeto = itemView.findViewById(R.id.txtuserid);


        }
    }

}
