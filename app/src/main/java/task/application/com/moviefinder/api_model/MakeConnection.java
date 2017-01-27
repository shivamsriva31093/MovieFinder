package task.application.com.moviefinder.api_model;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by shashank on 1/24/2017.
 */


    public class MakeConnection extends AsyncTask<Void,Void,JSONObject> {
        private static final String TAG = "MakeConnection";
        private final String urls;
        private HttpURLConnection urlConnection;
        private BufferedReader reader;
        private String jsonStr;
        private JSONObject error;
        private static final String INVALID = "invalidjson";


        public MakeConnection(String url) {
            this.urls = url;
        }


        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected JSONObject doInBackground(Void... params) {
            connect();
            return get_json();
        }





        public void connect() {
            try {
                URL url = new URL(urls);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(7000);
                urlConnection.connect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public JSONObject get_json() {
            try {
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null)
                    jsonStr = null;
                assert inputStream != null;
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                JSONObject jsonObject = new JSONObject(buffer + "");
                if (jsonObject != null)
                    return jsonObject;
                else
                    error = new JSONObject(INVALID);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return error;
        }




    }

