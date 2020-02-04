package com.paycraft.service;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.paycraft.ticketbookingoperator.TicketBookingApplication;

import java.io.InputStream;

import ir.mirrajabi.okhttpjsonmock.OkHttpMockInterceptor;
import ir.mirrajabi.okhttpjsonmock.providers.InputStreamProvider;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Service {
    private static final String BASE_URL = "http://example.com";
    private final UsersService usersService;

    private static Service Instance = null;

    private Service() {
        usersService = constructService();
    }

    public static Service getInstance() {
        if (Instance == null){
            Instance = new Service();
        }
        return Instance;
    }

    public UsersService getUserService(){
        return usersService;
    }

    private UsersService constructService() {
        OkHttpClient okHttpClient = constructClient();
        Retrofit retrofit = constructRetrofit(okHttpClient);
        return retrofit.create(UsersService.class);
    }

    private Retrofit constructRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .build();
    }

    private OkHttpClient constructClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new OkHttpMockInterceptor(getAndroidProvider(), 5))
                .build();
    }

    private InputStreamProvider getAndroidProvider() {
        return new InputStreamProvider() {
            @Override
            public InputStream provide(String path) {
                try {
                    return TicketBookingApplication.getInstance().getAssets().open(path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }
}
