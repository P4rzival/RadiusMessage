package com.github.P4rzival.RadiusMessage;

import android.os.AsyncTask;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

public class DatabaseAccessor {

    public static RequestQueue requestQueue =
            Volley.newRequestQueue(RadiusMessage.getAppInstance().getApplicationContext());

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

    public static void getPostsFromLocation(double latitude, double longitude) {
        // TODO: This is hardcoded for now until we can figure out how to make it related to level
        //  of zoom
        double radius = 200;

        String url = "https://radiusmessage.cikeys.com/posts/GetPostsFromLocation.php?lat="
                + latitude + "&lon=" + longitude + "&rad=" + radius;

        // Volley doesn't have proper documentation, so I'll rattle off the parameters here as of
        //  20200521.
        // method: the HTTP method to use
        // url: URL to fetch the JSON from
        // jsonRequest: A JSONArray to post with the request. Null is allowed and
        //               indicates no parameters will be posted along with request.
        // listener: Listener to receive the JSON response
        // errorListener: Error listener, or null to ignore errors.
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET,
                url, null, new GetPostsRequestListener(),
                new GetPostsRequestErrorListener());

        requestQueue.add(jsonObjectRequest);
    }

    private static class GetPostsRequestListener implements Response.Listener<JSONArray> {
        @Override
        public void onResponse(JSONArray response) {

            PostDrawer postDrawer = new PostDrawer();

            try {
                for(int i=0; i<response.length(); i++) {
                    postDrawer.createPost(response.getJSONObject(i));
                }
            } catch(JSONException je) {
                System.out.println("Error in GetPostsRequestListener.onResponse: "
                + je.getMessage());
            }
        }
    }

    private static class GetPostsRequestErrorListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {

            System.out.println("Error in getPostsFromLocation: " + error.getMessage());

            Toast.makeText(RadiusMessage.getAppInstance().getApplicationContext(),
                    "Failed to get new posts. Check network connection.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static void getPostsFromCurrentLocation() {
        GeoPoint currentLocation = UserLocationManager.getInstance().getCurrentLocationAsGeoPoint();
        getPostsFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude());
    }

}
