package io.github.tmatz.thumbsup;

import android.app.*;
import android.content.*;
import android.service.notification.*;
import android.widget.*;
import java.util.*;

import android.text.ClipboardManager;

public class NotificationService extends NotificationListenerService
{
    public static final String EXECUTE_NOTIFICATION_ACTION = "io.github.tmatz.thumbsup.EXECUTE_NOTIFICATION_ACTION";
    public static final String EXTRA_ACTION = "io.github.tmatz.thumbsup.EXTRA_ACTION";
    public static final String ACTION_LOVE = "ACTION_THUMBS_UP";
    public static final String ACTION_DONT_LOVE = "ACTION_THUMBS_DOWN";
    public static final String ACTION_TOGGLE_LOVE = "ACTION_TOGGLE_THUMBS_UPDOWN";
    public static final String ACTION_DISLIKE = "ACTION_DISLIKE";

    private static final String PACKAGE_NAME_SPOTIFY = "com.spotify.music";    
    private static final String TITLE_LOVE = "このトラックはいいね!";
    private static final String TITLE_DONT_LOVE = "[このトラックはいいね!]に設定済み";
    private static final String TITLE_ADD_TO_LIBRARY = "My Libraryに保存";
    private static final String TITLE_DELETE_FROM_LIBRARY = "My Libraryから削除。";
    private static final String TITLE_DISLIKE = "このトラックは嫌い";

    private PendingIntent mIntentLove;
    private PendingIntent mIntentDontLove;
    private PendingIntent mIntentAddToLibrary;
    private PendingIntent mIntentDeleteFromLibrary;
    private PendingIntent mIntentDislike;

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if (EXECUTE_NOTIFICATION_ACTION.equals(intent.getAction()))
        {
            String action = intent.getStringExtra(EXTRA_ACTION);
            switch (action)
            {
                case ACTION_LOVE:
                    sendLove();
                    break;

                case ACTION_DONT_LOVE:
                    sendDontLove();
                    break;

                case ACTION_TOGGLE_LOVE:
                    sendToggleLove();
                    break;

                case ACTION_DISLIKE:
                    sendDislike();
                    break;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn)
    {
        super.onNotificationPosted(sbn);

        if (!PACKAGE_NAME_SPOTIFY.equals(sbn.getPackageName()))
        {
            return;
        }

        //ShowNotificationInfo(sbn.getNotification());

        ClearIntent();
        for (Notification.Action action: sbn.getNotification().actions)
        {
            switch (action.title.toString().trim())
            {
                case TITLE_LOVE:
                    mIntentLove = action.actionIntent;
                    break;

                case TITLE_DONT_LOVE:
                    mIntentDontLove = action.actionIntent;
                    break;

                case TITLE_ADD_TO_LIBRARY:
                    mIntentAddToLibrary = action.actionIntent;
                    break;

                case TITLE_DELETE_FROM_LIBRARY:
                    mIntentDeleteFromLibrary = action.actionIntent;
                    break;

                case TITLE_DISLIKE:
                    mIntentDislike = action.actionIntent;
                    break;
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn, NotificationListenerService.RankingMap rankingMap)
    {
        super.onNotificationRemoved(sbn, rankingMap);

        if (!PACKAGE_NAME_SPOTIFY.equals(sbn.getPackageName()))
        {
            return;
        }

        ClearIntent();
    }

    private void ClearIntent()
    {
        mIntentLove = null;
        mIntentDontLove = null;
        mIntentAddToLibrary = null;
        mIntentDeleteFromLibrary = null;
        mIntentDislike = null;
    }

    private static void startService(Context context, String action)
    {
        Intent intent = new Intent(context, NotificationService.class);
        intent.setAction(EXECUTE_NOTIFICATION_ACTION);
        intent.putExtra(EXTRA_ACTION, action);
        context.startService(intent);
    }

    public static void love(Context context)
    {
        startService(context, ACTION_LOVE);
    }

    public static void dontLove(Context context)
    {
        startService(context, ACTION_DONT_LOVE);
    }

    public static void toggleLove(Context context)
    {
        startService(context, ACTION_TOGGLE_LOVE);
    }

    public static void dislike(Context context)
    {
        startService(context, ACTION_DISLIKE);
    }

    private PendingIntent getLoveIntent()
    {
        if (mIntentLove != null)
        {
            return mIntentLove;
        }

        if (mIntentAddToLibrary != null)
        {
            return mIntentAddToLibrary;
        }

        return null;
    }

    private PendingIntent getDontLoveIntent()
    {
        if (mIntentDontLove != null)
        {
            return mIntentDontLove;
        }

        if (mIntentDeleteFromLibrary != null)
        {
            return mIntentDeleteFromLibrary;
        }

        return null;
    }

    private void sendLove()
    {
        sendPendingIntent(getLoveIntent());
    }

    private void sendDontLove()
    {
        sendPendingIntent(getDontLoveIntent());
    }

    private void sendToggleLove()
    {
        if (getLoveIntent() != null)
        {
            sendLove();
        }
        else if (getDontLoveIntent() != null)
        {
            sendDontLove();
        }
    }

    private void sendDislike()
    {
        sendPendingIntent(mIntentDislike);
    }

    private void sendPendingIntent(PendingIntent intent)
    {
        if (intent != null)
        {
            try
            {
                intent.send();
            }
            catch (PendingIntent.CanceledException e)
            {}            
        }
    }

    private String infoToString(ArrayList<CharSequence> values)
    {
        StringBuilder sb = new StringBuilder();
        if (values.size() > 0)

        {
            sb.append(values.get(0));
            for (int i = 1; i < values.size(); i++)
            {
                sb.append("," + values.get(i));
            }
            sb.append(";");
        }
        return sb.toString();
    }

    private void ShowNotificationInfo(Notification notification)
    {
        StringBuilder sb = new StringBuilder();
        for (Notification.Action action: notification.actions)
        {
            ArrayList<CharSequence> infos = new ArrayList<>();
            infos.add(action.title);
            infos.add("" + action.icon);
            if (action.getIcon() != null)
            {
                infos.add("icon=" + action.getIcon().toString());
            }
            sb.append(infoToString(infos));
        }
        String info = sb.toString();

        Toast.makeText(this, info, Toast.LENGTH_LONG).show();

        ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null)
        {
            clipboard.setText(info);
        }
    }
}
