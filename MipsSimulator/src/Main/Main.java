package Main;

import java.io.FileNotFoundException;
import java.util.Queue;

import CodeParser.CodeParser;
import Controller.Controller;
import Executor.Instruction;


public class Main {

	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		Controller controller = new Controller();
//		
//		controller.operation("build");
//		controller.operation("compile");
	
		
	CodeParser codeParser = CodeParser.getInstance();
	
	
	try {
		
		CodeParser.parseLabels();
		
		
		Queue<Long> x = codeParser.parseCode();
		
//		System.out.println("Results: ");
//		while(!x.isEmpty()) {
//			System.out.println(x.poll());
//		}
//		
//		while(!x.isEmpty()) {
//			Instruction instr = codeParser.parseInstruction(x.poll());
//			for(int i = 0; i<instr.getInstruction().length; i++) {
//				System.out.println(instr.getInstruction()[i]);
//			}
//			
//			System.out.println("");
//		}
		
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	
	//codeParser.parseInstruction(intNUM)
	
	
	}
	
	

}
