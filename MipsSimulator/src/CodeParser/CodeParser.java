package CodeParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import Executor.*;
import java.lang.String;

public class CodeParser 
{	
	private static CodeParser codeParser;
	private static HashMap<String,Integer> hashMap_op;
	private static HashMap<String,Integer> hashMap_funct;
	private static HashMap<String,Integer> hashMap_loops;
	
	private CodeParser() 
	{
		HashMap_Op();
		HashMap_Funct();
		hashMap_loops = new HashMap<>();
	}
	
	public static CodeParser getInstance() 
	{	
		if(codeParser == null) 
		{
			codeParser = new CodeParser();
		}
		return codeParser;
	}

	//Setting up leading 0's for bit field
	public static String appendBit(String bitField, int targetLength) 
	{
		StringBuilder tempBinary = new StringBuilder();
		for (int i = bitField.length(); i < targetLength; i++)
		{
			tempBinary.append('0');
		}
		tempBinary.append(bitField);
		bitField = tempBinary.toString();
		return bitField;		
	}



	//Converts string value of register to corresponding int value
	public static int getRegFromStr(String reg)
	{
		int val = -1;

		// make sure no trailing/leading white space
		reg = reg.trim();
		String tempString = reg.substring(1);

		//finds starting index for registers based on letter
		switch(reg.charAt(0))
		{
	        	// Zero Register
	        case 'z':
	            val = 0;
	            break;
	
	        // Return values
	        case 'v':
	            val = 2 + Integer.valueOf(tempString);
	            break;
	
	        // at register or Function Argument
		        case 'a':
		            if (reg.charAt(1)== 't')
		                val = 1;
		            else
		                val = 4 + Integer.parseInt(tempString);
		            break;
		
		        //Temp data either starts at 8 or 24 depending on digit
		        case 't':
		            if (Integer.valueOf(tempString) > 7)
		                val = 24 + Integer.valueOf(tempString);
		            else
		                val = 8 + Integer.valueOf(tempString);;
		            break;
	
		        // Either Stack Pointer of Saved Registers
		        case 's':
		            if (reg.charAt(1) == 'p')
		                val = 29;
		            else
		                val = 16 + Integer.valueOf(tempString);
		            break;
		            
		        //K0 and K1
		        case 'k':
		        	val = 26 + Integer.valueOf(tempString);
		        	break;
		        // Global area pointer
		        case 'g':
		            val = 28;
		            break;
		
		        // Frame Pointer
		        case 'f':
		            val = 30;
		            break;
		
		        // Return address
		        case 'r':
		            val = 31;
		            break;
		}
    return val;
	}

	//****************************************
	//Hashmap for Opcode Values <opString, opcode>
	//****************************************
	public void HashMap_Op()
	{
		hashMap_op = new HashMap<>();
		hashMap_op.put("j", 2);
		hashMap_op.put("beq", 4);
		hashMap_op.put("add", 0);
		hashMap_op.put("addi", 8);
		hashMap_op.put("sub", 0);
		hashMap_op.put("sw", 43); // 32 + 11 = 43 (0x2b)
		hashMap_op.put("lw", 35); // 32 + 3 = 35	(0x23)
		hashMap_op.put("sll", 0);
		hashMap_op.put("srl", 0);
		hashMap_op.put("mul", 28); //16 + 12 = 28 (0x1c)
		hashMap_op.put("and", 0);	
	}
	
	//****************************************
	//Hashmap for Funct Values <opString, funct>
	//****************************************
	public void HashMap_Funct()
	{
		hashMap_funct = new HashMap<>();
		hashMap_funct.put("add", 0x20);
		hashMap_funct.put("sub", 0x22);
		hashMap_funct.put("sll", 0);
		hashMap_funct.put("srl", 2);
		hashMap_funct.put("and", 0x24);
		hashMap_funct.put("or", 0x25);
	}	
	
