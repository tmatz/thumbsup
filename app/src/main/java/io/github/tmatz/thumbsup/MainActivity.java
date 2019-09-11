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
        final Button buttonDump = findViewById(R.id.buttonDump);

        buttonLove.setOnClickListener(
            new OnClickListener()
            {
                @Override
                public void onClick(View p1)
                {
                    ThumbsUp.love(MainActivity.this);
                }
            });

        buttonDontLove.setOnClickListener(
            new OnClickListener()
            {
                @Override
                public void onClick(View p1)
                {
                    ThumbsUp.dontLove(MainActivity.this);
                }
            });

        buttonToggleLove.setOnClickListener(
            new OnClickListener()
            {
                @Override
                public void onClick(View p1)
                {
                    ThumbsUp.toggleLove(MainActivity.this);
                }
            });

        buttonDislike.setOnClickListener(
            new OnClickListener()
            {
                @Override
                public void onClick(View p1)
                {
                    ThumbsUp.dislike(MainActivity.this);
                }
            });

        buttonNotificationSetting.setOnClickListener(
            new OnClickListener()
            {
                @Override
                public void onClick(View p1)
                {
                    startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                }
            });

        buttonDump.setOnClickListener(
            new OnClickListener()
            {
                @Override
                public void onClick(View p1)
                {
                    ThumbsUp.dump(MainActivity.this);
                }
            });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }
}
