package Main;

import java.io.FileNotFoundException;
import java.util.Queue;

import CodeParser.CodeParser;
import Controller.Controller;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Controller controller = new Controller();
		
		controller.operation("build");
		controller.operation("breakpoint");
		System.out.println("And then finish executing....");
		controller.operation("continue");



	}
	
}
