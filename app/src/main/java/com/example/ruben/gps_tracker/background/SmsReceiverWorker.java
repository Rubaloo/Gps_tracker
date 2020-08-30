package com.example.ruben.gps_tracker.background;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SmsReceiverWorker extends Worker {

    public SmsReceiverWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
        
        saveDb();
        return Result.success();
    }

    private void saveDb() {

    }
}
