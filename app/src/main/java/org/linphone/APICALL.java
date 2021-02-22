package org.linphone;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.linphone.core.tools.Log;

public class APICALL {
    private final String API_URL_BASE;

    public APICALL() {
        this.API_URL_BASE = "https://2ao1n0pyyg.execute-api.ca-central-1.amazonaws.com/api/";
    }

    public String postJson(String number) {
        JSONObject jObjectType = null;
        try {
            jObjectType = new JSONObject();
            // put elements into the object as a key-value pair
            jObjectType.put("phone_number", number);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final MediaType mediaType = MediaType.parse("application/json");
        OkHttpClient httpClient = new OkHttpClient();
        String url = API_URL_BASE + "verify";
        String jsonStr = jObjectType.toString() + "";
        Request request =
                new Request.Builder().url(url).post(RequestBody.create(mediaType, jsonStr)).build();
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.e("verify", "Got response from server for JSON post using OkHttp ");
                return response.body().string();
            }
        } catch (IOException e) {
            Log.e("verify", "error in getting response for json post request okhttp");
        }
        return null;
    }

    public String verifyCode(String number, String code) {
        JSONObject jObjectType = null;
        try {
            jObjectType = new JSONObject();
            // put elements into the object as a key-value pair
            jObjectType.put("phone_number", number);
            jObjectType.put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final MediaType mediaType = MediaType.parse("application/json");
        OkHttpClient httpClient = new OkHttpClient();
        String url = API_URL_BASE + "checkToken";
        // String url = "http://159.89.127.205/api/checkToken";
        String jsonStr = jObjectType.toString() + "";
        Request request =
                new Request.Builder().url(url).post(RequestBody.create(mediaType, jsonStr)).build();
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.e("verify", "Got response from server for JSON post using OkHttp ");
                return response.body().string();
            } else {

                return response.body().string();
            }
        } catch (IOException e) {
            Log.e("verify", "error in getting response for json post request okhttp");
        }
        return null;
    }

    //    public String AuthLogin(String username, String Password) {
    //        JSONObject jObjectType = null;
    //        try {
    //            jObjectType = new JSONObject();
    //            // put elements into the object as a key-value pair
    //            jObjectType.put("username", username);
    //            jObjectType.put("password", Password);
    //        } catch (JSONException e) {
    //            e.printStackTrace();
    //        }
    //
    //        final MediaType mediaType = MediaType.parse("application/json");
    //        OkHttpClient httpClient = new OkHttpClient();
    //        String url = API_URL_BASE + "login";
    //        // String url = "http://159.89.127.205/api/checkToken";
    //        String jsonStr = jObjectType.toString() + "";
    //        Request request =
    //                new Request.Builder().url(url).post(RequestBody.create(mediaType,
    // jsonStr)).build();
    //        Response response = null;
    //        try {
    //            response = httpClient.newCall(request).execute();
    //            if (response.isSuccessful()) {
    //                Log.e("verify", "Got response from server for JSON post using OkHttp ");
    //                return response.body().string();
    //            }
    //        } catch (IOException e) {
    //            Log.e("verify", "error in getting response for json post request okhttp");
    //        }
    //        return null;
    //    }

    public String getProfile(String token) {
        OkHttpClient httpClient = new OkHttpClient();
        String url = API_URL_BASE + "my-user";
        Request request =
                new Request.Builder().header("Authorization", "Bearer " + token).url(url).build();

        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            Log.e("okhttp", "error in getting response get request okhttp");
        }
        return null;
    }

    public String getHttpBalanceResponse(String token) {
        OkHttpClient httpClient = new OkHttpClient();
        String url = API_URL_BASE + "balance";
        Request request =
                new Request.Builder().header("Authorization", "Bearer " + token).url(url).build();

        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            Log.e("okhttp", "error in getting response get request okhttp");
        }
        return null;
    }

    public String getCountriesResponse(String token) {
        OkHttpClient httpClient = new OkHttpClient();
        String url = API_URL_BASE + "countries_list";
        Request request =
                new Request.Builder().header("Authorization", "Bearer " + token).url(url).build();

        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            Log.e("okhttp", "error in getting response get request okhttp");
        }
        return null;
    }

    public String getForawardCountriesResponse(String token, String number) {
        OkHttpClient httpClient = new OkHttpClient();
        String url = API_URL_BASE + "number-rate?number=" + number;
        Request request =
                new Request.Builder().header("Authorization", "Bearer " + token).url(url).build();

        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            Log.e("okhttp", "error in getting response get request okhttp");
        }
        return null;
    }

    //    public String getCountryPricing(String conutrycode, String token) {
    //        JSONObject jObjectType = null;
    //        try {
    //            jObjectType = new JSONObject();
    //            // put elements into the object as a key-value pair
    //            jObjectType.put("countryCode", conutrycode);
    //
    //        } catch (JSONException e) {
    //            e.printStackTrace();
    //        }
    //
    //        final MediaType mediaType = MediaType.parse("application/json");
    //        OkHttpClient httpClient = new OkHttpClient();
    //        String url = API_URL_BASE + "pricing";
    //        String jsonStr = jObjectType.toString() + "";
    //        Request request =
    //                new Request.Builder()
    //                        .header("Authorization", "Bearer " + token)
    //                        .url(url)
    //                        .post(RequestBody.create(mediaType, jsonStr))
    //                        .build();
    //        Response response = null;
    //        try {
    //            response = httpClient.newCall(request).execute();
    //            if (response.isSuccessful()) {
    //                Log.e("verify", "Got response from server for JSON post using OkHttp ");
    //                return response.body().string();
    //            }
    //        } catch (IOException e) {
    //            Log.e("verify", "error in getting response for json post request okhttp");
    //        }
    //        return null;
    //    }

    public String getCountryPricing(String conutrycode, String token) {
        OkHttpClient httpClient = new OkHttpClient();
        String url = API_URL_BASE + "pricing?countryCode=" + conutrycode;
        Request request =
                new Request.Builder().header("Authorization", "Bearer " + token).url(url).build();

        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            Log.e("okhttp", "error in getting response get request okhttp");
        }
        return null;
    }

    public String getAvailableNumber(JSONObject jObjectType, String Token) {
        final MediaType mediaType = MediaType.parse("application/json");
        OkHttpClient httpClient = new OkHttpClient();
        String url = API_URL_BASE + "availablePhoneNumbers";
        String jsonStr = jObjectType.toString() + "";
        Request request =
                new Request.Builder()
                        .header("Authorization", "Bearer " + Token)
                        .url(url)
                        .post(RequestBody.create(mediaType, jsonStr))
                        .build();
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.e("verify", "Got response from server for JSON post using OkHttp ");
                return response.body().string();
            } else {
                return response.body().string();
            }
        } catch (IOException e) {
            Log.e("verify", "error in getting response for json post request okhttp");
        }
        return null;
    }

    public String NumberCreate(JSONObject jObjectType, String Token) {
        final MediaType mediaType = MediaType.parse("application/json");
        OkHttpClient httpClient = new OkHttpClient();
        String url = API_URL_BASE + "number";
        String jsonStr = jObjectType.toString() + "";
        Request request =
                new Request.Builder()
                        .header("Authorization", "Bearer " + Token)
                        .url(url)
                        .post(RequestBody.create(mediaType, jsonStr))
                        .build();
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.e("verify", "Got response from server for JSON post using OkHttp ");
                return response.body().string();
            } else {

                return response.body().string();
            }
        } catch (IOException e) {
            Log.e("verify", "error in getting response for json post request okhttp");
        }
        return null;
    }

    public String getMyNumbers(String token) {
        OkHttpClient httpClient = new OkHttpClient();
        String url = API_URL_BASE + "number";
        Request request =
                new Request.Builder().header("Authorization", "Bearer " + token).url(url).build();

        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            Log.e("okhttp", "error in getting response get request okhttp");
        }
        return null;
    }

    public String UpdateFriendlyName(String id, JSONObject jObjectType, String Token) {
        final MediaType mediaType = MediaType.parse("application/json");
        OkHttpClient httpClient = new OkHttpClient();
        String url = API_URL_BASE + "number/" + id;
        String jsonStr = jObjectType.toString() + "";
        Request request =
                new Request.Builder()
                        .header("Authorization", "Bearer " + Token)
                        .url(url)
                        .patch(RequestBody.create(mediaType, jsonStr))
                        .build();
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.e("verify", "Got response from server for JSON post using OkHttp ");
                return response.body().string();
            } else {
                return response.body().string();
            }
        } catch (IOException e) {
            Log.e("verify", "error in getting response for json post request okhttp");
        }
        return null;
    }

    public String getEndPoint(String token) {
        OkHttpClient httpClient = new OkHttpClient();
        String url = API_URL_BASE + "endpoint";
        Request request =
                new Request.Builder().header("Authorization", "Bearer " + token).url(url).build();
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            Log.e("okhttp", "error in getting response get request okhttp");
        }
        return null;
    }

    public String Send_Stripe_Payment(JSONObject jObjectType, String Token) {
        final MediaType mediaType = MediaType.parse("application/json");
        OkHttpClient httpClient = new OkHttpClient();
        String url = API_URL_BASE + "paymentProcess";
        String jsonStr = jObjectType.toString() + "";
        Request request =
                new Request.Builder()
                        .header("Authorization", "Bearer " + Token)
                        .url(url)
                        .post(RequestBody.create(mediaType, jsonStr))
                        .build();
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.e("verify", "Got response from server for JSON post using OkHttp ");
                return response.body().string();
            } else {
                return response.body().string();
            }
        } catch (IOException e) {
            Log.e("verify", "error in getting response for json post request okhttp");
        }
        return null;
    }

    public String Check_Contacts(JSONObject jObjectType, String Token) {
        final MediaType mediaType = MediaType.parse("application/json");
        OkHttpClient httpClient = new OkHttpClient();
        String url = API_URL_BASE + "/checkContacts";
        String jsonStr = jObjectType.toString() + "";
        Request request =
                new Request.Builder()
                        .header("Authorization", "Bearer " + Token)
                        .url(url)
                        .post(RequestBody.create(mediaType, jsonStr))
                        .build();
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.e("verify", "Got response from server for JSON post using OkHttp ");
                return response.body().string();
            } else {
                return response.body().string();
            }
        } catch (IOException e) {
            Log.e("verify", "error in getting response for json post request okhttp");
        }
        return null;
    }

    public String getCallerIds(String token) {
        OkHttpClient httpClient = new OkHttpClient();
        String url = API_URL_BASE + "getCallerID";
        Request request =
                new Request.Builder().header("Authorization", "Bearer " + token).url(url).build();
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                return response.body().string();
            }
        } catch (IOException e) {
            Log.e("okhttp", "error in getting response get request okhttp");
        }
        return null;
    }

    public String SetCallerID(JSONObject jObjectType, String Token) {
        final MediaType mediaType = MediaType.parse("application/json");
        OkHttpClient httpClient = new OkHttpClient();
        String url = API_URL_BASE + "setCallerID";
        String jsonStr = jObjectType.toString() + "";
        Request request =
                new Request.Builder()
                        .header("Authorization", "Bearer " + Token)
                        .url(url)
                        .patch(RequestBody.create(mediaType, jsonStr))
                        .build();
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.e("verify", "Got response from server for JSON post using OkHttp ");
                return response.body().string();
            } else {
                return response.body().string();
            }
        } catch (IOException e) {
            Log.e("verify", "error in getting response for json post request okhttp");
        }
        return null;
    }

    public String DeleteVoiceMail(String id, String Token) {
        //        final MediaType mediaType = MediaType.parse("application/json");
        OkHttpClient httpClient = new OkHttpClient();
        String url = API_URL_BASE + "destroy_message/" + id;
        //        String jsonStr = jObjectType.toString() + "";
        Request request =
                new Request.Builder()
                        .header("Authorization", "Bearer " + Token)
                        .url(url)
                        .delete()
                        //                        .delete(RequestBody.create(mediaType, jsonStr))
                        //                        .patch(RequestBody.create(mediaType, jsonStr))
                        .build();
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.e("verify", "Got response from server for JSON post using OkHttp ");
                return response.body().string();
            } else {
                return response.body().string();
            }
        } catch (IOException e) {
            Log.e("verify", "error in getting response for json post request okhttp");
        }
        return null;
    }

    public String DeleteNumber(String id, String Token) {
        //        final MediaType mediaType = MediaType.parse("application/json");
        OkHttpClient httpClient = new OkHttpClient();
        String url = API_URL_BASE + "number/" + id;
        //        String jsonStr = jObjectType.toString() + "";
        Request request =
                new Request.Builder()
                        .header("Authorization", "Bearer " + Token)
                        .url(url)
                        .delete()
                        //                        .delete(RequestBody.create(mediaType, jsonStr))
                        //                        .patch(RequestBody.create(mediaType, jsonStr))
                        .build();
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.e("verify", "Got response from server for JSON post using OkHttp ");
                return response.body().string();
            } else {
                return response.body().string();
            }
        } catch (IOException e) {
            Log.e("verify", "error in getting response for json post request okhttp");
        }
        return null;
    }

    public String UpdateProfile(JSONObject jObjectType, String Token) {
        final MediaType mediaType = MediaType.parse("application/json");
        OkHttpClient httpClient = new OkHttpClient();
        String url = API_URL_BASE + "update-me";
        String jsonStr = jObjectType.toString() + "";
        Request request =
                new Request.Builder()
                        .header("Authorization", "Bearer " + Token)
                        .url(url)
                        .patch(RequestBody.create(mediaType, jsonStr))
                        .build();
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.e("verify", "Got response from server for JSON post using OkHttp ");
                return response.body().string();
            }
        } catch (IOException e) {
            Log.e("verify", "error in getting response for json post request okhttp");
        }
        return null;
    }
}
