package com.example.meena.robotrition;

import android.app.Activity;
import android.app.FragmentManagerNonConfig;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by meena on 8/9/2017.
 */

public class FatSecretSearch extends Fragment {
    /**
     * FatSecret Authentication
     * http://platform.fatsecret.com/api/default.aspx?screen=rapiauth
     * Reference
     * https://github.com/ethan-james/cookbox/blob/master/src/com/vitaminc4/cookbox/FatSecret.java
     */

    private String food_id="";
    private String food_name="";
     ArrayList<ArrayList> types= new ArrayList<>();
  /*

    String calcium="";
    String caloires="";
    String carbs="";
    String cholestrol="";
    String fat="";
    String satfat="";
    String iron="";
    String potassium="";
    String protien="";
    String servingMes="";
    String sodium="";
    String sugar="";
    String vitaminA="";*/
    final static private String APP_METHOD = "GET";
    final static private String APP_KEY = "37ae4f7d04d74372a438465b3904081b";
    static private String APP_SECRET = "de528af7c1eb4ae8a9e91e6e4af4215c";
    final static private String APP_URL = "http://platform.fatsecret.com/rest/server.api";
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    JSONObject foods = null;
    CommunicationChannel mCallback;
    Button search;
    Context context;
    int c;
    public interface CommunicationChannel{
        void sendInfo(ArrayList<ArrayList> types);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (CommunicationChannel) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+ " must implement OnHeadlineSelectedListener");
        }
    }
    public FatSecretSearch(String searchFood, final Context context, int page) {
        this.context=context;
        List<String> params = new ArrayList<>(Arrays.asList(generateOauthParams(page)));
        String[] template = new String[1];
        params.add("method=foods.search");
        params.add("search_expression=" + Uri.encode(searchFood));
        params.add("oauth_signature=" + sign(APP_METHOD, APP_URL, params.toArray(template)));
        try {
            URL url = new URL(APP_URL + "?" + paramify(params.toArray(template)));
            FlightThread downloadFlight = new FlightThread();
            downloadFlight.execute(String.valueOf(url));
        } catch (Exception e) {
            Log.w("Fit", e.toString());
            e.printStackTrace();

        }
    }

    private static String[] generateOauthParams(int i) {
        return new String[]{
                "oauth_consumer_key=" + APP_KEY,
                "oauth_signature_method=HMAC-SHA1",
                "oauth_timestamp=" +
                        Long.valueOf(System.currentTimeMillis() * 2).toString(),
                "oauth_nonce=" + nonce(),
                "oauth_version=1.0",
                "format=json",
                "page_number="+i,
                "max_results="+20};
    }


    private static String sign(String method, String uri, String[] params) {
        String[] p = {method, Uri.encode(uri), Uri.encode(paramify(params))};
        String s = join(p, "&");
        APP_SECRET+="&";
        SecretKey sk = new SecretKeySpec(APP_SECRET.getBytes(), HMAC_SHA1_ALGORITHM);
        APP_SECRET=APP_SECRET.substring(0,APP_SECRET.length()-1);
        try {
            Mac m = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            m.init(sk);
            return Uri.encode(new String(Base64.encode(m.doFinal(s.getBytes()), Base64.DEFAULT)).trim());
        } catch (java.security.NoSuchAlgorithmException e) {
            Log.w("FatSecret_TEST FAIL", e.getMessage());

            return null;
        } catch (java.security.InvalidKeyException e) {
            Log.w("FatSecret_TEST FAIL", e.getMessage());

            return null;
        }
    }


    private static String paramify(String[] params) {
        String[] p = Arrays.copyOf(params, params.length);
        Arrays.sort(p);
        return join(p, "&");
    }

    private static String join(String[] array, String separator) {

        StringBuilder b = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i > 0)
                b.append(separator);
            b.append(array[i]);
        }
        return b.toString();
    }

    private static String nonce() {
        Random r = new Random();
        StringBuilder n = new StringBuilder();
        for (int i = 0; i < r.nextInt(8) + 2; i++)
            n.append(r.nextInt(26) + 'a');
        return n.toString();
    }


    public class FlightThread extends AsyncTask<String,Void,Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                URL endpoint = new URL(params[0]);
                URLConnection api = endpoint.openConnection();
                String line;
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(api.getInputStream()));
                while ((line = reader.readLine()) != null)
                    builder.append(line);
                JSONObject foodGet = new JSONObject(builder.toString());
                foods = foodGet.getJSONObject("foods");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                JSONArray array= foods.getJSONArray("food");
                for (int x=0;x<array.length();x++)
                {
                    ArrayList<String> items= new ArrayList<>();
                    items.add(array.getJSONObject(x).getString("food_id"));
                    items.add(array.getJSONObject(x).getString("food_name"));
                    items.add(array.getJSONObject(x).getString("brand_name"));
                    types.add(items);
                }

            } catch(JSONException e){
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            mCallback.sendInfo(types);

        }
    }

}
