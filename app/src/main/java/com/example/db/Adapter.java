package com.example.db;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class Adapter extends BaseAdapter {

    ArrayList<NoteModel> modelList;
    Context context;

    public Adapter(ArrayList<NoteModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int i) {
        return modelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view= LayoutInflater.from(context).inflate(R.layout.list_item,viewGroup,false);

        TextView noteName=view.findViewById(R.id.listItemNameText);
        TextView date=view.findViewById(R.id.listItemDateText);
        LinearLayout layout=view.findViewById(R.id.recycelerItemClikableLinearLayout);

        noteName.setText(modelList.get(i).getNotename());
        date.setText(modelList.get(i).getTime());

        String notename=modelList.get(i).getNotename();
        String dates=modelList.get(i).getTime();
        String description=modelList.get(i).getDescription();
        String topic=modelList.get(i).getTopic();

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,DetailPageAct.class);
                intent.putExtra("notename",notename);
                intent.putExtra("dates",dates);
                intent.putExtra("topic",topic);
                intent.putExtra("description",description);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        return view;
    }

    public void filterList(ArrayList<NoteModel> filteredlist) {
        modelList = filteredlist;
        notifyDataSetChanged();
    }

}
