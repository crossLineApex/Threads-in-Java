package com.learnjava;

public class MyThread extends Thread{

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("End thread run");
    }

    public static void main(String[] args) throws InterruptedException {
        MyThread t1 = new MyThread();
        t1.start();
        System.out.println("End Prog ...");
    }
}
