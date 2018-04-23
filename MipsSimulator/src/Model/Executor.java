package Model;

/**
 * Main model Executor contains sub models memory and register
 * Interprets memory addresses and modifies values in registers and memory
 * for instructions
 * @author James
 */
public class Executor {

	private MemoryModel memory;
	//private RegisterModel register;
	
	/*
	J, 
	BEQ,
	SW, 
	LW
	MUL
	NOP
	 */
	
	
	/**
	 * Executes an R type instruction based on its function and parameter values
	 * @param rd The destination register
	 * @param rs The first source register
	 * @param rt The second source register
	 * @param shamt shift amount, for 
	 */
	public void executeR(int rd, int rs, int rt, int shamt, int func)
	{
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
		//ADDI instruction
		case 8:
			rs = memory.getRegister(rs);
			memory.setRegister(rs + imm, rt);
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
		rt = memory.getRegister(rt);
		
		memory.setRegister(rt<<shamt, rd);
	}
	
	/**
	 * Shifts the value in rt by shamt bits to the right
	 * @param rd destination register to store result 
	 * @param rt target register value to shift
	 * @param shamt amount to shift the value rt
	 */
	private void srl(int rd, int rt, int shamt)
	{
		rt = memory.getRegister(rt);
		
		memory.setRegister(rt>>shamt, rd);
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
		rt = memory.getRegister(rt);
		rs = memory.getRegister(rs);
		
		memory.setRegister(rs + rt, rd);
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
		rt = memory.getRegister(rt);
		rs = memory.getRegister(rs);
		
		memory.setRegister(rs - rt, rd);
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
		rt = memory.getRegister(rt); 
		rs = memory.getRegister(rs);
		
		memory.setRegister(rs&rt, rd);
	}

	/**
	 * Stores the value of rs AND rt in rd
	 * 
	 * @param rd destination register
	 * @param rs source register 1
	 * @param rt source register 2
	 */
	private void or(int rd, int rs, int rt)
	{
		rt = memory.getRegister(rt);
		rs = memory.getRegister(rs);
		
		memory.setRegister(rs|rt, rd);
	}
	
	
}
