package com.paycraft.presenter;

import com.paycraft.model.beans.User;
import com.paycraft.presenter.modelview.UserView;
import com.paycraft.presenter.modelview.dao.UserAccessDao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class TestUserPresenter {
    private UserPresenter userPresenter;

    @Mock
    private UserView userView;

    @Mock
    UserAccessDao userAccessDao;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        userPresenter = Mockito.spy(new UserPresenter(userView,userAccessDao));
    }


    @Test
    public void validateUser_invalidPan(){
        boolean isValid =userPresenter.verifyDocumentID("565656",userPresenter.PAN);
        Assert.assertFalse(isValid);
    }

    @Test
    public void validateUser_invalidAadhaar(){
        boolean isValid =userPresenter.verifyDocumentID("trrro",userPresenter.AADHAAR);
        Assert.assertFalse(isValid);
    }

    @Test
    public void validateUser_validPan(){
        boolean isValid =userPresenter.verifyDocumentID("DRLPS5952Q",userPresenter.PAN);
        Assert.assertTrue(isValid);
    }

    @Test
    public void validateUser_validUser(){
        User user = getSampleUser();
       boolean isValid = userPresenter.validateUser(user);
       Assert.assertTrue(isValid);
    }


    private User getSampleUser() {
        User  user = new User();
        user.setDocumentType("PAN");
        user.setName("rake");
        user.setDocumentID("DRLPS5952Q");
        user.setMobileNumber("4543432321");
        user.setDateOfBirth("22/09/1987");
        return user;
    }



}
