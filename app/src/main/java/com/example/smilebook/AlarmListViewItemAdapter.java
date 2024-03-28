package com.example.smilebook;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AlarmListViewItemAdapter extends BaseAdapter {
    ArrayList<AlarmListViewItem> arrayList = new ArrayList<AlarmListViewItem>();
    Context context;

    public AlarmListViewItemAdapter(Context context) {
        this.context = context;
    }
    @Override //ArrayList의 크기 반환
    public int getCount() {
        return arrayList.size();
    }

    @Override //해당 position 위치에 있는 item 리턴
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override // position 리턴
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.alarm_listview_item, viewGroup, false);

            viewHolder = new ViewHolder();
            viewHolder.alarmCircle = convertView.findViewById(R.id.alarm_circle);
            viewHolder.alarmText = convertView.findViewById(R.id.alarm_text);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        AlarmListViewItem alarmListViewItem = arrayList.get(i);
        viewHolder.alarmCircle.setImageDrawable(alarmListViewItem.getDrawable());
        viewHolder.alarmText.setText(alarmListViewItem.getAlarm_text());

        return convertView;
    }

    public void addItem(Drawable drawable, String string){

        AlarmListViewItem alarmListviewItem = new AlarmListViewItem();
        alarmListviewItem.setDrawable(drawable);
        alarmListviewItem.setAlarm_text(string);

        arrayList.add(alarmListviewItem);
    }

    private static class ViewHolder {
        ImageView alarmCircle;
        TextView alarmText;
    }

    //        ViewHolder viewHolder;
//
//        context = viewGroup.getContext();
//        alarm_listview_item alarmListviewItem = arrayList.get(i);
//
//        //listview.item을 inflate해서 convertview를 참조
//        if(view ==null)
//        {
//            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            view = inflater.inflate(R.layout.alarm_listview_item, viewGroup, false);
//        }
//
//        //화면에 보여질 데이터 참조
//        ImageView alarm_circle = (ImageView)view.findViewById(R.id.alarm_circle);
//        TextView alarm_text = (TextView)view.findViewById(R.id.alarm_text);
//
//        alarm_circle.setImageDrawable(alarmListviewItem.getDrawable());
//        alarm_text.setText(alarmListviewItem.getAlarm_text());
//
//        return view;
}
