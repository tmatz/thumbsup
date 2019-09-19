package io.github.tmatz.thumbsup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class FireReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle bundle = intent.getBundleExtra(LocaleIntentHelper.BUNDLE);
        final String action = bundle.getString(ThumbsUpIntentHelper.EXTRA_ACTION);
        switch (action)
        {
            case ThumbsUpIntentHelper.ACTION_LOVE:
                NotificationServiceHelper.love(context);
                break;

            case ThumbsUpIntentHelper.ACTION_DONT_LOVE:
                NotificationServiceHelper.dontLove(context);
                break;

            case ThumbsUpIntentHelper.ACTION_TOGGLE_LOVE:
                NotificationServiceHelper.toggleLove(context);
                break;

            case ThumbsUpIntentHelper.ACTION_DISLIKE:
                NotificationServiceHelper.dislike(context);
                break;

            default:
                break;
        }
    }
}
