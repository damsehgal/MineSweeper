package com.company;

/**
 * Created by dam on 15/7/17.
 */
public class Dog extends Animal {

    int x = 4;


    Dog (int y)
    {
        super(y);
        System.out.print("dog");
        x = 6;
    }
    public void bark() {
        System.out.print("sfdsfdlm");
    }
    public void eat()
    {
        System.out.print("Eating Dog");
    }
}
