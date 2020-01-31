package com.bitstamp;

import com.firebase.jobdispatcher.JobService;

public class PriceAlert extends JobService {
    @Override
    public boolean onStopJob(com.firebase.jobdispatcher.JobParameters job) {
        return false;
    }

    @Override
    public boolean onStartJob(com.firebase.jobdispatcher.JobParameters job) {
        FetchPriceAlert fetchPriceAlert = new FetchPriceAlert(getApplicationContext());
        fetchPriceAlert.execute();
        return true;
    }

}