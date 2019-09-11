package io.github.tmatz.thumbsup;

import android.content.Context;
import android.content.Intent;

public class ThumbsUp
{
    public static void love(Context context)
    {
        startService(context, NotificationService.ACTION_LOVE);
    }

    public static void dontLove(Context context)
    {
        startService(context, NotificationService.ACTION_DONT_LOVE);
    }

    public static void toggleLove(Context context)
    {
        startService(context, NotificationService.ACTION_TOGGLE_LOVE);
    }

    public static void dislike(Context context)
    {
        startService(context, NotificationService.ACTION_DISLIKE);
    }

    public static void dump(Context context)
    {
        startService(context, NotificationService.ACTION_DUMP);
    }

    private static void startService(Context context, String action)
    {
        Intent intent = new Intent(context, NotificationService.class);
        intent.setAction(NotificationService.EXECUTE_NOTIFICATION_ACTION);
        intent.putExtra(NotificationService.EXTRA_ACTION, action);
        context.startService(intent);
    }
}
