package com.thirdparty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.unity3d.player.UnityPlayer;
//import com.google.gson.Gson;

import java.net.URLDecoder;

public class GoogleLoginActivity extends Activity{
    private static final String TAG = "MyGoogleLogin";
    private static final int RC_SIGN_IN = 100;

    static GoogleSignInClient googleSignInClient;
    static Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate");
        super.onCreate(savedInstanceState);

        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        task.addOnCompleteListener(new OnCompleteListener<GoogleSignInAccount>() {
            @Override
            public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                finish();
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    onLoginComplete(account);

                } catch (ApiException e) {
                    if(e.getStatusCode()==12501){
                        ReturnData returnData = new ReturnData();
                        returnData.Cancel=true;
                        onPostMessage(returnData);
                        return;
                    }
                    Log.e(TAG, "signInResult:failed code=" + e.getMessage());
                    ReturnData returnData = new ReturnData();
                    returnData.Error = e.getMessage();
                    onPostMessage(returnData);
                }catch (Exception e){
                    Log.e(TAG, "signInResult:failed " + e.getMessage());
                    ReturnData returnData = new ReturnData();
                    returnData.Error = e.getMessage();
                    onPostMessage(returnData);
                }

            }
        });

    }

    public static void create(Activity activity, String key){

        context = activity;

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(key)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(activity, gso);
    }

    public static void release(){
        context = null;
        googleSignInClient =null;
    }

    public static void signOut(){
        GoogleSignInAccount account = getLastSignedInAccount();
        if(account!=null){
            googleSignInClient.signOut();
            Log.i(TAG,"signOut"+account.getDisplayName());
        }
    }
    static class ReturnData {
        public String DisplayName;
        public String FamilyName;
        public String GivenName;
        public String Email;
        public String Id;
        public String IdToken;
        public String ServerAuthCode;
        public String PhotoUrl;
        public String Error;
        public Boolean Cancel;
    }
    static void onLoginComplete(GoogleSignInAccount account){
        ReturnData userData = new ReturnData();
        userData.DisplayName = account.getDisplayName();
        userData.FamilyName = account.getFamilyName();
        userData.GivenName = account.getGivenName();
        userData.Email = account.getEmail();
        userData.Id = account.getId();
        userData.IdToken = account.getIdToken();
        userData.ServerAuthCode = account.getServerAuthCode();

        try {
            String photoUrl = account.getPhotoUrl().toString();
            userData.PhotoUrl = URLDecoder.decode(photoUrl, "UTF-8");
        }catch (Exception ex){
            userData.PhotoUrl=account.getPhotoUrl().toString();
            Log.w(TAG,"URLDecoder.decode:"+ex.getMessage());
        }

        Log.d(TAG, "===========Signed in successfully=============");
        onPostMessage(userData);
    }

    static void onPostMessage(ReturnData returnData){
        Gson gson = new Gson();
        String json = gson.toJson(returnData);
        Log.d(TAG, "onPostMessage:"+json);
        UnityPlayer.UnitySendMessage("LuaRuntime","OnGoogleLoginResult",json);
    }

    public static void signIn() {
        GoogleSignInAccount account = getLastSignedInAccount();
        if(account!=null){
            onLoginComplete(account);
            return;
        }
        Intent intent = new Intent(context, GoogleLoginActivity.class);
        context.startActivity(intent);
    }

    public static GoogleSignInAccount getLastSignedInAccount(){
        return GoogleSignIn.getLastSignedInAccount(context);
    }
}
