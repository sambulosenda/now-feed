package com.jaellysbales.nowfeed;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by c4q-rosmary on 6/27/15.
 */
public class CardAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Activity activity;
    private List<Fragment> cards;
    private Context context;

    public CardAdapter(Activity activity, List<Fragment> cards) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.cards = cards;
        this.activity = activity;
    }

    public CardAdapter(Context context){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @Override
    public int getCount() {// uses get count to determine how many items to have in the list
//        return cards.size();
        return 1;
    } // shows how many items are in the list view. needs updating

    @Override
    public Object getItem(int i) {
//        return cards.get(i);
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) { // what is being passed into listView in Main
//        ViewHolder holder;
//
//        if (view == null) {
//            view = inflater.inflate(R.layout.adapter_row, null);
//            holder = new ViewHolder();
//            holder.fragment = (LinearLayout) view.findViewById(R.id.fragment);
//            view.setTag(holder);
//        } else {
//            holder = (ViewHolder) view.getTag();
//        }
//
//        Log.d("CardAdapter", "CREATING FRAGMENT");
//
//        try{
//            // Do fragment transaction here
//            activity.getFragmentManager().beginTransaction()
//                    .add(R.id.fragment, cards.get(i))
//                    .commit();
//
//        }catch (Exception e){
//            Log.d("CardAdapter", "ERROR CREATING VIEW");
//        }
//
//        return view;

        return AlarmCard.createAlarmView(inflater);
    }

    static class ViewHolder {
        LinearLayout fragment;
    }
}
