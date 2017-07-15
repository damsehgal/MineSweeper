package com.company;

/**
 * Created by dam on 15/7/17.
 */
public class Animal {

    int x = 2;

    Animal(int x) {

        System.out.print("super");
    }

    private void bark() {
        System.out.print("213");
    }
    final  void  show(){
        System.out.print("Show");
    }
    public static void hoot()
    {
        System.out.print("De de choot");
    }
    public void eat()
    {
        System.out.print("Eating");
    }

}
