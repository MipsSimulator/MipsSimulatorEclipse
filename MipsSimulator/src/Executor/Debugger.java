package Executor;

import CodeParser.CodeParser;
import Model.Executor;
import Model.MainMemoryModel;
import Model.RegisterModel;

/**
 * Debugs the program based on the users request.
 * Has a handle to MainMemoryModel, RegisterModel, and Executor to be able to fully analyze the state
 * of the program as it executes.
 * @author Ben
 *
 */
public class Debugger {
	
	private MainMemoryModel memory;
	private RegisterModel register;
	private Executor executor;
	
	private int breakPoint;
	
	public Debugger() {
		
		memory = MainMemoryModel.getInstance();
		register = RegisterModel.getInstance();
		executor = Executor.getInstance();
		
		breakPoint = Integer.MAX_VALUE;
		// empty
	}
	
	
	public void breakPoint(int breakPoint) {
		
		this.breakPoint = breakPoint;
		compile();
		update();
		
	}
	
	
	/**
	 * After hitting the breakpoint, the user selects continue running. The program finishes
	 * executing.
	 */
	public void continueRunning() {
		
		compile();
		update();	
	}
	
	/**
	 * Go to the next line of execution and see the state of the program.
	 */
	public void step() {
		
		this.breakPoint = memory.getPc()+1;
		compile();
		update();
		
	}
	
	
	public void compile() {
		
		executor.setIsExecuting(true);
		int lastInstrAddress = memory.getLastInstrAddress();
		
		// Loop and constraints of program execution
		while(executor.isExecuting() && memory.getPc() < lastInstrAddress && memory.getPc() < breakPoint) {
			executor.executeInstruction();
		}
		
		this.breakPoint = Integer.MAX_VALUE;
	}
	
	
	
	public void update() {
		
		// Create and send a new debug event to the view to update some table that the user would see.
		
	}
}
