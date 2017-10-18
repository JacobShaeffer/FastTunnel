package com.shaeffer.jacob;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.shaeffer.jacob.Control;
import com.shaeffer.jacob.minor.ScreenService;

public class IOSLauncher extends IOSApplication.Delegate implements ScreenService {
    //TODO: set fullscreen portrait, screen always on, get unique device id?, adds?

    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        return new IOSApplication(new Control(this), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }

    @Override
    public void keepScreenOn(boolean keepScreenOn) {

    }
}