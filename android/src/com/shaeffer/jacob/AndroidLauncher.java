package com.shaeffer.jacob;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.shaeffer.jacob.minor.ScreenService;


public class AndroidLauncher extends AndroidApplication implements ScreenService{

    View gameView;

    @Override
	protected void onCreate (Bundle savedInstanceState) {
        hideSystemUI();

		super.onCreate(savedInstanceState);

        RelativeLayout layout = new RelativeLayout(this);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.hideStatusBar = true;
        config.useImmersiveMode = true;
        gameView = initializeForView(new Control(this), config);
        layout.addView(gameView);
        setContentView(layout);
	}

	@Override
	protected void onResume(){
		//getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);// | View.SYSTEM_UI_FLAG_FULLSCREEN);
        //hideSystemUI();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        super.onResume();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	}

    @Override
    public void keepScreenOn(final boolean isOn) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gameView.setKeepScreenOn(isOn);
            }
        });
    }

    private void hideSystemUI()
    {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
        );
    }

}
