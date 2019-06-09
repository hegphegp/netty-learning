package com.hegp.modules;

import com.hegp.annotation.Action;
import com.hegp.annotation.Module;

@Module(module = "userModule")
public class UserModule {

    @Action(action = "test1")
    public void test1() {
        System.out.println("test1");
    }

    @Action(action = "test2")
    public void test2() {
        System.out.println("test2");
    }
}
