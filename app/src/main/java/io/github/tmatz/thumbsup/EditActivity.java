package io.github.tmatz.thumbsup;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

public class EditActivity extends Activity
{
    private RadioGroup mActionGroup;
    private Button mButtonOk;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit);

        mActionGroup = findViewById(R.id.editRadioGroupAction);

        mButtonOk = findViewById(R.id.editButtonOk);
        mButtonOk.setOnClickListener(
            new OnClickListener()
            {
                @Override
                public void onClick(View p1)
                {
                    onButtonOkClicked();
                }
            });
    }

    private void onButtonOkClicked()
    {
        String blurb = null;
        Bundle bundle = new Bundle();

        switch (mActionGroup.getCheckedRadioButtonId())
        {
            case R.id.editRadioButtonLove:
                blurb = "Love";
                bundle.putString(ThumbsUpIntent.EXTRA_ACTION, ThumbsUpIntent.ACTION_LOVE);
                break;
            case R.id.editRadioButtonDontLove:
                blurb = "Dont Love";
                bundle.putString(ThumbsUpIntent.EXTRA_ACTION, ThumbsUpIntent.ACTION_DONT_LOVE);
                break;
            case R.id.editRadioButtonToggleLove:
                blurb = "Toggle Love";
                bundle.putString(ThumbsUpIntent.EXTRA_ACTION, ThumbsUpIntent.ACTION_TOGGLE_LOVE);
                break;
            case R.id.editRadioButtonDislike:
                blurb = "Dislike";
                bundle.putString(ThumbsUpIntent.EXTRA_ACTION, ThumbsUpIntent.ACTION_DISLIKE);
                break;
        }

        Intent intent = new Intent();
        intent.putExtra(LocaleIntent.BLURB, blurb);
        intent.putExtra(LocaleIntent.BUNDLE, bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}
