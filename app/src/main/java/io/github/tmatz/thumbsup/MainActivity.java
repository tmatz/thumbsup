package io.github.tmatz.thumbsup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final Button buttonLove = findViewById(R.id.buttonLove);
        final Button buttonDontLove = findViewById(R.id.buttonDontLove);
        final Button buttonToggleLove = findViewById(R.id.buttonToggleLove);
        final Button buttonDislike = findViewById(R.id.buttonDislike);
        final Button buttonNotificationSetting = findViewById(R.id.buttonNotificationSetting);

        buttonLove.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View p1)
                {
                    NotificationService.love(MainActivity.this);
                }
            });

        buttonDontLove.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View p1)
                {
                    NotificationService.dontLove(MainActivity.this);
                }
            });

        buttonToggleLove.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View p1)
                {
                    NotificationService.toggleLove(MainActivity.this);
                }
            });

        buttonDislike.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View p1)
                {
                    NotificationService.dislike(MainActivity.this);
                }
            });

        buttonNotificationSetting.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View p1)
                {
                    startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                }
            });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        NotificationService.enableShowNotificationInfo(true);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        NotificationService.enableShowNotificationInfo(false);
    }
}
