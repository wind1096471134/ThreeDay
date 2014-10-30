package com.android.threeday1;

import com.android.threeday.test;

import junit.framework.TestCase;


public class MyClass extends TestCase{

    public void testJunit( ) throws Exception{
        //assertEquals(true, false);
        TestCase.assertEquals(test.get(), true);
    }
}
