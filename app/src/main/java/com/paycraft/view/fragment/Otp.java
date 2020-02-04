package com.paycraft.view.fragment;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.google.android.material.snackbar.Snackbar;
import com.paycraft.ticketbookingoperator.R;
import com.paycraft.presenter.modelview.dao.OtpAccessDao;
import com.paycraft.presenter.OtpPresenter;
import com.paycraft.presenter.modelview.OtpView;

import java.util.Objects;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class Otp extends Fragment implements OtpView {

    @Override
    public void showSnackBar(String message) {

    }

    public interface OtpCommunication{
         void verifyOtp();
    }


    private final OtpCommunication otpCommunication;
    private OtpPresenter otpPresenter;
    private LinearLayout fragment_otp;

    public Otp(OtpCommunication otpCommunication) {
        this.otpCommunication = otpCommunication;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.otp_fragment, container, false);
    }

    private void inflateComponentView(View view) {
        OtpTextView otpTextView = view.findViewById(R.id.otp_view);
        fragment_otp = view.findViewById(R.id.fragment_otp);
        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {

            }

            @Override
            public void onOTPComplete(String otp) {
                Objects.requireNonNull(getArguments()).putInt("otp", Integer.parseInt(otp));
                otpPresenter.verifyOtp();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inflateComponentView(view);
        otpPresenter = new OtpPresenter(this,new OtpAccessDao());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }

    @Override
    public void showError() {
        Snackbar snackbar = Snackbar.make(fragment_otp, otpPresenter.getString(R.string.error_otp), Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onDestroy() {
        otpPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void backToActivity() {
        otpCommunication.verifyOtp();
    }
}
