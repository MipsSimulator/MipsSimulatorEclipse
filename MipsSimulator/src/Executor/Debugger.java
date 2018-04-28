package Executor;

import java.util.LinkedList;
import java.util.Queue;

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
	private RegisterModel registerModel;
	private Executor executor;
	
	private static Debugger debugger;
	private int breakPoint;
	
	private Debugger() {
		
		memory = MainMemoryModel.getInstance();
		registerModel = RegisterModel.getInstance();
		executor = Executor.getInstance();
		
		breakPoint = Integer.MAX_VALUE;
		// empty
	}
	
	public static Debugger getInstance() {
		if(debugger == null)
			debugger = new Debugger();
		
		return debugger;
	}
	
	
	public void breakPoint(int breakPoint) {
		
		this.breakPoint = breakPoint + registerModel.getPc();
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
		
		this.breakPoint = registerModel.getPc()+1;
		compile();
		update();
		
	}
	
	
	private void compile() {
		
		executor.setIsExecuting(true);
		int lastInstrAddress = memory.getLastInstrAddress();
		
		// Loop and constraints of program execution
		while(executor.isExecuting() && registerModel.getPc() <= lastInstrAddress && registerModel.getPc() < breakPoint) {
			System.out.println("Executing Instruction...");
			executor.executeInstruction();
		}
		
		this.breakPoint = Integer.MAX_VALUE;
	}
	
	
	
	public void update() {
		
		DebugEvent debugEvent = new DebugEvent(registerModel.getRegisterFile(),memory.getRecentValues(),
				memory.getRecentAddresses());
		
		Queue<Long> q = debugEvent.getMemValues();
		Queue<Integer> q2 = debugEvent.getMemAddresses();
		
		System.out.println("10 Most recently accessed addresses");
		while(!q.isEmpty()) {
			System.out.print(q2.poll() + "     ");
			System.out.println(q.poll());
			
		}
		System.out.println("Register File:");
			for(int i = 0; i<debugEvent.getRegisterState().length; i++) {
				System.out.println("Register " + i + ": " + debugEvent.getRegisterState()[i]);
			}
		
		
		
		
		// Create and send a new debug event to the view to update some table that the user would see.
		
	}
}
