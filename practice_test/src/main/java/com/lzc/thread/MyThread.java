package com.lzc.thread;

public class MyThread implements Runnable {

	public void run() {
		while(true)
		{
			System.out.println("-");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("start");
		MyThread myThread=new MyThread();
		Thread t=new Thread(myThread);
		//t.setDaemon(true);
		t.start();
		System.out.println("end");

	}

}
