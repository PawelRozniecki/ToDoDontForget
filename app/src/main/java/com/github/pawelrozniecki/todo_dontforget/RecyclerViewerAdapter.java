package com.github.pawelrozniecki.todo_dontforget;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.github.pawelrozniecki.todo_dontforget.database.DatabaseHelper;
import java.util.ArrayList;

public class RecyclerViewerAdapter extends RecyclerView.Adapter<RecyclerViewerAdapter.ViewHolder> {

    //for debugging
    private static final String TAG = "RecyclerViewerAdapter";
    private ArrayList<String> itemArray = new ArrayList<>();
    private Context mContext;
    private CardView linearLayout;
    private ArrayList<String> dateArray = new ArrayList<>();
    private ArrayList<String> statusArray;
    private DatabaseHelper helper;
    private ArrayList<String> categoryArray = new ArrayList<>();

    public RecyclerViewerAdapter(ArrayList<String> itemArray, ArrayList<String> dateArray, ArrayList<String> categoryArray,ArrayList<String> statusArray, Context mContext) {
        this.itemArray = itemArray;
        this.dateArray = dateArray;
        this.categoryArray = categoryArray;
        this.statusArray = statusArray;
        this.mContext = mContext;
    }

    @NonNull
    @Override

    //inflate  will read XML layout and parse it to JAVA
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        helper = new DatabaseHelper(mContext);

        holder.listItem.setText(itemArray.get(position));
        holder.date.setText("Due: " + dateArray.get(position));


        switch(categoryArray.get(position)){
            case "ToDo": holder.imageView.setImageResource(R.drawable.todo_icon); break;
            case "School":holder.imageView.setImageResource(R.drawable.school_icon);break;
            case "Shopping":holder.imageView.setImageResource(R.drawable.shop_icon);break;
            case "Work":holder.imageView.setImageResource(R.drawable.work_icon);break;
        }


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(holder.checkBox.isChecked()){

                        holder.listItem.setPaintFlags(holder.listItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        holder.animate = ObjectAnimator.ofObject(holder.itemHolder, "backgroundColor", new ArgbEvaluator(), Color.WHITE, Color.parseColor("#0be881"));
                        holder.animate.setDuration(500);
                        holder.animate.start();
                        holder.status.setText("Done");
                }else{
                    holder.status.setText("Not Done");
                    holder.listItem.setPaintFlags(holder.listItem.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    holder.animate = ObjectAnimator.ofObject(holder.itemHolder, "backgroundColor", new ArgbEvaluator(), Color.parseColor("#0be881"),Color.WHITE);
                    holder.animate.setDuration(500);
                    holder.animate.start();
                }
            }
        });

        holder.itemHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = itemArray.get(position);
                Cursor data = helper.getDataID(name);

                int itemID = -1;
                while(data.moveToNext()){
                    //get ID value from first column
                    itemID = data.getInt(0);

                }
                if(itemID>-1){

                    Intent intent = new Intent(mContext, ManageListItem.class );
                    intent.putExtra("id", itemID);
                    intent.putExtra("name", name);
                    mContext.startActivity(intent);

                }else{

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return itemArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        CardView itemHolder;
        TextView listItem,date,status;
        ObjectAnimator animate;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            linearLayout = itemView.findViewById(R.id.item_holder);
            listItem = itemView.findViewById(R.id.list_item);
            checkBox = itemView.findViewById(R.id.check_box);
            itemHolder = itemView.findViewById(R.id.item_holder);
            imageView = itemView.findViewById(R.id.icon);
            status = itemView.findViewById(R.id.status);
            date = itemView.findViewById(R.id.date);



        }
    }
}
