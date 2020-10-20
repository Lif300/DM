package com.example.marketplaceproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class myadapter extends RecyclerView.Adapter<myadapter.myviewholder>{

    ArrayList<datamodel> dataholder;
    Context mContext;
    private FirebaseUser firebaseUser;
    private String uid;
    private DatabaseReference myRef;

    public myadapter(ArrayList<datamodel> dataholder) {
        this.dataholder = dataholder;

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        mContext = parent.getContext();


        return new myviewholder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        Glide.with(holder.itemView.getContext()).load(dataholder.get(position).getImageurl()).into(holder.CircleImg);
        holder.header.setText(dataholder.get(position).getHeader());
        holder.descr.setText(dataholder.get(position).getDescr());
        holder.price.setText(dataholder.get(position).getPrec());

        holder.header.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) { //Si el usuario mantiene presionado el articulo, este podrá ser eliminado.
                myRef = FirebaseDatabase.getInstance().getReference();
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                uid = firebaseUser.getUid();

                Query query = myRef.child("Usuarios").child(uid).child("articulos");
                ArrayList<String> ids = new ArrayList(); //Se crea un array list para obtener el push.ID de todos los articulos de firebase del usuario.
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot snapshot2 : snapshot.getChildren()){

                                ids.add(snapshot2.getKey()); //se agrega el id a la lista

                            }


                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext); //Creamos un botón para que el usuario decida si eliminar o no el artículo.
                builder.setTitle("Borrar Artículo");
                builder.setMessage("¿Desea eliminar este artículo?");

                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {   //YES BUTTON
                    @Override
                    public void onClick(DialogInterface dialog, int which) { //El usuario presionó eliminar, eliminamos el artículo.
                        String key = ids.get(position);
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(uid).child("articulos");
                        ref.child(key).removeValue();
                        Snackbar.make(v, "Artículo eliminado", Snackbar.LENGTH_LONG).show();
                        //((DatabaseReference) query).child(ids.get(position)).removeValue();

                                    /*  se obtiene la posición del adapter, la posición 1 (primer articulo del recyclerview) es igual a la primera posición de la lista.
                                        Se elimina el articulo seleccionado.
                                        Ejemplo: Seleccionas el articulo 2. getPosition = 2, se elimina el articulo no. 2 de la lista de firebase. */
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { //El usuario presionó cancelar, solamente desaparecemos el dialog.
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                Log.d("EL NOMBRE ES:", " "+holder.getAdapterPosition());
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    class myviewholder extends RecyclerView.ViewHolder{
        CircleImageView CircleImg;
        TextView header, descr, price;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            CircleImg = itemView.findViewById(R.id.circle1);
            header = itemView.findViewById(R.id.txtheader1);
            descr = itemView.findViewById(R.id.txtdescr1);
            price = itemView.findViewById(R.id.txtprecio1);

        }
    }
}
