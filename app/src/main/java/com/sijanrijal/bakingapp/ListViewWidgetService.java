package com.sijanrijal.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

public class ListViewWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListViewsRemoteViewsFactory(this.getApplicationContext(), intent);
    }

}

class ListViewsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private ArrayList<String> ingredients;

    public ListViewsRemoteViewsFactory(Context context, Intent intent) {

        mContext = context;

        //initialize the ingredient list for the widget
        ingredients = new ArrayList<>();
        ingredients.add(intent.getStringExtra("INGREDIENT"));
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        ingredients.clear();
        ingredients.add(BakingWidget.ingredient);
    }

    @Override
    public void onDestroy() {
        ingredients.clear();
    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(),
                R.layout.widget_item);
        remoteViews.setTextViewText(R.id.item, ingredients.get(position));
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
