package io.github.tmatz.thumbsup;

import android.content.*;
import android.os.*;
import android.widget.*;

public class FireReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle bundle = intent.getBundleExtra(LocaleIntent.BUNDLE);
        final String action = bundle.getString(ThumbsUpIntent.EXTRA_ACTION);
        switch (action)
        {
            case ThumbsUpIntent.ACTION_LOVE:
                NotificationService.love(context);
                break;

            case ThumbsUpIntent.ACTION_DONT_LOVE:
                NotificationService.dontLove(context);
                break;

            case ThumbsUpIntent.ACTION_TOGGLE_LOVE:
                NotificationService.toggleLove(context);
                break;

            case ThumbsUpIntent.ACTION_DISLIKE:
                NotificationService.dislike(context);
                break;
        }
    }
}
