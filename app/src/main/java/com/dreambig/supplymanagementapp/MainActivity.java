package com.dreambig.supplymanagementapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.dreambig.supplymanagementapp.Views.AuthFragments.AuthViewModel;
import com.dreambig.supplymanagementapp.Models.CheckAccountModel;
import com.dreambig.supplymanagementapp.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private final static String WEB_CLIENT_ID = "293170300165-f34ju1aua766261n7ut5o04g1jh0fq89.apps.googleusercontent.com";
    private ActivityMainBinding binding;
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private NavHostFragment navHostFragment;
    private NavController navController;
    private AuthViewModel mViewModel;
    private GoogleAuthCallBack googleAuthCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        //Initialize nav components
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(binding.fragmentContainerView.getId());
        navController = navHostFragment.getNavController();


        //Initialize google sign in
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(WEB_CLIENT_ID)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Check the previous signed in
        checkPreviousSignIn();

        //Observe sign out
        observeSignOut();
    }

    private void observeSignOut() {
        mViewModel.getIsSignedOut().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSignedOut) {
                if(isSignedOut)
                    navController.navigate(R.id.signUpFragment);
            }
        });
    }

    public void signOut(){
        mViewModel.signOut();
    }

    public void showOneTapUI(){
        //Pops out the google sign intent
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            googleAuthCallBack.result(account);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    public void googleSignOut(){
        mGoogleSignInClient.signOut();
    }

    public void popBackStack(){
        navController.popBackStack();
    }

    public void checkPreviousSignIn(){
        //custom account
        mViewModel.getIsSignedIn().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSignedIn) {
                if(isSignedIn)
                    navController.setGraph(R.navigation.main_navigation);
            }
        });

        //google account
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            mViewModel.getmCheckAccount(account.getEmail()).observe(this, new Observer<CheckAccountModel>() {
                @Override
                public void onChanged(CheckAccountModel checkAccountModel) {
                    Log.d("MY_DEV","check triggered");
                    if(checkAccountModel.getExist() && checkAccountModel.getGmail()){
                        navController.setGraph(R.navigation.main_navigation);
                    }
                    else{
                        navController.setGraph(R.navigation.authentication_navigation);
                    }
                }
            });
        }
        else{
            navController.setGraph(R.navigation.authentication_navigation);
        }
    }

    public void setGoogleAuthCallBack(GoogleAuthCallBack googleAuthCallBack) {
        this.googleAuthCallBack = googleAuthCallBack;
    }

    public interface GoogleAuthCallBack{
        void result(GoogleSignInAccount account);
    }
}