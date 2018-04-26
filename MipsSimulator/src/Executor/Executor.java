package Executor;

import Model.MainMemoryModel;
import Model.RegisterModel;

/**
	 * Main model Executor contains sub models memory and register
	 * Interprets memory addresses and modifies values in registers and memory
	 * for instructions
	 * @author James
	 */
	public class Executor {
		
		/**
		 * Flag that has to hold true after each instruction. The program can end before
		 * the last instruction in memory is reached.
		 */
		private boolean isExecuting;

		/**
		 * Main Memory Model
		 */
		private MainMemoryModel memoryModel;
		
		/**
		 * Register Model
		 */
		private RegisterModel regModel;
		
		//private RegisterModel register;
		
		/*
		J, 
		BEQ,
		ADDI, 
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
		 * Shifts the value in rt by shamt bits to the left
		 * @param rd destination register to store result 
		 * @param rt target register value to shift
		 * @param shamt amount to shift the value rt
		 */
		private void sll(int rd, int rt, int shamt)
		{
			rt = regModel.getRegister(rt);
			
			regModel.setRegister(rt<<shamt, rt);
		}
		
		/**
		 * Shifts the value in rt by shamt bits to the right
		 * @param rd destination register to store result 
		 * @param rt target register value to shift
		 * @param shamt amount to shift the value rt
		 */
		private void srl(int rd, int rt, int shamt)
		{
			rt = regModel.getRegister(rt);
			
			regModel.setRegister(rt>>shamt, rd);
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
			rt = regModel.getRegister(rt);
			rs = regModel.getRegister(rs);
			
			regModel.setRegister(rs + rt, rd);
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
			rt = regModel.getRegister(rt);
			rs = regModel.getRegister(rs);
			
			regModel.setRegister(rs - rt, rd);
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
			rt = regModel.getRegister(rt); 
			rs = regModel.getRegister(rs);
			
			regModel.setRegister(rs&rt, rd);
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
			rt = regModel.getRegister(rt);
			rs = regModel.getRegister(rs);
			
			regModel.setRegister(rs|rt, rd);
		}
		
		/**
		 * Builds the code; Stores the instructions in the Main Memory Model, at the bottom of
		 * the text segment. 
		 * @param instructions - array of instructions as ints. Needs to be converted to binary to decode
		 * the instruction.
		 */
		public void build(int[] instructions) {
			int pc = memoryModel.getPc();
			
			// copy of program counter is incremented every iteration to store instruction at the next
			// address
			for(int i = 0; i<instructions.length; i++, pc++) {
				memoryModel.storeMemory(pc,instructions[i]);
			}
			
		}
		
		/**
		 * Begins compilation of the assembly program by fetching the instructions from memory.
		 * Once it has the instruction and is decoded by the CodeParser and the actual instruction
		 * execution is delegated to a method in this class. Either R,I, or J type methods.
		 */
		public void compile() {
			
			isExecuting = true;
			int lastInstrAddress = memoryModel.getLastInstrAddress();
			
			// Loop and constraints of program execution
			while(isExecuting && memoryModel.getPc() < lastInstrAddress) {
				
//				int pc = memoryModel.getPc();
//				// 5, 5, 12, 25, 2
//				int[] instruction = CodeParser.parseInstruction(memoryModel.loadMemory(pc));
//				
//				switch(instruction[0]) {
//				
//				case R: executeR(instruction)
//					memoryModel.setPc(memoryModel.getPc()+1);
//				case I:
//					memoryModel.setPc(memoryModel.getPc()+1);
//				case J
//					memoryModel.setPc(memoryModel.getPc()+1);
//				
				
				}
			}
		
			
		}
		
		
	}
