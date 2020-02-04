package com.paycraft.presenter.modelview.dao;

import com.paycraft.service.Service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpAccessDao {
    public interface Listener {
        void onFailure();

        void onSuccess(String response);
    }

    public void verifyOtpRequest(final Listener listener) {
        Service service = Service.getInstance();
        Call<ResponseBody> result = service.getUserService().verifyOtp("verification");
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    listener.onSuccess(response.body().string());
                    System.out.println(response.body().string());//convert response to string
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailure();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onFailure();
                t.printStackTrace();
            }
        });
    }

}
