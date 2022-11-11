package com.dreambig.supplymanagementapp.DI;

import android.app.Application;

import com.dreambig.supplymanagementapp.Networks.AuthService;
import com.dreambig.supplymanagementapp.Repositories.AuthRepo;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    public AuthRepo getInstance(AuthService authService, Application application){
        return AuthRepo.getInstance(authService, application);
    }

    @Provides
    public AuthService getAuthServices(Retrofit retrofit){
        return retrofit.create(AuthService.class);
    }

    @Provides
    public Retrofit retrofitBuilder(){
        return new Retrofit.Builder()
                .baseUrl("https://supply-management-restapi.vercel.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
