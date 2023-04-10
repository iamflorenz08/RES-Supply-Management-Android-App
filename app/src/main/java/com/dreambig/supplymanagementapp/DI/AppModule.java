package com.dreambig.supplymanagementapp.DI;

import android.app.Application;

import com.dreambig.supplymanagementapp.Locals.Token;
import com.dreambig.supplymanagementapp.Networks.AuthService;
import com.dreambig.supplymanagementapp.Networks.ProfileService;
import com.dreambig.supplymanagementapp.Networks.SupplyService;
import com.dreambig.supplymanagementapp.Repositories.AuthRepo;
import com.dreambig.supplymanagementapp.Repositories.FirebaseStorageRepo;
import com.dreambig.supplymanagementapp.Repositories.ProfileRepo;
import com.dreambig.supplymanagementapp.Repositories.SupplyRepo;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    public ProfileRepo getProfileRepo(ProfileService profileService, Token tokenStorage){
        return new ProfileRepo(profileService, tokenStorage);
    }

    @Provides
    public Token getTokenStorage(Application application){
        return new Token(application);
    }

    @Provides
    public ProfileService getProfileService(Retrofit retrofit){
        return retrofit.create(ProfileService.class);
    }

    @Provides
    public FirebaseStorageRepo getFirebaseStorage(){
        return new FirebaseStorageRepo();
    }

    //Supplies
    @Provides
    public SupplyRepo getSupplyRepoInstance(SupplyService service, AuthRepo authRepo, Application application){
        return new SupplyRepo(service, authRepo, application);
        //return SupplyRepo.getInstance(service, authRepo, application);
    }

    @Provides
    public SupplyService getSupplyService(Retrofit retrofit){
        return retrofit.create(SupplyService.class);
    }

    //Authentication
    @Provides
    public AuthRepo getInstance(AuthService authService, Application application){
        return AuthRepo.getInstance(authService, application);
    }

    @Provides
    public AuthService getAuthServices(Retrofit retrofit){
        return retrofit.create(AuthService.class);
    }


    //Retrofit
    @Provides
    @Singleton
    public Retrofit retrofitBuilder(){
        return new Retrofit.Builder()
                .baseUrl("https://supply-management-restapi.vercel.app/") //localhost - http://10.0.2.2:3000/  //online host- https://supply-management-restapi.vercel.app
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
