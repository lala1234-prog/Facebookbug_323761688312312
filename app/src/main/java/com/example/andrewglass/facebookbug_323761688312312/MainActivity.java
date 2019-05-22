package com.example.andrewglass.facebookbug_323761688312312;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.model.GameRequestContent;
import com.facebook.share.widget.GameRequestDialog;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CallbackManager callbackManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button loginButton = findViewById(R.id.Login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginToFacebook();
            }
        });

        final Button logout = findViewById(R.id.Logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutOfFacebook();
            }
        });

        final Button sendInvite = findViewById(R.id.SendGameRequest);
        sendInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGameRequestDialog();
            }
        });

        AccessToken currentToken = AccessToken.getCurrentAccessToken();
        boolean activeAccessToken = currentToken != null && !currentToken.isExpired();

        enableButtons(activeAccessToken);

        callbackManager = CallbackManager.Factory.create();

        LoginManager loginManager = LoginManager.getInstance();
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                enableButtons(true);
            }

            @Override
            public void onCancel() {

                enableButtons(false);
            }

            @Override
            public void onError(FacebookException error) {
                enableButtons(false);
            }
        });
    }

    @Override
    public void onActivityResult(int rc, int resc, Intent intent)
    {
        callbackManager.onActivityResult(rc, resc, intent);
    }

    private void enableButtons(boolean isLoggedIn)
    {
        Button loginButton = findViewById(R.id.Login);
        Button logout = findViewById(R.id.Logout);
        Button sendInvite = findViewById(R.id.SendGameRequest);

        loginButton.setEnabled(!isLoggedIn);
        logout.setEnabled(isLoggedIn);
        sendInvite.setEnabled(isLoggedIn);
    }


    private void loginToFacebook()
    {
        LoginManager loginManager = LoginManager.getInstance();

        List<String> permissionsList = new LinkedList<>();

        loginManager.logInWithReadPermissions(this, permissionsList);
    }

    private void logOutOfFacebook()
    {
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.logOut();

        enableButtons(false);
    }

    private void openGameRequestDialog()
    {
        GameRequestContent content = new GameRequestContent.Builder()
                .setMessage("Come play this level with me")
                .build();

        GameRequestDialog dialog = new GameRequestDialog(this);
        dialog.show(content);
    }
}
