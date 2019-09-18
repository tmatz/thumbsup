package io.github.tmatz.thumbsup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;

public class EditActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit);

        Button buttonOk = findViewById(R.id.editButtonOk);
        buttonOk.setOnClickListener(
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
        Intent intent;

        RadioGroup actionGroup = findViewById(R.id.editRadioGroupAction);
        switch (actionGroup.getCheckedRadioButtonId())
        {
            case R.id.editRadioButtonLove:
                intent = createIntent("Love", ThumbsUpIntent.ACTION_LOVE);
                break;

            case R.id.editRadioButtonDontLove:
                intent = createIntent("Dont Love", ThumbsUpIntent.ACTION_DONT_LOVE);
                break;

            case R.id.editRadioButtonToggleLove:
                intent = createIntent("Toggle Love", ThumbsUpIntent.ACTION_TOGGLE_LOVE);
                break;

            case R.id.editRadioButtonDislike:
                intent = createIntent("Dislike", ThumbsUpIntent.ACTION_DISLIKE);
                break;

            default:
                return;
        }

        setResult(RESULT_OK, intent);
        finish();
    }

    private Intent createIntent(String blurb, String extraAction)
    {
        Bundle bundle = new Bundle();
        bundle.putString(ThumbsUpIntent.EXTRA_ACTION, extraAction);
        Intent intent = new Intent();
        intent.putExtra(LocaleIntent.BLURB, blurb);
        intent.putExtra(LocaleIntent.BUNDLE, bundle);
        return intent;
    }
}
