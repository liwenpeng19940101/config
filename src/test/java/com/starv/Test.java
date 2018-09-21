package com.starv;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Created by m1590 on 2018/9/6.
 */
public class Test {

    public  class  Parent{
        public Parent(){
            System.out.println("父类的构造方法");
        }
    }
    public  class Son extends Parent{
        public Son(){
            System.out.println("子类的构造方法");
        }
    }
    @org.junit.Test
    public void  test(){
        Parent parent=new Son();

    }
}
