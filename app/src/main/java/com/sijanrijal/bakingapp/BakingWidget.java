package com.sijanrijal.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidget extends AppWidgetProvider {

    static String ingredient = "Select a recipe to view its ingredients";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_widget_view);
        // Construct the RemoteViews object and set up the intent
        // that will provide the data to the collection in the widget
        Intent serviceIntent = new Intent(context, ListViewWidgetService.class);
        serviceIntent.putExtra("INGREDIENT", ingredient);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget);
        views.setRemoteAdapter(R.id.list_widget_view, serviceIntent);

        //Create an intent to launch the home screen of the app when clicked
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        views.setPendingIntentTemplate(R.id.list_widget_view, pendingIntent);

        //launch the pending intent
        //views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int appWidgetId: appWidgetIds) {
            updateAppWidget(context, appWidgetManager,appWidgetId);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

