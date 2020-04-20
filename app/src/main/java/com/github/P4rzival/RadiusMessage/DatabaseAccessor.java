package com.github.P4rzival.RadiusMessage;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class DatabaseAccessor {

    public DatabaseAccessor() { }

    public void databaseFakeRequestApproval(PostRequestSupervisor postRequestSupervisor) {
        postRequestSupervisor.prsContinueTrue();
    }

    public void databaseRequestApproval(PostRequestSupervisor postRequestSupervisor) {
        new databaseRequestTask().execute(postRequestSupervisor);
    }

    public static void getPostsFromLocation(double latitude, double longitude) throws MalformedURLException {
        // TODO: This is hardcoded for now until we can figure out how to make it related to level
        //  of zoom
        double radius = 200;

        try {
            InputStreamReader streamReader =
                    new InputStreamReader(new URL(
                        "https://radiusmessage.cikeys.com/posts/GetPostsFromLocation.php?lat="
                                + latitude + "&lon=" + longitude + "&rad=" + radius).openStream());

            //reader.beginArray();
            //while(reader.hasNext()) {
            //    reader
            //}

        }catch(MalformedURLException murlE) {
            System.out.print(murlE.getMessage());
            Toast.makeText(RadiusMessage.getAppInstance().getApplicationContext(),
                    "Failed to get new posts. Check network connection.",
                    Toast.LENGTH_SHORT);
        }catch(Exception e) {
            System.out.print(e.getMessage());
            Toast.makeText(RadiusMessage.getAppInstance().getApplicationContext(),
                    "Failed to get new posts. Check network connection.",
                    Toast.LENGTH_SHORT);
        }
    }


    private class databaseRequestTask extends AsyncTask<PostRequestSupervisor, Long, Integer> {

        long timeoutCounter = 0l;
        long previousTime = System.currentTimeMillis();
        long currentTime = previousTime;

        @Override
        protected Integer doInBackground(PostRequestSupervisor... postRequestSupervisors) {

            //InputStreamReader streamReader =
            //        new InputStreamReader(new URL(
            //                "https://radiusmessage.cikeys.com/posts/GetPostsFromLocation.php?lat="
            //                        + latitude + "&lon=" + longitude + "&rad=" + radius).openStream());

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