	//*****************************************************
	//parseLabels: Identifies labels from assembly file and 
	// sets a hashmap with <Label,memIndex>
	//*****************************************************
	public static void parseLabels() throws FileNotFoundException
	{
		Scanner file = new Scanner (new File ("C:\\Users\\Aaron\\Documents\\GitHub\\MipsSimulatorEclipse-Development\\MipsSimulator\\assembly_demo.txt"));
		int memIndex = 0;
		while (file.hasNext())
		{
			String lineInstruction = file.nextLine();
			String temp = lineInstruction.split(":")[0];
			if (temp.length() != lineInstruction.length())
			{
				hashMap_loops.put(temp, memIndex);
				
			}
			memIndex++;
		}
		file.close();
	}
	
	
	//**************************************
	//parseCode returns a Queue of type Long
	//Queue contains the decimal value of the binary number 
	//that corresponds to each line of instruction
	//****************************************
	public Queue<Long> parseCode() throws FileNotFoundException
	{
		int memIndex=0;
		
		Scanner file = new Scanner (new File ("C:\\Users\\Aaron\\Documents\\GitHub\\MipsSimulatorEclipse-Development\\MipsSimulator\\assembly_demo.txt"));
			
		Queue<Long> instructions = new LinkedList<>(); 
		
		//Binary Strings for final conversion
		String BinaryOP;
		String BinaryRS;
		String BinaryRT;
		String BinaryRD;
		String BinarySHAMT;
		String BinaryFUNCT;
		String BinaryIM;
		String BinaryLABEL;
		
		while (file.hasNext())	
		{		
			boolean invalidLength = false;
			
			StringBuilder FullBinary = new StringBuilder();	
			//Printing string variables read from text file "assembly_demo"
			String lineInstruction = file.nextLine();
			
			//Pseudo Address Identifier
			String temp = lineInstruction.split(":")[0];		
			if (temp.length() != lineInstruction.length())
			{
				lineInstruction = lineInstruction.split(":")[1];
			}
		
			//Splitting tab from the beginning of each instruction if applicable
			String tabSplit = lineInstruction.split("\t+")[0];
			if (tabSplit.length() != lineInstruction.length())
			{
				lineInstruction = lineInstruction.split("\t")[1];
			}
			
			//Splitting the instruction into rt/rd/etc....
			String[] split = lineInstruction.split("[^A-Za-z0-9]+");
			
			String opString = split[0];
			int opcode = hashMap_op.get(opString);
				
			//R-type Parser
			if (opcode == 0 || opcode == 28)
			{			
				//Error Handling for invalid instruction length of R-type
				if (split.length != 4)
				{
					invalidLength = true;
				}
				//Error Handling for Register Fields
				if (split[2].length()!=2)
				{
					System.out.print("Invalid RT at line: ");
					System.out.print(memIndex);
				}
				if (split[1].length()!=2)
				{
					System.out.print("Invalid RS at line: ");
					System.out.print(memIndex);
				}
				if (split[3].length()!=2)
				{
					System.out.print("Invalid RD at line: ");
					System.out.print(memIndex);
				}
				
				int tempInstruction[] = {0,0,0,0,0,0};
				switch (opString)
				{
				
					case "add":
					case "sub":
					case "and":
					case "or":
					case "mul":
					{
						tempInstruction[0] = 0;
						tempInstruction[1] = getRegFromStr(split[2]);
						tempInstruction[2] = getRegFromStr(split[3]);
						tempInstruction[3] = getRegFromStr(split[1]);
						tempInstruction[4] = 0;
						tempInstruction[5] = hashMap_funct.get(opString);
					}
					break;
						
					case "sll" :
					case "srl" :
					{
						tempInstruction[0] = 0;
						tempInstruction[1] = 0;
						tempInstruction[2] = getRegFromStr(split[2]);
						tempInstruction[3] = getRegFromStr(split[1]);
						tempInstruction[4] = Integer.valueOf(split[3]);
						tempInstruction[5] = hashMap_funct.get(opString);
					}
				}
				//Converting Integer Values to Binary String
				BinaryOP = Integer.toBinaryString(tempInstruction[0]);
				BinaryRS = Integer.toBinaryString(tempInstruction[1]);
				BinaryRT = Integer.toBinaryString(tempInstruction[2]);
				BinaryRD = Integer.toBinaryString(tempInstruction[3]);
				BinarySHAMT = Integer.toBinaryString(tempInstruction[4]);
				BinaryFUNCT = Integer.toBinaryString(tempInstruction[5]);
				
				//Appending Binary String to match the corresponding bitfield
				BinaryOP = appendBit(BinaryOP, 6);
				BinaryFUNCT = appendBit(BinaryFUNCT, 6);
				BinaryRS = appendBit(BinaryRS, 5);
				BinaryRT = appendBit(BinaryRT, 5);
				BinaryRD = appendBit(BinaryRD, 5);
				BinarySHAMT = appendBit(BinarySHAMT, 5);
				
				//Combining the individual bit fields into a 32bit binary
				FullBinary.append(BinaryOP);
				FullBinary.append(BinaryRS);
				FullBinary.append(BinaryRT);
				FullBinary.append(BinaryRD);
				FullBinary.append(BinarySHAMT);
				FullBinary.append(BinaryFUNCT);
				
				//Converting 32bit Binary to Long
				String instructionBinary = FullBinary.toString();				
				instructions.add(Long.parseLong(instructionBinary, 2));
			}
			
			//J-type Parser
			else if (opcode == 2 || opcode == 3)
			{
				//Error Handling for invalid instruction length of I-type
				if (split.length != 2)
				{
					invalidLength = true;
				}
				
				int tempInstruct_J[] = {0,0};
				tempInstruct_J[0] = opcode;
				tempInstruct_J[1] = hashMap_loops.get(split[1]);
				
				//Converting to total integer value of the instruction
				BinaryOP = Integer.toBinaryString(tempInstruct_J[0]);
				BinaryLABEL = Integer.toBinaryString(tempInstruct_J[1]);
				BinaryOP = appendBit(BinaryOP, 6);
				BinaryLABEL = appendBit(BinaryLABEL, 26);
				FullBinary.append(BinaryOP);
				FullBinary.append(BinaryLABEL);
				String instructionBinary = FullBinary.toString();
				instructions.add(Long.parseLong(instructionBinary, 2));

			}
				
			//I-type Parser
			else if (opcode == 1 || (opcode > 3 && opcode < 28) || (opcode > 28 && opcode < 63)) //Excluding MUL OPCODE (28) 
			{
				int tempInstruct_I[] = {0,0,0,0};
				
				//Handling BGEZ / BLTZ
				//More I-type instructions with with 2 arguements can go here.
				if (split.length == 3)
				{
					switch (opString)
					{
						case "bgez":
						{
							tempInstruct_I[0] = opcode;
							tempInstruct_I[1] = getRegFromStr(split[1);
							tempInstruct_I[2] = 1;
							tempInstruct_I[3] = hashMap_loops.get(split[2]);
							break;
						}
						case "bltz":
						{
							tempInstruct_I[0] = opcode;
							tempInstruct_I[1] = getRegFromStr(split[1);
							tempInstruct_I[2] = 0;
							tempInstruct_I[3] = hashMap_loops.get(split[2]);
							break;	
						}
					}
				}
				
				//Most I-type instructions will get parsed here.
				else if (split.length == 4)
				{
					BinaryOP = Integer.toBinaryString(tempInstruct_I[0]);
					BinaryRS = Integer.toBinaryString(tempInstruct_I[1]);
					BinaryRT = Integer.toBinaryString(tempInstruct_I[2]);
					BinaryIM = Integer.toBinaryString(tempInstruct_I[3]);
				}
				
				//Input Validation
				else {
					invalidLength = true;
				}
				
				//Converting to total integer value of instruction
				BinaryOP = appendBit(BinaryOP, 6);
				BinaryRS = appendBit(BinaryRS, 5);
				BinaryRT = appendBit(BinaryRT, 5);
				BinaryIM = appendBit(BinaryIM, 16);	
				FullBinary.append(BinaryOP);
				FullBinary.append(BinaryRS);
				FullBinary.append(BinaryRT);
				FullBinary.append(BinaryIM);
				String instructionBinary = FullBinary.toString();
				instructions.add(Long.parseLong(instructionBinary, 2));				
			}
			
			//Input Validation
			if (invalidLength)
			{
				System.out.print("Invalid instruction length encountered at line: ");
				System.out.println(memIndex);
			}
			memIndex++;
		}
		
		file.close();
		return instructions;
	
	}

	//********************************************************
	//parseInstruction accepts "long" input and
	//converts to integer array to represent corresponding
	//bit fields for each type of instruction.
	//intNUM = decimal value that represents a 32bit instruction
	//**********************************************************
	public Instruction parseInstruction (long intNUM)
	{
		
		String fullBinary = Long.toBinaryString(intNUM);
		fullBinary = appendBit(fullBinary, 32);
		String binaryOP = fullBinary.substring(0, 6);
		int opcode = Integer.parseInt(binaryOP, 2);
		
		if (opcode == 0)
		{
			String binArray[] = {"-1","-1","-1","-1","-1","-1"};
			int intArray[] = {0, 0, 0, 0, 0, 0}; 
			binArray[0] = binaryOP;
			binArray[1] = fullBinary.substring(6, 11);
			binArray[2] = fullBinary.substring(11, 16);
			binArray[3] = fullBinary.substring(16, 21);
			binArray[4] = fullBinary.substring(21, 26);
			binArray[5] = fullBinary.substring(26, 32);
			for (int i = 0; i < intArray.length; i++)
			{
				intArray[i] = Integer.parseInt(binArray[i], 2);
			}
			return new Instruction(Instruction.RTYPE, intArray);
		}
		
		else if (opcode == 2 || opcode == 3)
		{
			String binArray[] = {"-1","-1","-1","-1","-1","-1"};
			int intArray[] = {0, 0, 0, 0, 0, 0}; 
			binArray[0] = binaryOP;
			binArray[1] = fullBinary.substring(6, 32);
			for (int i = 0; i < intArray.length; i++)
			{
				intArray[i] = Integer.parseInt(binArray[i], 2);
			}
			return new Instruction(Instruction.RTYPE, intArray);
		}
		
		
		else if (opcode == 1 || opcode > 3 && opcode < 63)
		{
			String binArray[] = {"-1","-1","-1","-1"};
			int intArray[] = {0, 0, 0, 0}; 
			binArray[0] = binaryOP;
			binArray[1] = fullBinary.substring(6, 11);
			binArray[2] = fullBinary.substring(11, 16);
			binArray[3] = fullBinary.substring(16, 32);
			for (int i = 0; i < intArray.length; i++)
			{
				intArray[i] = Integer.parseInt(binArray[i], 2);
			}
			return new Instruction(Instruction.JTYPE, intArray);
		}
		
		// TODO: Add handling for when op-code isn't matched.
		return null;
	}
}
