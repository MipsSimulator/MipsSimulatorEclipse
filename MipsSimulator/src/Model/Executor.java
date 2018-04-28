package Model;

import java.util.Queue;
import CodeParser.*;
import Executor.Instruction;

/**
 * Main model Executor contains sub models memory and register
 * Interprets memory addresses and modifies values in registers and memory
 * for instructions
 * @author James
 */
public class Executor {
	
	private boolean isExecuting;
	private MainMemoryModel memory;
	private RegisterModel register;
	private CodeParser codeParser;
	private static Executor executor;

	//git add --all 
	//git commit -m ""
	//git push origin Development
	/*
	J, 
	BEQ,
	 */
	
	private  Executor() {	
		this.memory = MainMemoryModel.getInstance();
		this.register = RegisterModel.getInstance();
		this.codeParser = CodeParser.getInstance();
	}
	
	
	/**
	 * Singleton of executor
	 * @return
	 */
	public static Executor getInstance() {
		if(executor == null) {
			executor = new Executor();
		}
		return executor;
	}
	
	
	/**
	 * Executes an R type instruction based on its function and parameter values
	 * @param rd The destination register
	 * @param rs The first source register
	 * @param rt The second source register
	 * @param shamt shift amount, for 
	 */
	public void executeR(int instruction[])
	{
		int rs = instruction[1];
		int rt = instruction[2];
		int rd = instruction[3];
		int shamt = instruction[4];
		int func = instruction[5];
		
		// Determines the operation based on func value
		switch(func)
		{
			// SLL instruction
			case 0: 
				sll(rd, rt, shamt);
				break;
				
			// SRL instruction 
			case 2:
				srl(rd, rt, shamt);
				break;
				
			// ADD instruction
			case 32: 
				add(rd, rs, rt);
				break;
				
			// AND instruction
			case 36:
				and(rd,rs,rt);
				break;
			
			// SUB instruction
			case 34:
				sub(rd,rs,rt);
				break;
			
			// OR instruction
			case 37:
				or(rd,rs,rt);
				break;
				
			default:
				
				System.out.println("ERR: Instruction not yet implemented!");
				break;
		}
	}
	
	/**
	 * Executes an I type instruction determined by its OP code
	 * @param op the opcode of the instruction
	 * @param rt the target register to store value in 
	 * @param rs the register value that imm value will be added too
	 * @param imm the number that will be added to rs
	 */
	public void executeI(int op, int rt, int rs, int imm)
	{
		switch(op)
		{
		// ADDI instruction
		case 8:
			addi(rs, rt, imm);
			break;
			
		// SW instruction
		case 42:
			
			break;
			
		// LW instruction
		case 35:
			
			break;
		
		// MUL instruction 
		case 28: 
			
			break; 
			
		
		}
		
	}
	
	/**
	 * Shifts the value in rt by shamt bits to the left
	 * @param rd destination register to store result 
	 * @param rt target register value to shift
	 * @param shamt amount to shift the value rt
	 */
	private void sll(int rd, int rt, int shamt)
	{
		rt = register.getRegister(rt);
		
		register.setRegister(rt<<shamt, rd);
	}
	
	/**
	 * Shifts the value in rt by shamt bits to the right
	 * @param rd destination register to store result 
	 * @param rt target register value to shift
	 * @param shamt amount to shift the value rt
	 */
	private void srl(int rd, int rt, int shamt)
	{
		rt = register.getRegister(rt);
		
		register.setRegister(rt>>shamt, rd);
	}
	
	/**
	 * Stores the sum of rs and rt into rd
	 * 
	 * @param rd destination register
	 * @param rs source register 1
	 * @param rt source register 2
	 */
	private void add(int rd, int rs, int rt)
	{
		rt = register.getRegister(rt);
		rs = register.getRegister(rs);
		
		register.setRegister(rs + rt, rd);
	}
	
	/**
	 * Stores the difference of rs and rt into rd
	 * 
	 * @param rd destination register
	 * @param rs source register 1
	 * @param rt source register 2
	 */
	private void sub(int rd, int rs, int rt)
	{
		rt = register.getRegister(rt);
		rs = register.getRegister(rs);
		
		register.setRegister(rs - rt, rd);
	}
	
