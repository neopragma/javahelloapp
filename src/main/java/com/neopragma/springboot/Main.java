package com.neopragma.springboot;

public class Main {
	
	static Hello hello;
	
	public static void main(String[] args) {
		hello = new Hello();
		System.out.println(hello.greet());
		System.exit(0);
	}

}
