package Executor;


/**
 * Packages up both the type of instruction and the actual instruction.
 * @author Ben
 *
 */
public class Instruction {
	
	public static final int RTYPE = 1;
	public static final int ITYPE = 2;
	public static final int JTYPE = 3;
	
	private int type;
	private int[] instruction;
	
	public Instruction(int type, int[] instruction) {
		this.type = type;
		this.instruction = instruction;
	}
	
	public int getType() {
		return this.type;
	}
	
	public int[] getInstruction() {
		return this.instruction;
	}
}