	/**
	 * Stores the value of rs AND rt in rd
	 * 
	 * @param rd destination register
	 * @param rs source register 1
	 * @param rt source register 2
	 */
	private void and(int rd, int rs, int rt)
	{
		rt = register.getRegister(rt); 
		rs = register.getRegister(rs);
		
		register.setRegister(rs&rt, rd);
	}

	/**
	 * Stores the value of rs OR rt in rd
	 * 
	 * @param rd destination register
	 * @param rs source register 1
	 * @param rt source register 2
	 */
	private void or(int rd, int rs, int rt)
	{
		rt = register.getRegister(rt);
		rs = register.getRegister(rs);
		
		register.setRegister(rs|rt, rd);
	}
	
	
	/**
	 * Stores value of rt + imm in register rd 
	 * @param rd 
	 * @param rt 
	 * @param imm
	 */
	private void addi(int rt, int rs, int imm)
	{
		rs = register.getRegister(rs);
		register.setRegister(rs + imm, rt);
	}
	
	/**
	 * Places a value from memory into a register
	 * @param rd
	 * @param rt
	 * @param imm
	 */
	private void lw(int rt, int rs, int imm)
	{
		int lval;
		rs = register.getRegister(rs);
		lval = memory.loadMemory(rs + imm);
		register.setRegister(lval, rt);
	}

	/**
	 * Places a value from register into memory 
	 * @param rs
	 * @param rt
	 * @param imm
	 */
	private void sw(int rt, int rs, int imm)
	{
		rt = register.getRegister(rt);
		rs = register.getRegister(rs);
		memory.storeMemory(rs + imm, rt);
	}
	
	/**
	 * Jumps to the address that gets passed
	 * @param address
	 */
	private void j(int address) {
		register.setPc(address);
	}
	
	
	/**
	 * Builds the code; Stores the instructions in the Main Memory Model, at the bottom of
	 * the text segment. Note the actual pc doesn't get updated, this is just to have a temporary pointer
	 * to the main memory.
	 * @param instructions - array of instructions as ints. Needs to be converted to binary to decode
	 * the instruction.
	 */
	public void build(Queue<Integer> instructions) {
		int pc = register.getPc();
		
		int size = instructions.size();
		// copy of program counter is incremented every iteration to store instruction at the next
		// address
		for(int i = 0; i<size; i++, pc++) {
			memory.storeMemory(pc,instructions.poll());
		}
		
	}
	
	/**
	 * Begins execution of the assembly program by fetching the instructions from memory.
	 * Once it has the instruction and is decoded by the CodeParser and the actual instruction
	 * execution is delegated to a method in this class. Either R,I, or J type methods.
	 */
	public void run() {
		
		isExecuting = true;
		int lastInstrAddress = memory.getLastInstrAddress();
		
		// Loop and constraints of program execution
		while(isExecuting && register.getPc() < lastInstrAddress) {
			executeInstruction();
		}
	
	}
	
	
	
	/**
	 * Executes J type instructions
	 * @param 
	 */
	public void executeJ(int[] instruction) {
		
		int op = instruction[0];
		int psuedoDirectAddress = instruction[1];
		
		
		switch(op) {
		
			case 2:
				j(psuedoDirectAddress);
			
		}
	}
	
	
	/**
	 * Set the execution flag. If it is true, then the program is executing and vice versa.
	 * @param isExecuting
	 */
	public void setIsExecuting(boolean isExecuting) {
		this.isExecuting = isExecuting;
	}
	
	/**
	 * Find out whether the program is executing
	 * @return the execution flag
	 */
	public boolean isExecuting() { return isExecuting;}
	
	
	
	/**
	 * Executes one instruction.
	 */
	public void executeInstruction() {
		
		int pc = register.getPc();
		Instruction instruction = codeParser.parseInstruction(memory.loadMemory(pc));
		
		/**
		 * Execute R, I, or J type instructions
		 */
		switch(instruction.getType()) {
			
			case Instruction.RTYPE: 
				executeR(instruction.getInstruction());
				break;
			case Instruction.ITYPE:
//				executeI(instruction.getInstruction());
				break;
			case Instruction.JTYPE:
				executeJ(instruction.getInstruction());
				break;
		}
	}
	

}
