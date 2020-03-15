package com.github.P4rzival.RadiusMessage;

import android.os.AsyncTask;

import org.json.*;

import java.util.ArrayDeque;

public class PostRequestSupervisor {

    private boolean shouldContinue = false;

    JSONObject post;

    long timeoutCounter = 0l;

    static ArrayDeque<PostRequestSupervisor> waitingPostRequestSupervisors
            = new ArrayDeque<PostRequestSupervisor>();

    public PostRequestSupervisor(JSONObject newPost) {
        prsContinueFalse();
        this.post = newPost;
        timeoutCounter = 0;
        waitingPostRequestSupervisors.add(this);
        new SpinlockTask().execute(this);
    }

    private class SpinlockTask extends AsyncTask<PostRequestSupervisor, Long, Integer> {

        long previousTime = System.currentTimeMillis();
        long currentTime = previousTime;

        @Override
        protected Integer doInBackground(PostRequestSupervisor... postRequestSupervisors) {

            DatabaseAccessor.databaseRequestApproval(postRequestSupervisors[0]);

            // Spinlock
            while(!prsContinue())
            {
                currentTime = System.currentTimeMillis();
                publishProgress(currentTime - previousTime);
                previousTime = currentTime;
            }

            //(new PostDrawer()).createPost(this.post);
            return 0;
        }

        @Override
        protected void onProgressUpdate(Long... progress) {
            timeoutCounter += progress[0];
        }

        @Override
        protected void onPostExecute(Integer result) {
            waitingPostRequestSupervisors.remove(this);
        }
    }

    boolean prsContinue() {
        return this.shouldContinue;
    }

    boolean prsContinueTrue() {
        return this.shouldContinue = true;
    }

    boolean prsContinueFalse() {
        return this.shouldContinue = false;
    }

    boolean prsContinueToggle() {
        return this.shouldContinue = !this.shouldContinue;
    }
}
