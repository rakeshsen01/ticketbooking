package com.paycraft.presenter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.DatePicker;

import com.google.gson.Gson;
import com.paycraft.model.beans.OtpBean;
import com.paycraft.model.beans.User;
import com.paycraft.presenter.modelview.UserView;
import com.paycraft.presenter.modelview.dao.UserAccessDao;
import com.paycraft.ticketbookingoperator.R;

import java.util.Calendar;

/**
 *  This class is responsible to handle all call for User
 */
public class UserPresenter extends  BasePresenter implements UserAccessDao.Listener {
    private UserView userView;
    private final UserAccessDao userAccessDao;
    protected final String AADHAAR = "Aadhaar";
    protected final String PAN = "Pan";
    public UserPresenter(UserView userView, UserAccessDao userAccessDao) {
        this.userView = userView;
        this.userAccessDao = userAccessDao;
    }

    /**
     *  Validate User form and proceed for OTP generation.
     * @param user
     */
    public boolean validateUser(User user) {
        String patternMobile = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}";
        if(TextUtils.isEmpty(user.getName())){
            userView.showSnackBar(getString(R.string.error_username));
            return false;
        }
        else if(TextUtils.isEmpty(user.getDateOfBirth())){
            userView.showSnackBar(getString(R.string.error_dob));
            return false;
        }
        else if(TextUtils.isEmpty(user.getDocumentType()) || user.getDocumentType().equalsIgnoreCase("Select Document")){
            userView.showSnackBar(getString(R.string.error_dot));
            return false;
        }
        else if(TextUtils.isEmpty(user.getDocumentID())){
            userView.showSnackBar(getString(R.string.error_did));
            return false;
        }
        else if (!verifyDocumentID(user.getDocumentID(),user.getDocumentType())){
            userView.showSnackBar(getString(R.string.error_did) + user.getDocumentType());
            return false;
        }
        else if(TextUtils.isEmpty(user.getMobileNumber()) || !user.getMobileNumber().matches(patternMobile)){
            userView.showSnackBar(getString(R.string.error_mobile));
            return false;
        }

        return true;
    }

    public void proceedRequest(){
        userAccessDao.requestUsers(this);

    }


    private String getString(int id){
        if(userView != null){
            return ((Context)userView).getString(id);
        }
        else
         return "";

    }

    /**
     *  Verify Document ID based on selected Document
     * @param id
     * @param documentType
     * @return
     */
    protected boolean verifyDocumentID(String id, String documentType) {
        boolean isValidCard;
        String patternDigit = "\\d{10}";
        String patternPan = "^([a-zA-Z]){5}([0-9]){4}([a-zA-Z]){1}?$";
        String patternLicence = "[a-zA-Z]{2}-\\d\\d-(19\\d\\d|20[01][0-9])-\\d{7}$";
        if(documentType.equalsIgnoreCase(AADHAAR)){
            isValidCard = id.matches(patternDigit);
        }
        else if(documentType.equalsIgnoreCase(PAN)){
            isValidCard = id.matches(patternPan);
        }
        else{
            isValidCard = id.matches(patternLicence);
        }
        return isValidCard;
    }

    public void onDestroy() {
        userView = null;
    }

    /**
     * Open Dialog Picker for Date of Birth
     */
    public void selectDob() {
        final Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        DatePickerDialog picker = new DatePickerDialog((Context) userView,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        userView.setDateOfBirth((dayOfMonth + "/" + (monthOfYear + 1) + "/" + year));
                    }
                }, year, month, day);
        picker.show();
    }

    @Override
    public void onFailure() {
      if(userView != null){
          userView.showSnackBar(getString(R.string.error_failure));
      }
    }

    @Override
    public void onSuccess(String response) {
        if (userView != null && !TextUtils.isEmpty(response)) {
            Gson gson = new Gson();
            OtpBean otpBean = gson.fromJson(response,OtpBean.class);
            userView.showSnackBar("Your Otp Is: "+otpBean.getOtp());
            userView.navigateToOtpScreen(otpBean.getOtp());
        }
    }
}
