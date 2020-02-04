package com.paycraft.model.beans;

public class OtpBean {
    private final String id;
    private final int otp;

    public OtpBean(String id, int otp) {
        this.id = id;
        this.otp = otp;
    }



    public int getOtp() {
        return otp;
    }


    @Override
    public String toString() {
        return "OtpBean{" +
                "id='" + id + '\'' +
                ", otp=" + otp +
                '}';
    }
}
