package com.csumb.pmoung.kiqback;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by BRX01 on 10/31/2016.
 */
public class userTest {
    private User userTest;
    @Before
    public void create(){
        userTest = new User(1, "Sean", "O'Fallon", "93955", "sofallon@csumb.edu", "M", "2016-10-27", "About Sean");
    }

    @Test
    public void getUserId() throws Exception {
        Boolean idIs1 = false;
        try{
            if(userTest.getUserId() == 1)
            {
                idIs1=true;
            }
        }catch(Exception e){
            idIs1=false;
        }

        assertTrue(idIs1);
    }

    @Test
    public void setUserId() throws Exception {
        Boolean idIs100 = false;
        try{
            userTest.setUserId(100);
            if(userTest.getUserId() == 100)
            {
                idIs100=true;
            }
        }catch(Exception e){
            idIs100=false;
        }

        assertTrue(idIs100);
    }

}

