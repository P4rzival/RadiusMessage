package com.github.P4rzival.RadiusMessage;

import android.os.AsyncTask;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.net.URLEncoder;

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

            // We only have a single PostRequestSupervisor, but AsyncTask insists we have the
            //  option for the possibility of multiple
            PostRequestSupervisor postRequestSupervisor = postRequestSupervisors[0];
            String url;

            try {
                url = "https://radiusmessage.cikeys.com/posts/SubmitPost.php?text=" +
                        URLEncoder.encode(
                                (String) postRequestSupervisor.post.get("user_message_text"))
                        + "&rad=" + postRequestSupervisor.post.get("radius")
                        + "&lat=" + postRequestSupervisor.post.get("latitude")
                        + "&lon=" + postRequestSupervisor.post.get("longitude")
                        + "&der=" + postRequestSupervisor.post.get("message_duration")
                        + "&del=" + postRequestSupervisor.post.get("message_delay")
                        + "&img=" + postRequestSupervisor.post.get("user_message_image");

            } catch (JSONException e) {
                System.out.println("Debug: Problem in DatabaseRequestTask: " + e.getMessage());
                e.printStackTrace();

                // TODO 20200521: Is this what we do here? Honestly, if this messes up, the app
                //  should just quit.
                return -1;
            }

            StringRequest stringRequest =
                    new StringRequest(url, new DatabaseRequestListener(),
                            new DatabaseRequestErrorListener());

            requestQueue.add(stringRequest);

            // TODO 20200521: We can't actually know that the network request is done, but we'll
            //  call it good enough here for now
            postRequestSupervisor.prsContinueTrue();

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

                    System.out.println("Debug: Cool post alert: "
                            + response.getJSONObject(i).get("user_message_text"));
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
        // TODO From 20200521: Should this have a handler in case GPS access isn't available?
        //  This currently produces a NullPointerException in that case.
        GeoPoint currentLocation = UserLocationManager.getInstance().getCurrentLocationAsGeoPoint();
        getPostsFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude());
    }


    private static class DatabaseRequestListener implements Response.Listener<String> {
        @Override
        public void onResponse(String response) {
            // There are no strings that give us a clue as to whether or not it worked at the
            //  moment, so just do nothing for now
        }
    }

    private static class DatabaseRequestErrorListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {

            System.out.println("Error in DatabaseRequestTask: " + error.getMessage());

            Toast.makeText(RadiusMessage.getAppInstance().getApplicationContext(),
                    "Failed to make post. Check network connection.",
                    Toast.LENGTH_SHORT).show();
        }

    }

}

