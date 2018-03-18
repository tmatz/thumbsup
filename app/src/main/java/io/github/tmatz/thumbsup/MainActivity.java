package io.github.tmatz.thumbsup;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.View.*;
import android.view.*;

public class MainActivity extends Activity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final Button buttonLove = (Button)findViewById(R.id.buttonLove);
        final Button buttonDontLove = (Button)findViewById(R.id.buttonDontLove);
        final Button buttonToggleLove = (Button)findViewById(R.id.buttonToggleLove);
        final Button buttonDislike = (Button)findViewById(R.id.buttonDislike);

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
    }
}
