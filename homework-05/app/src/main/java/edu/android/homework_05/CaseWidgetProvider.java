package edu.android.homework_05;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * @author liosha on 24.05.2016.
 */
public class CaseWidgetProvider extends AppWidgetProvider {
    public static final String ACTION_CASE_CHANGED = "edu.android.homework_05.ACTION_CASE_CHANGED";
    private boolean isDark = true;
    private boolean isLeft = true;
    private boolean isRight = true;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MainActivity.IS_DARK_KEY, isDark);
        intent.putExtra(MainActivity.IS_LEFT_KEY, isLeft);
        intent.putExtra(MainActivity.IS_RIGHT_KEY, isRight);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_case);
        views.setOnClickPendingIntent(R.id.btn_buy, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetIds[0], views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (ACTION_CASE_CHANGED.equals(intent.getAction())) {
            this.isDark = intent.getBooleanExtra(MainActivity.IS_DARK_KEY, true);
            this.isLeft = intent.getBooleanExtra(MainActivity.IS_LEFT_KEY, true);
            this.isRight = intent.getBooleanExtra(MainActivity.IS_RIGHT_KEY, true);

            Log.d("MY", "Values received! " + intent.getAction());
        }
    }
}
