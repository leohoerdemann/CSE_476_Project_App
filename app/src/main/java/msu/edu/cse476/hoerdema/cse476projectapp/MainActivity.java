package msu.edu.cse476.hoerdema.cse476projectapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;

import com.auth0.android.jwt.JWT;

import net.openid.appauth.AppAuthConfiguration;
import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;
import net.openid.appauth.browser.BrowserAllowList;
import net.openid.appauth.browser.VersionedBrowserMatcher;

import org.json.JSONException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class MainActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> authorizationLauncher;
    private AuthState authState;
    private JWT jwt = null;
    private AuthorizationService authorizationService;
    private AuthorizationServiceConfiguration authServiceConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authorizationLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
//                            System.out.println("here");
                        }
                    }
                });


    }
    public void restoreState() {
        String jsonString = ((Application) getApplication())
                .getSharedPreferences("AUTH_STATE_PREFERENCE", Context.MODE_PRIVATE)
                .getString("AUTH_STATE", null);
        if (jsonString != null && !TextUtils.isEmpty(jsonString)) {
            try {
                authState = AuthState.jsonDeserialize(jsonString);
                if (!TextUtils.isEmpty(authState.getIdToken())) {
                    jwt = new JWT(authState.getIdToken());
                }
            } catch (JSONException jsonException) {

            }
        }
    }

    public void persistState() {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("AUTH_STATE_PREFERENCE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("AUTH_STATE", authState.jsonSerializeString());
        editor.apply();
    }

    private void initAuthServiceConfig() {
        authServiceConfig = new AuthorizationServiceConfiguration(
                Uri.parse("https://accounts.google.com/o/oauth2/v2/auth"),
                Uri.parse("https://www.googleapis.com/oauth2/v4/token"),
                null,
                Uri.parse("https://accounts.google.com/o/oauth2/revoke?token="));
    }

    private void initAuthService() {
        AppAuthConfiguration appAuthConfiguration = new AppAuthConfiguration.Builder()
                .setBrowserMatcher(
                        new BrowserAllowList(
                                VersionedBrowserMatcher.CHROME_CUSTOM_TAB,
                                VersionedBrowserMatcher.SAMSUNG_CUSTOM_TAB
                        )
                ).build();
        authorizationService = new AuthorizationService(
                getApplication(),
                appAuthConfiguration);
    }

    public void onSignIn(View view){
        attemptAuthorization();
    }

    public void attemptAuthorization() {
        initAuthServiceConfig();
        initAuthService();
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[64];
        secureRandom.nextBytes(bytes);
        int encoding = Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP;
        String codeVerifier = Base64.encodeToString(bytes, encoding);

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] hash = digest.digest(codeVerifier.getBytes());
        String codeChallenge = Base64.encodeToString(hash, encoding);


        AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(
                authServiceConfig,
                String.valueOf(R.string.client_id),
                ResponseTypeValues.CODE,
                Uri.parse(String.valueOf(R.string.url_auth_redirect)))
                .setCodeVerifier(codeVerifier, codeChallenge, "S256");
        builder.setScopes("profile",
                "email",
                "openid",
                "https://www.googleapis.com/auth/drive");
        AuthorizationRequest request = builder.build();
        Intent authIntent = authorizationService.getAuthorizationRequestIntent(request);

        authorizationLauncher.launch(authIntent);



    }



    public void onStartLeaderBoard(View view) {
        Intent intent = new Intent(this, LeaderBoardActivity.class);
        startActivity(intent);
    }
}