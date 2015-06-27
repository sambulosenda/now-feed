package com.jaellysbales.nowfeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by c4q-rosmary on 6/27/15.
 */
public class CardAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;

    public CardAdapter(Context context){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @Override
    public int getCount() {// uses get count to determine how many items to have in the list
        return 1;
    } // shows how many items are in the list view. needs updating

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) { // what is being passed into listView in Main

        return  AlarmCard.createAlarmView(inflater);
    }
}
