package com.clickawaybuying.shopify;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.clickawaybuying.shopify.utils.PreferenceUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class BackgroudWorker extends AsyncTask<String, String, String> {
    private String user_name,pass_word,result,regResult,res,regRes,uname,pass,username,email;
    Context context;
    BackgroudWorker(Context ctx) {
        this.context = ctx;
    }



    @Override
    protected String doInBackground(String... strings) {
        String type = strings[0];

        String loginURL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/andphplogin.php";
        String regURL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/andphpreg.php";
        String fbLoginURL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/andphpFbLogin.php";

        if (type.equals("reg")) {
            username = strings[1];
            String email = strings[2];
            String password = strings[3];
            try {
                URL url = new URL(regURL);
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    OutputStreamWriter outputStreamWritter = new OutputStreamWriter(outputStream, "UTF-8");
                    BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWritter);
                    String insert_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")
                            + "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8")
                            + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                    bufferedWriter.write(insert_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "iso-8859-1");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    result = "";
                    String line = "";
                    StringBuilder stringBuilder = new StringBuilder();

                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");

                    }
                    result = stringBuilder.toString();
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }else if (type.equals("fbLogin")) {
            username = strings[1];
            email = strings[2];
            String image = strings[3];
            try {
                URL url = new URL(fbLoginURL);
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    OutputStreamWriter outputStreamWritter = new OutputStreamWriter(outputStream, "UTF-8");
                    BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWritter);
                    String insert_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")
                            + "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8")
                            + "&" + URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(image, "UTF-8");
                    bufferedWriter.write(insert_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "iso-8859-1");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    result = "";
                    String line = "";
                    StringBuilder stringBuilder = new StringBuilder();

                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");

                    }
                    result = stringBuilder.toString();
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }else if(type.equals("login")){
            user_name = strings[1];
            pass_word = strings[2];
            try {
                URL url = new URL(loginURL);
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    OutputStreamWriter outputStreamWritter = new OutputStreamWriter(outputStream, "UTF-8");
                    BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWritter);
                    String login_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8")
                            + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(pass_word, "UTF-8");
                    bufferedWriter.write(login_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "iso-8859-1");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    result = "";
                    String line = "";
                    StringBuilder stringBuilder = new StringBuilder();

                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");

                    }
                    result = stringBuilder.toString();
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        if (result == null){
            Toast.makeText(context,"An error occurred, please check your internet connection.",Toast.LENGTH_LONG).show();
            result = "empty";
        }
        res = result.trim();
        //Toast.makeText(LoginPage.la, ""+result, Toast.LENGTH_SHORT).show();

        if (res.equals("fbLoginSuccess") || res.equals("fbRegsuccess")){
            Toast.makeText(context, "Login Success, Welcome "+username, Toast.LENGTH_LONG).show();
            String title = "Welcome to Shopify CAB "+username;
            //push notification
            String message = "Enjoy safe shopping with thousands of good quality products.";
            insertNotif(message,title);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    context.getApplicationContext()
            )
                    .setSmallIcon(R.drawable.logoo)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true);

            Intent intent = new Intent(context.getApplicationContext(),HomeScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("message",message);

            PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager)context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0,builder.build());
            Intent myactivity = new Intent(context.getApplicationContext(), HomeScreen.class);
            myactivity.addFlags(myactivity.FLAG_ACTIVITY_NEW_TASK);
            //get account info method
            getFBUserInfo();
            //go to Homescreen
            context.getApplicationContext().startActivity(myactivity);
            //finish activity in Loginpage.class
            LoginPage.la.finish();


        }
        if (res.equals("fbLoginfailed") || res.equals("fbRegfailed")){
            Toast.makeText(LoginPage.la, "Login failed, please try again later", Toast.LENGTH_SHORT).show();
        }
        if (res.equals("userexist")){
            SignUp.JName.setError("Username already exists");
            SignUp.JName.requestFocus();
            return;
        }
        if (res.equals("regsuccess")){
            String username = SignUp.JName.getText().toString();
            String password = SignUp.JPassword.getText().toString();
            String email = SignUp.JEmail.getText().toString();

            Toast.makeText(context,"Signed up successfully",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(context.getApplicationContext(),LoginPage.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("username",username);
            i.putExtra("password",password);
            insertToFirebase(email,password,i);



        }
        if (res.equals("Success")){
            Toast.makeText(context, "Login Success, Welcome "+user_name, Toast.LENGTH_LONG).show();
            String title = "Welcome to Shopify CAB "+user_name;
            //push notification
            String message = "Enjoy safe shopping with thousands of good quality products.";
            insertNotif(message,title);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    context.getApplicationContext()
            )
                    .setSmallIcon(R.drawable.logoo)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true);

            Intent intent = new Intent(context.getApplicationContext(),HomeScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("message",message);

            PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager)context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0,builder.build());
            Intent myactivity = new Intent(context.getApplicationContext(), HomeScreen.class);
            myactivity.addFlags(myactivity.FLAG_ACTIVITY_NEW_TASK);
            //get account info method
            getUserInfo();
            //go to Homescreen
            context.getApplicationContext().startActivity(myactivity);
            //finish activity in Loginpage.class
            LoginPage.la.finish();



        }
        if (!res.equals("regsuccess")) {

        }
        if (res.equals("loginfailed")) {
            Toast.makeText(context, "Login failed incorrect username or password.", Toast.LENGTH_LONG).show();
        }


            //super.onPostExecute(s);
    }
    public void getUserInfo(){
        String showURL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/getAccountInfo.php";
        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                showURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//gets the php json responses
                try {
                    JSONObject jsonObject= new JSONObject(response.toString());
                    JSONArray address = jsonObject.getJSONArray("accounts");
                    for (int i=0;i<address.length();i++){
                        JSONObject addresses = address.getJSONObject(i);

                        String id = addresses.getString("id");
                        String username = addresses.getString("username");
                        String email = addresses.getString("email");

                        //save to shared preference for login sessions
                        PreferenceUtils.saveEmail(username, context.getApplicationContext());
                        PreferenceUtils.saveUsername(email, context.getApplicationContext());
                        PreferenceUtils.saveID(id, context.getApplicationContext());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {//gets php error messages
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file
                parameters.put("username",user_name);

                return parameters;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
    public void getFBUserInfo2(){
        String showURL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/getFBAccInfo.php";
        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                showURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//gets the php json responses
                try {
                    JSONObject jsonObject= new JSONObject(response.toString());
                    JSONArray address = jsonObject.getJSONArray("product");
                    for (int i=0;i<address.length();i++){
                        JSONObject addresses = address.getJSONObject(i);

                        String id = addresses.getString("id");
                        String username = addresses.getString("username");
                        String email = addresses.getString("email");

                        //save to shared preference for login sessions
                        PreferenceUtils.saveEmail(username, context.getApplicationContext());
                        PreferenceUtils.saveUsername(email, context.getApplicationContext());
                        PreferenceUtils.saveID(id, context.getApplicationContext());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {//gets php error messages
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file
                parameters.put("username",username);

                return parameters;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
    public void insertNotif(final String message, final String title){
        final String user_name  = PreferenceUtils.getEmail(context.getApplicationContext());
        String insertURL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/insertNotif.php";
        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, insertURL, new Response.Listener<String>() {//canned request for retrieving the response body at a given URL as a String
            @Override
            public void onResponse(String response) {//gets the php json responses

            }
        }, new Response.ErrorListener() {//gets php error messages
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file

                parameters.put("message",message);
                parameters.put("title",title);
                parameters.put("username",LoginPage.JUsername.getText().toString());


                return parameters;
            }
        };
        requestQueue.add(request);
    }
    private void insertToFirebase(String email,String pwd,final Intent i) {
        SignUp.mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(SignUp.sa, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(context,"User already exist.",Toast.LENGTH_SHORT).show();
                }
                else {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.getCurrentUser().sendEmailVerification()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(context, "Email verification has been sent to your email", Toast.LENGTH_SHORT).show();
                                        context.getApplicationContext().startActivity(i);
                                    }
                                    else{
                                        Toast.makeText(context, "Failed to send email verification, Please try again later", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    public void getFBUserInfo(){
        String showURL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/getAccountInfo.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this.context.getApplicationContext());
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                showURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//gets the php json responses
                try {
                    JSONObject jsonObject= new JSONObject(response.toString());
                    JSONArray address = jsonObject.getJSONArray("accounts");
                    for (int i=0;i<address.length();i++){
                        JSONObject addresses = address.getJSONObject(i);

                        String id = addresses.getString("id");
                        String username = addresses.getString("username");
                        String email = addresses.getString("email");

                        //save to shared preference for login sessions
                        PreferenceUtils.saveEmail(username, context.getApplicationContext());
                        PreferenceUtils.saveUsername(email, context.getApplicationContext());
                        PreferenceUtils.saveID(id, context.getApplicationContext());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {//gets php error messages
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file
                parameters.put("username",username);

                return parameters;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

}
