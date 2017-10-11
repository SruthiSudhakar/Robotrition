package com.example.meena.robotrition;


        import android.app.Activity;
        import android.content.Context;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.support.v4.app.Fragment;
        import android.util.Base64;
        import android.util.Log;
        import android.view.ViewGroup;
        import android.widget.RelativeLayout;
        import android.widget.TextView;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.io.UnsupportedEncodingException;
        import java.net.MalformedURLException;
        import java.net.URL;
        import java.net.URLConnection;
        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.Formatter;
        import java.util.List;
        import java.util.Objects;
        import java.util.Random;
        import java.util.StringTokenizer;

        import javax.crypto.Mac;
        import javax.crypto.SecretKey;
        import javax.crypto.spec.SecretKeySpec;

public class FatSecretGet extends Fragment {
    /**
     * FatSecret Authentication
     * http://platform.fatsecret.com/api/default.aspx?screen=rapiauth
     * Reference
     * https://github.com/ethan-james/cookbox/blob/master/src/com/vitaminc4/cookbox/FatSecret.java
     */

    private String food_id = "";
    private String food_name = "";
    private ArrayList<ArrayList> numberServings = new ArrayList<>();
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
    ArrayList<String> foodNames = new ArrayList<>();
    final static private String APP_METHOD = "GET";
    final static private String APP_KEY = "37ae4f7d04d74372a438465b3904081b";
    static private String APP_SECRET = "de528af7c1eb4ae8a9e91e6e4af4215c";
    final static private String APP_URL = "http://platform.fatsecret.com/rest/server.api";
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    Context context;
    JSONObject food = null;
    Ditalits mCallback;

    public interface Ditalits {
        void getDetails(ArrayList arrayList, String adsd, String asddff);

        void addedItem(ArrayList list, String foodsn, String foodId);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (Ditalits) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnHeadlineSelectedListener");
        }
    }

    public FatSecretGet(long ab, Context context) {
        this.context = context;
        List<String> params = new ArrayList<>(Arrays.asList(generateOauthParams()));
        String[] template = new String[1];
        params.add("method=food.get");
        params.add("food_id=" + ab);
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

    private static String[] generateOauthParams() {
        return new String[]{
                "oauth_consumer_key=" + APP_KEY,
                "oauth_signature_method=HMAC-SHA1",
                "oauth_timestamp=" +
                        Long.valueOf(System.currentTimeMillis() * 2).toString(),
                "oauth_nonce=" + nonce(),
                "oauth_version=1.0",
                "format=json"};
    }


    private static String sign(String method, String uri, String[] params) {
        String[] p = {method, Uri.encode(uri), Uri.encode(paramify(params))};
        String s = join(p, "&");
        APP_SECRET += "&";
        SecretKey sk = new SecretKeySpec(APP_SECRET.getBytes(), HMAC_SHA1_ALGORITHM);
        APP_SECRET = APP_SECRET.substring(0, APP_SECRET.length() - 1);
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


    public class FlightThread extends AsyncTask<String, Void, Void> {

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
                food = foodGet.getJSONObject("food");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                food_id = food.getString("food_id");
                food_name = food.getString("food_name");
                JSONObject jsonObject = food.getJSONObject("servings").getJSONObject("serving");
                for (int x = 0; x < jsonObject.length(); x++) {
                    ArrayList<String> items = new ArrayList();

                    //try each one, if not there, input NA\
                    String unit = "";
                    String unitD = "";
                    try {
                        unit = jsonObject.getString("metric_serving_unit");
                        unitD = "m" + unit; //1
                    } catch (JSONException e) {
                        unit = "g";
                        unitD = "mg";
                    }
                    ;

                    try {
                        items.add(jsonObject.getString("calcium") ); //1
                    } catch (JSONException e) {
                        items.add("Not Avalible");
                    }
                    ;
                    try {
                        items.add(jsonObject.getString("calories") ); //02
                    } catch (JSONException e) {
                        items.add("Not Avalible");
                    }
                    ;
                    try {
                        items.add(jsonObject.getString("carbohydrates") ); //03
                    } catch (JSONException e) {
                        items.add("Not Avalible");
                    }
                    ;
                    try {
                        items.add(jsonObject.getString("cholesterol") ); //04
                    } catch (JSONException e) {
                        items.add("Not Avalible");
                    }
                    ;
                    try {
                        items.add(jsonObject.getString("fat") ); //05
                    } catch (JSONException e) {
                        items.add("Not Avalible");
                    }
                    ;
                    try {
                        items.add(jsonObject.getString("fiber") ); //06
                    } catch (JSONException e) {
                        items.add("Not Avalible");
                    }
                    ;
                    try {
                        items.add(jsonObject.getString("iron")); //07
                    } catch (JSONException e) {
                        items.add("Not Avalible");
                    }
                    ;
                    try {
                        items.add(jsonObject.getString("metric_serving_amount")); //08
                    } catch (JSONException e) {
                        items.add("Not Avalible");
                    }
                    ;
                    try {
                        items.add(jsonObject.getString("protien") ); //09
                    } catch (JSONException e) {
                        items.add("Not Avalible");
                    }
                    ;
                    try {
                        items.add(jsonObject.getString("saturated_fat") ); //10
                    } catch (JSONException e) {
                        items.add("Not Avalible");
                    }
                    ;
                    try {
                        String str = jsonObject.getString("serving_description");
                        String[] part = str.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                        for (int startZ=0;startZ<part.length;startZ++) {
                        }
                        items.add(part[0]); //11
                    } catch (JSONException e) {
                        items.add("Not Avalible");
                    }
                    ;
                    try {
                        items.add(jsonObject.getString("sodium") ); //12
                    } catch (JSONException e) {
                        items.add("Not Avalible");
                    }
                    ;
                    try {
                        items.add(jsonObject.getString("sugar") ); //13
                    } catch (JSONException e) {
                        items.add("Not Avalible");
                    }
                    ;
                    try {
                        items.add(jsonObject.getString("vitamin_a") ); //14
                    } catch (JSONException e) {
                        items.add("Not Avalible");
                    }
                    ;
                    try {
                        items.add(jsonObject.getString("vitamin_c") ); //15
                    } catch (JSONException e) {
                        items.add("Not Avalible");
                    };
                    try {
                        String str = jsonObject.getString("serving_description");
                        String[] part = str.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                        for (int startZ=0;startZ<part.length;startZ++) {
                        }
                        items.add(part[1]); //11
                    } catch (JSONException e) {
                        items.add("Not Avalible");
                    };
                    numberServings.add(items);
                }
            } catch (JSONException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            mCallback.getDetails(numberServings.get(0), food_name, food_id);

        }
    }
}