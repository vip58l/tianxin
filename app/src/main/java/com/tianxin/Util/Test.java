package com.tianxin.Util;

public class Test extends bbb implements aaa {

    private aaa a;
    private bbb b;

    @Override
    public void abc2(int i) {
        a.abc2(10);
    }

    @Override
    void abc3(int i) {
        b.abc3(20);
    }
}

//接口不能被实例化 implements 调用
 interface  aaa {
    int a = 20;

    void abc2(int i);
}

//抽象类不能被实例化只能作为父类被继承 extends 继承来调用
abstract class bbb {
    int b = 10;

    abstract void abc3(int i);
}




