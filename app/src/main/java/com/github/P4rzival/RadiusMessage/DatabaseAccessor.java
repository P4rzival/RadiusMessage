package com.github.P4rzival.RadiusMessage;

import android.os.AsyncTask;

import org.json.JSONObject;

public class DatabaseAccessor {

    public DatabaseAccessor() { }

    public static void databaseFakeRequestApproval(PostRequestSupervisor postRequestSupervisor) {
        postRequestSupervisor.prsContinueTrue();
    }

    public static void databaseRequestApproval(PostRequestSupervisor postRequestSupervisor) {
        new DatabaseRequestTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, postRequestSupervisor);
    }


    private static class DatabaseRequestTask extends AsyncTask<PostRequestSupervisor, Long, Integer> {

        long timeoutCounter = 0l;
        long previousTime = System.currentTimeMillis();
        long currentTime = previousTime;

        @Override
        protected Integer doInBackground(PostRequestSupervisor... postRequestSupervisors) {

            databaseFakeRequestApproval(postRequestSupervisors[0]);

            return 0;
        }

        @Override
        protected void onProgressUpdate(Long... progress) {
            timeoutCounter += progress[0];
        }

        @Override
        protected void onPostExecute(Integer result) {
        }
    }

}
