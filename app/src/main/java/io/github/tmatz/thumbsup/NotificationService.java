package io.github.tmatz.thumbsup;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.ClipboardManager;
import android.widget.Toast;

public class NotificationService extends NotificationListenerService
{
    public static final String EXECUTE_NOTIFICATION_ACTION = "io.github.tmatz.thumbsup.EXECUTE_NOTIFICATION_ACTION";
    public static final String EXTRA_ACTION = "io.github.tmatz.thumbsup.EXTRA_ACTION";
    public static final String ACTION_LOVE = "ACTION_THUMBS_UP";
    public static final String ACTION_DONT_LOVE = "ACTION_THUMBS_DOWN";
    public static final String ACTION_TOGGLE_LOVE = "ACTION_TOGGLE_THUMBS_UPDOWN";
    public static final String ACTION_DISLIKE = "ACTION_DISLIKE";
    public static final String ACTION_DUMP = "ACTION_DUMP";

    private static final String PACKAGE_NAME_SPOTIFY = "com.spotify.music";    
    private static final String[] TITLE_LOVE = { "Like", "いいね！" };
    private static final String[] TITLE_DONT_LOVE = { "Unlike", "イマイチ" };
    private static final String[] TITLE_ADD_TO_LIBRARY = { "????", "My Libraryに保存" };
    private static final String[] TITLE_DELETE_FROM_LIBRARY = { "????", "My Libraryから削除。" };
    private static final String[] TITLE_DISLIKE = { "Hide this song", "この曲を非表示にする" };

    private TonePlayer mTonePlayer;

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mTonePlayer = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if (EXECUTE_NOTIFICATION_ACTION.equals(intent.getAction()))
        {
            String actionName = intent.getStringExtra(EXTRA_ACTION);
            IServiceAction action = createServiceAction(actionName);
            action.execute();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private IServiceAction createServiceAction(String action)
    {
        switch (action)
        {
            case ACTION_LOVE:
                return new ActionLove();

            case ACTION_DONT_LOVE:
                return new ActionDontLove();

            case ACTION_TOGGLE_LOVE:
                return new ActionToggleLove();

            case ACTION_DISLIKE:
                return new ActionDislike();

            case ACTION_DUMP:
                return new ActionDump();

            default:
                return new ActionNop();
        }
    }

    private Notification getNotification(String packageName)
    {
        for (StatusBarNotification notification: getActiveNotifications())
        {
            if (packageName.equals(notification.getPackageName()))
            {
                return notification.getNotification();
            }
        }
        return null;
    }

    private Notification.Action getNotificationAction(String[]... titles)
    {
        Notification notification = getNotification(PACKAGE_NAME_SPOTIFY);
        if (notification == null)
        {
            return null;
        }

        for (Notification.Action action: notification.actions)
        {
            String title = action.title.toString().trim();
            if (StringUtils.existIn(title, titles) && action.actionIntent != null)
            {
                return action;
            }
        }

        return null;
    }

    private Notification.Action getLoveAction()
    {
        return getNotificationAction(TITLE_LOVE, TITLE_ADD_TO_LIBRARY);
    }

    private Notification.Action getDontLoveAction()
    {
        return getNotificationAction(TITLE_DONT_LOVE, TITLE_DELETE_FROM_LIBRARY);
    }

    private Notification.Action getDislikeAction()
    {
        return getNotificationAction(TITLE_DISLIKE);
    }

    private void sendNotificationAction(Notification.Action action)
    {
        if (action == null || action.actionIntent == null)
        {
            getTonePlayer().toneFailed();
            return;
        }

        try
        {
            action.actionIntent.send();
            getTonePlayer().toneSuccess();
        }
        catch (PendingIntent.CanceledException e)
        {
            getTonePlayer().toneError();
        }
    }

    private TonePlayer getTonePlayer()
    {
        if (mTonePlayer == null)
        {
            mTonePlayer = new TonePlayer();
        }
        return mTonePlayer;
    }

    private class TonePlayer
    {
        private final ToneGenerator mToneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);

        public void toneSuccess()
        {
            playTone(ToneGenerator.TONE_PROP_ACK);
        }

        public void toneFailed()
        {
            playTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE);
        }

        public void toneError()
        {
            playTone(ToneGenerator.TONE_SUP_INTERCEPT);
        }

        private void playTone(int tone)
        {
            mToneGenerator.startTone(tone);
        }
    }

    private interface IServiceAction
    {
        void execute();
    }

    private class ActionLove implements IServiceAction
    {
        public void execute()
        {
            // already marked
            if (getDontLoveAction() != null)
            {
                getTonePlayer().toneSuccess();
                return;
            }

            sendNotificationAction(getLoveAction());
        }
    }

    private class ActionDontLove implements IServiceAction
    {
        public void execute()
        {
            // already marked
            if (getLoveAction() != null)
            {
                getTonePlayer().toneSuccess();
                return;
            }

            sendNotificationAction(getDontLoveAction());
        }
    }

    private class ActionToggleLove implements IServiceAction
    {
        public void execute()
        {
            Notification.Action action = getLoveAction();

            if (action == null)
            {
                action = getDontLoveAction();
            }

            sendNotificationAction(action);
        }
    }

    private class ActionDislike implements IServiceAction
    {
        public void execute()
        {
            sendNotificationAction(getDislikeAction());
        }
    }

    private class ActionDump implements IServiceAction
    {
        public void execute()
        {
            Notification notification = getNotification(PACKAGE_NAME_SPOTIFY);
            String info = toString(notification);
            Toast.makeText(NotificationService.this, info, Toast.LENGTH_LONG).show();
            copyToClipboard(info);
        }

        private String toString(Notification notification)
        {
            if (notification == null)
            {
                return "no notification";
            }

            StringBuilder sb = new StringBuilder();

            for (Notification.Action action: notification.actions)
            {
                sb.append(action.title);
                sb.append(";");
            }

            return sb.toString();
        }

        private void copyToClipboard(String text)
        {
            ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null)
            {
                clipboard.setText(text);
            }
        }
    }

    private class ActionNop implements IServiceAction
    {
        @Override
        public void execute()
        {
            // nop
        }
    }

    private static final class StringUtils
    {
        public static boolean existIn(String str, String[]... arrays)
        {
            if (str == null)
            {
                return false;
            }

            for (String[] a: arrays)
            {
                if (existIn(str, a))
                {
                    return true;
                }
            }

            return false;
        }

        public static boolean existIn(String str, String ...strings)
        {
            if (str == null)
            {
                return false;
            }

            for (String s: strings)
            {
                if (str.equals(s))
                {
                    return true;
                }
            }

            return false;
        }
    }
}
