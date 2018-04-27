package Controller;

import java.util.ArrayList;
import Model.*;
import java.util.Queue;

import Executor.*; // Import the Executor package
import CodeParser.*;

/**
 * The Controller class will dictate what the Executor class executes. This class has a
 * CodeParser that helps determine what gets executed by the Executor class.
 * @author Ben
 *
 */
public class Controller {

	/**
	 * The controller tells the executor what to do but does not know the underlying implementation.
	 * Note: The executor has the Model(Reg model and Main memory model)
	 */
	private Executor executor;
	
	/**
	 * Single instance of code parser (CodeParser is a singleton)
	 * Code Parser is used as a utility class.
	 */
	private CodeParser codeParser;
	
	/**
	 * Instantiates the executor and code parser.
	 */
	public Controller() {
		executor = new Executor();
		codeParser = new CodeParser();
		// instantiate the view here and pass it an instance of controller.
	}
	
	
	/**
	 * Some action occurs in the view and sends some info of the event to the Controller via this method.
	 * The info is encoded in a String. This method deciphers what the operation is and orders the executor
	 * to perform the operation.
	 * @param operation
	 */
	public void operation(String operation) {
		
		// Builds the program. Stores the instructions in memory.
		if(operation.equals("build")) {
			Queue<Integer> instructions = CodeParser.parseInstructions();
			executor.build(instructions);
		}
		
		// Compiles the program. Calls the executor to fetch instructions from memory and execute them.
		else if(operation.equals("compile")) {
			executor.compile();
		}
		
		// Other events that may happen in the program..
		
	}
	
	
	
	
	
	
}
