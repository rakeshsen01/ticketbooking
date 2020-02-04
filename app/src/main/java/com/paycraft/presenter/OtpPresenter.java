package com.paycraft.presenter;

import android.text.TextUtils;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.paycraft.model.beans.OtpBean;
import com.paycraft.presenter.modelview.OtpView;
import com.paycraft.presenter.modelview.dao.OtpAccessDao;
import com.paycraft.ticketbookingoperator.R;
import com.paycraft.view.fragment.Otp;

import java.util.Objects;

public class OtpPresenter extends BasePresenter implements OtpAccessDao.Listener {
    private OtpView otpView;
    private final OtpAccessDao otpAccessDao;

    public OtpPresenter(OtpView otpView, OtpAccessDao otpAccessDao) {
        this.otpView = otpView;
        this.otpAccessDao = otpAccessDao;

    }

    public void verifyOtp(){
      otpAccessDao.verifyOtpRequest(this);
    }

    @Override
    public void onFailure() {
     if(otpView != null){
         otpView.showSnackBar(getString(R.string.error_failure));
     }
    }

    @Override
    public void onSuccess(String response) {
        if (otpView != null && !TextUtils.isEmpty(response)) {
            Gson gson = new Gson();
            OtpBean otpBean = gson.fromJson(response,OtpBean.class);
            int otpValue = Objects.requireNonNull(((Otp) otpView).getArguments()).getInt("otp");
            if(otpBean.getOtp() == otpValue){
                // Verify
                otpView.backToActivity();
            }
            else{
                if(otpView != null) {
                    otpView.showError();
                }
            }
        }

    }

    public void onDestroy() {
        otpView = null;
    }

    public String getString(int id) {
        if(otpView != null){
            return ((Fragment)otpView).getString(id);
        }
        else
            return "";
    }
}
