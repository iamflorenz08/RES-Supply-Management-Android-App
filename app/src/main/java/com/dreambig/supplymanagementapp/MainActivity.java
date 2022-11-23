package com.dreambig.supplymanagementapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.dreambig.supplymanagementapp.Models.AuthResponseModel;
import com.dreambig.supplymanagementapp.Views.AuthFragments.AuthViewModel;
import com.dreambig.supplymanagementapp.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

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

        //View model
        mViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        //Initialize nav components
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(binding.fragmentContainerView.getId());
        navController = navHostFragment.getNavController();

        //Initialize details
        init();
        mViewModel.init();

        Uri resetPassword = getIntent().getData();
        if(resetPassword!=null){
            resetPassword(resetPassword);
        }
        else{
            //Check the previous signed in
            checkPreviousSignIn();
        }
        //permissions
        permissions();
    }

    private void resetPassword(Uri resetPassword) {
        List<String> params = resetPassword.getPathSegments();
        String resetToken = params.get(params.size() - 1);
        Bundle bundle = new Bundle();
        bundle.putString("resetToken", resetToken);
        navController.setGraph(R.navigation.authentication_navigation);
        navController.navigate(R.id.action_signUpFragment_to_createNewPasswordFragment, bundle);
    }

    private void permissions() {
        String[] permissionsStorage = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        int requestExternalStorage = 1;
        int permissionREAD = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWRITE = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionREAD != PackageManager.PERMISSION_GRANTED || permissionWRITE != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissionsStorage, requestExternalStorage);
        }
    }

    private void init(){
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(WEB_CLIENT_ID)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }


    public void googleSignOut(){
        mGoogleSignInClient.signOut();
    }

    public void signOut(){
        mViewModel.signOut();
        navController.setGraph(R.navigation.authentication_navigation);
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

    public void checkPreviousSignIn(){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        //google account
        if(account != null)
            mViewModel.checkGoogleAccount(account.getEmail());
        else
            mViewModel.checkAutoSignIn();

        mViewModel.getmCheckGoogleAccount().observe(this, new Observer<AuthResponseModel>() {
            @Override
            public void onChanged(AuthResponseModel authResponseModel) {
                if(authResponseModel == null) {
                    return;
                }

                if(authResponseModel.getExist() && authResponseModel.getisGmail()){
                    mGoogleSignInClient.silentSignIn()
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<GoogleSignInAccount>() {
                                @Override
                                public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                                    try {
                                        GoogleSignInAccount account = task.getResult(ApiException.class);
                                        mViewModel.updateToken(account.getIdToken());
                                        navController.setGraph(R.navigation.main_navigation);
                                    } catch (ApiException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).addOnFailureListener(MainActivity.this, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    googleSignOut();
                                    mViewModel.checkAutoSignIn();
                                }
                            });
                }
                else{
                    googleSignOut();
                    mViewModel.checkAutoSignIn();
                }
            }
        });

        //custom account
        mViewModel.getIsSignedIn().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSignedIn) {
                if(isSignedIn)
                    navController.setGraph(R.navigation.main_navigation);
                else{
                    navController.setGraph(R.navigation.authentication_navigation);
                }
            }
        });





    }

    public void popBackStack(){
        navController.popBackStack();
    }

    public void setGoogleAuthCallBack(GoogleAuthCallBack googleAuthCallBack) {
        this.googleAuthCallBack = googleAuthCallBack;
    }

    public interface GoogleAuthCallBack{
        void result(GoogleSignInAccount account);
    }
}