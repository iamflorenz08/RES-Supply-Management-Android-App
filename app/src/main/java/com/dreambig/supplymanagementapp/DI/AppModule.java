package com.dreambig.supplymanagementapp.DI;

import android.app.Application;

import androidx.room.Room;

import com.dreambig.supplymanagementapp.Locals.RequisitionRoom.ItemDatabase;
import com.dreambig.supplymanagementapp.Locals.RequisitionRoom.RequistionDAO;
import com.dreambig.supplymanagementapp.Locals.Token;
import com.dreambig.supplymanagementapp.Networks.AuthService;
import com.dreambig.supplymanagementapp.Networks.NotificationService;
import com.dreambig.supplymanagementapp.Networks.ProfileService;
import com.dreambig.supplymanagementapp.Networks.RequisitionService;
import com.dreambig.supplymanagementapp.Networks.SavedItemService;
import com.dreambig.supplymanagementapp.Networks.SupplyService;
import com.dreambig.supplymanagementapp.Repositories.AuthRepo;
import com.dreambig.supplymanagementapp.Repositories.FirebaseStorageRepo;
import com.dreambig.supplymanagementapp.Repositories.NotificationRepo;
import com.dreambig.supplymanagementapp.Repositories.ProfileRepo;
import com.dreambig.supplymanagementapp.Repositories.RequisitionRepo;
import com.dreambig.supplymanagementapp.Repositories.SavedItemRepo;
import com.dreambig.supplymanagementapp.Repositories.SupplyRepo;

import java.net.URISyntaxException;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    public Socket getSocketInstance(){
        try {
            return IO.socket("http://10.0.2.2:3001");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Provides
    @Singleton
    public SavedItemRepo getSavedItemRepoInstance(SavedItemService savedItemService){
        return new SavedItemRepo(savedItemService);
    }

    @Provides
    @Singleton
    public NotificationRepo getNotificationInstance(NotificationService notificationService){
        return new NotificationRepo(notificationService);
    }

    @Provides
    public NotificationService getNotificationService(Retrofit retrofit){
        return retrofit.create(NotificationService.class);
    }

    @Provides
    @Singleton
    public RequisitionRepo getRequisitionInstance(RequisitionService requisitionService){
        return new RequisitionRepo(requisitionService);
    }

    @Provides
    public RequisitionService getRequisitionService(Retrofit retrofit){
        return retrofit.create(RequisitionService.class);
    }

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
    @Singleton
    public SupplyRepo getSupplyRepoInstance(SupplyService service, AuthRepo authRepo, ItemDatabase itemDatabase, Token token, Socket socket){
        return new SupplyRepo(service, authRepo, itemDatabase, token, socket);
    }

    @Provides
    @Singleton
    public ItemDatabase getItemDatabase(Application application){
        return Room.databaseBuilder(application.getApplicationContext(), ItemDatabase.class, "RequisitionDB")
                .build();
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
    public SavedItemService getSavedItemService(Retrofit retrofit){
        return retrofit.create(SavedItemService.class);
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
                .baseUrl("http://10.0.2.2:3000/") //localhost - http://10.0.2.2:3000/  //online host- https://supply-management-restapi.vercel.app
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
