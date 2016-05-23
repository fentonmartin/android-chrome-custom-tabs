package fen.code.chromecustomtabs;

import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * This Activity connect to the Chrome Custom Tabs Service on startup, and allows you to decide
 * when to call mayLaunchUrl.
 */
public class ServiceConnectionActivity extends AppCompatActivity
        implements View.OnClickListener, CustomTabActivityHelper.ConnectionCallback {
    private EditText mUrlEditText;
    private View mMayLaunchUrlButton;
    private CustomTabActivityHelper customTabActivityHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_connection);

        customTabActivityHelper = new CustomTabActivityHelper();
        customTabActivityHelper.setConnectionCallback(this);

        mUrlEditText = (EditText) findViewById(R.id.url);
        mMayLaunchUrlButton = findViewById(R.id.button_may_launch_url);
        mMayLaunchUrlButton.setEnabled(false);
        mMayLaunchUrlButton.setOnClickListener(this);

        findViewById(R.id.start_custom_tab).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        customTabActivityHelper.setConnectionCallback(null);
    }

    @Override
    public void onCustomTabsConnected() {
        mMayLaunchUrlButton.setEnabled(true);
    }

    @Override
    public void onCustomTabsDisconnected() {
        mMayLaunchUrlButton.setEnabled(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        customTabActivityHelper.bindCustomTabsService(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        customTabActivityHelper.unbindCustomTabsService(this);
        mMayLaunchUrlButton.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        Uri uri  = Uri.parse(mUrlEditText.getText().toString());
        switch (viewId) {
            case R.id.button_may_launch_url:
                customTabActivityHelper.mayLaunchUrl(uri, null, null);
                break;
            case R.id.start_custom_tab:
                CustomTabsIntent customTabsIntent =
                        new CustomTabsIntent.Builder(customTabActivityHelper.getSession())
                                .build();
                CustomTabActivityHelper.openCustomTab(
                        this, customTabsIntent, uri, new WebviewFallback());
                break;
            default:
                //Unkown View Clicked
        }
    }
}
