package com.paycraft.view.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;
import com.paycraft.model.beans.User;
import com.paycraft.presenter.UserPresenter;
import com.paycraft.presenter.modelview.UserView;
import com.paycraft.presenter.modelview.dao.UserAccessDao;
import com.paycraft.ticketbookingoperator.R;
import com.paycraft.view.fragment.Otp;

public class UserActivity extends AppCompatActivity implements UserView, View.OnClickListener, Otp.OtpCommunication {

    private EditText etUserName;
    private EditText etMobileNumber;
    private EditText etDob;
    private EditText etDocumentId;
    private Spinner documentType;
    private Button bt_Submit, second_Screen;
    private LinearLayout coordinatorLayout;
    private UserPresenter userPresenter;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inflateViewComponent();
        userPresenter = new UserPresenter(this, new UserAccessDao());
    }

    @Override
    protected void onDestroy() {
        userPresenter.onDestroy();
        super.onDestroy();
    }

    /**
     * Initialize User views
     */
    private void inflateViewComponent() {
        coordinatorLayout = findViewById(R.id.mainactiviy);
        etUserName = findViewById(R.id.et_username);
        etMobileNumber = findViewById(R.id.et_mobile_number);
        etDob = findViewById(R.id.et_dob);
        etDocumentId = findViewById(R.id.et_document_id);
        documentType = findViewById(R.id.sp_documentType);
        bt_Submit = findViewById(R.id.bt_Submit);
        bt_Submit.setOnClickListener(this);
        etDob.setOnClickListener(this);
        second_Screen = findViewById(R.id.second_Screen);
        second_Screen.setOnClickListener(this);
    }

    /**
     * Validate User Details
     */
    private void validateUserDetails() {
        user = new User();
        user.setDateOfBirth(etDob.getText().toString());
        user.setDocumentID(etDocumentId.getText().toString());
        user.setMobileNumber(etMobileNumber.getText().toString());
        user.setName(etUserName.getText().toString());
        user.setDocumentType(documentType.getSelectedItem().toString());
        if (userPresenter.validateUser(user)) {
            userPresenter.proceedRequest();
        }
    }


    @Override
    public void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    /**
     * Navigate to Otp screen after getting OTP.
     *
     * @param otpValue
     */
    @Override
    public void navigateToOtpScreen(int otpValue) {
        Bundle bundle = new Bundle();
        bundle.putInt("otp", otpValue);
        Otp otp = new Otp(this);
        otp.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment, otp, otp.getClass().getSimpleName()).addToBackStack(null).commit();
    }

    @Override
    public void setDateOfBirth(String dob) {
        etDob.setText(dob);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_Submit:
                validateUserDetails();
                break;
            case R.id.et_dob:
                userPresenter.selectDob();
                break;
            case R.id.second_Screen:
                moveToSecondScreen();
                break;
            default:
                break;
        }

    }

    /**
     * Move to second screen Booking Activity
     */
    private void moveToSecondScreen() {
        if (user != null) {
            Intent intent = new Intent(this, BookingActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }
    }


    @Override
    public void verifyOtp() {
        disableViews();
        second_Screen.setEnabled(true);
        onBackPressed();
    }

    /**
     * Disable Views ones user verify the OTP
     */
    private void disableViews() {
        etUserName.setEnabled(false);
        etMobileNumber.setEnabled(false);
        etDob.setEnabled(false);
        etDocumentId.setEnabled(false);
        documentType.setEnabled(false);
        bt_Submit.setEnabled(false);
    }
}
