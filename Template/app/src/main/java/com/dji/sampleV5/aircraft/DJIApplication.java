package com.dji.sampleV5.aircraft;

import android.app.Application;
import dji.v5.manager.SDKManager;
import dji.v5.manager.interfaces.SDKManagerCallback;
import dji.v5.common.error.IDJIError;
import dji.v5.common.register.DJISDKInitEvent;

public class DJIApplication extends Application {
    private static final String TAG = "DJIApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize DJI SDK
        SDKManager.getInstance().init(this, new SDKManagerCallback() {
            @Override
            public void onRegisterSuccess() {
                // SDK registration successful
                System.out.println("DJI SDK Registration Success");
            }

            @Override
            public void onRegisterFailure(IDJIError error) {
                // SDK registration failed
                System.out.println("DJI SDK Registration Failed: " + error.description());
            }

            @Override
            public void onProductDisconnect(int productId) {
                // Product disconnected
                System.out.println("Product Disconnected");
            }

            @Override
            public void onProductConnect(int productId) {
                // Product connected
                System.out.println("Product Connected");
            }

            @Override
            public void onProductChanged(int productId) {
                // Product changed
                System.out.println("Product Changed");
            }

            @Override
            public void onInitProcess(DJISDKInitEvent event, int progress) {
                // Initialization progress
                System.out.println("Init Progress: " + event.name() + " " + progress + "%");
            }

            @Override
            public void onDatabaseDownloadProgress(long current, long total) {
                // Database download progress
                System.out.println("Database Download: " + current + "/" + total);
            }
        });
    }
}