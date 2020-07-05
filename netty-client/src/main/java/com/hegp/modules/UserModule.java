package com.hegp.modules;

import com.hegp.common.annotation.Action;
import com.hegp.common.annotation.Module;
import com.hegp.common.constants.Constants;

@Module(module = Constants.USER_MODULE)
public class UserModule {
    public UserModule() { }
    @Action(action = Constants.USER_MODULE_TEST1)
    public void test1() {
        System.out.println("test1");
    }

    @Action(action = Constants.USER_MODULE_TEST2)
    public void test2() {
        System.out.println("test2");
    }
}
