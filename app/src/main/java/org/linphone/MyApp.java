package org.linphone;

import android.app.Application;
import android.content.res.Configuration;
import com.stripe.android.PaymentConfiguration;

public class MyApp extends Application {

    private static Application applicationInstance;

    public static synchronized Application getInstance() {
        return applicationInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationInstance = this;
        PaymentConfiguration.init(Constants.STRIPE_PUBLISHABLE_KEY_LIVE);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
