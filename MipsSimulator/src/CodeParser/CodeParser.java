package CodeParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.lang.String;

public class CodeParser {
	
	private static CodeParser codeParser;

	private static HashMap<String,Integer> hashMap_op;
	private static HashMap<String,Integer> hashMap_funct;
	private static HashMap<String,Integer> hashMap_loops;
	
	public static CodeParser getInstance() {
		
		if(codeParser == null) {
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
		String tempString = reg.substring(2);

		//finds starting index for registers based on letter
		switch(reg.charAt(1))
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
		        	System.out.println("Case A executing:   " + tempString);
		            if (reg.charAt(2)== 't')
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
		            if (reg.charAt(2) == 'p')
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


	public void HashMap_Op()
	{
		//Defining HashMap
		hashMap_op = new HashMap<>();
		
		//Intializing HashMap Key/Values
		hashMap_op.put("J", 2);
		hashMap_op.put("BEQ", 4);
		hashMap_op.put("ADD", 0);
		hashMap_op.put("ADDI", 8);
		hashMap_op.put("SUB", 9);
		hashMap_op.put("SW", 43); // 32 + 11 = 43 (0x2b)
		hashMap_op.put("LW", 35); // 32 + 3 = 35	(0x23)
		hashMap_op.put("SLL", 0);
		hashMap_op.put("SRL", 0);
		hashMap_op.put("MUL", 28); //16 + 12 = 28 (0x1c)
		hashMap_op.put("AND", 0);	
	}
	
	// Hashmap for Funct Values 
		
	public void HashMap_Funct()
	{
		hashMap_funct = new HashMap<>();
		hashMap_funct.put("ADD", 0x20);
		hashMap_funct.put("SUB", 0x22);
		hashMap_funct.put("SLL", 0);
		hashMap_funct.put("SRL", 2);
		hashMap_funct.put("AND", 0x24);
		hashMap_funct.put("OR", 0x25);
	}	
	
	//Identifies labels from assembly file and sets a Hashmap with <Label,memIndex>
	public static void parseLabels() throws FileNotFoundException
	{
		Scanner file = new Scanner (new File ("C:\\eclipse\\EclipseWorkspace\\Parsing_MIPS\\assembly_demo.txt"));
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
	
	public static Queue<Integer> parseCode() throws FileNotFoundException
	{
		
		Scanner file = new Scanner (new File ("C:\\eclipse\\EclipseWorkspace\\Parsing_MIPS\\assembly_demo.txt"));
			
		Queue<Integer> instructions = new LinkedList<>(); 
		
		//Binary Strings for final conversion
		String BinaryOP;
		String BinaryRS;
		String BinaryRT;
		String BinaryRD;
		String BinarySHAMT;
		String BinaryFUNCT;
		String BinaryIM;
		String BinaryLABEL;
		
		int memIndex = 0;
		while (file.hasNext())	
		{		
			StringBuilder FullBinary = new StringBuilder();	
			//Printing string variables read from text file "assembly_demo"
			String lineInstruction = file.nextLine();
			
			//Psuedo Address Identifier
			String temp = lineInstruction.split(":")[0];
			if (temp.length() != lineInstruction.length())
			{
				lineInstruction = lineInstruction.split(":")[1];
			}
			
			String[] split = lineInstruction.split("^[A-Za-z0-9]+");
			String opString = split[0];
			int tempInstruction[] = {0,0,0,0,0,0};
			
			//HashMap<token[i],Integer> hashMap = new HashMap;
			int opcode = hashMap_op.get(opString);
			
			//Choosing between R, I, J type
				
			//R-type Parser
			if (opcode == 0 || opcode == 28)
			{	
				switch (opString)
				{
					case "ADD":
					case "SUB":
					case "AND":
					case "OR":
					case "MUL":
					{
						tempInstruction[0] = 0;
						tempInstruction[1] = getRegFromStr(split[2]);
						tempInstruction[2] = getRegFromStr(split[3]);
						tempInstruction[3] = getRegFromStr(split[1]);
						tempInstruction[4] = 0;
						tempInstruction[5] = hashMap_funct.get(opString);
					}
					break;
						
					case "SLL" :
					case "SRL" :
					{
						tempInstruction[0] = 0;
						tempInstruction[1] = 0;
						tempInstruction[2] = getRegFromStr(split[2]);
						tempInstruction[3] = getRegFromStr(split[1]);
						tempInstruction[4] = Integer.valueOf(split[3]);
						tempInstruction[5] = hashMap_funct.get(opString);
					}
				}
				
				BinaryOP = Integer.toBinaryString(tempInstruction[0]);
				BinaryRS = Integer.toBinaryString(tempInstruction[1]);
				BinaryRT = Integer.toBinaryString(tempInstruction[2]);
				BinaryRD = Integer.toBinaryString(tempInstruction[3]);
				BinarySHAMT = Integer.toBinaryString(tempInstruction[4]);
				BinaryFUNCT = Integer.toBinaryString(tempInstruction[5]);
				
				BinaryOP = appendBit(BinaryOP, 6);
				BinaryFUNCT = appendBit(BinaryFUNCT, 6);
				BinaryRS = appendBit(BinaryRS, 5);
				BinaryRT = appendBit(BinaryRT, 5);
				BinaryRD = appendBit(BinaryRD, 5);
				BinarySHAMT = appendBit(BinarySHAMT, 5);
					
				FullBinary.append(BinaryOP);
				FullBinary.append(BinaryRS);
				FullBinary.append(BinaryRT);
				FullBinary.append(BinaryRD);
				FullBinary.append(BinarySHAMT);
				FullBinary.append(BinaryFUNCT);
				
				String instructionBinary = FullBinary.toString();
				
				instructions.add(Integer.parseInt(instructionBinary, 2));
				
			}
			
			//J-type Parser
			else if (opcode == 2 || opcode == 3)
			{
				int tempInstruct_J[] = {0,0};
				tempInstruct_J[0] = opcode;
				tempInstruct_J[1] = hashMap_loops.get(temp);
				
				BinaryOP = Integer.toBinaryString(tempInstruct_J[0]);
				BinaryRS = Integer.toBinaryString(tempInstruct_J[1]);
				
				BinaryOP = appendBit(BinaryOP, 6);
				BinaryRS = appendBit(BinaryLABEL, 26);
				
				FullBinary.append(BinaryOP);
				FullBinary.append(BinaryLABEL);
				
				String instructionBinary = FullBinary.toString();
				instructions.add(Integer.parseInt(instructionBinary, 2));
			}
				
			//I-type Parser
			else if (opcode == 1 || opcode > 3 && opcode < 28 || opcode > 28 && opcode < 63) //Excluding MUL OPCODE (28) 
			{
				
				int tempInstruct_I[] = {0,0,0,0};
				
				tempInstruct_I[0] = opcode;
				tempInstruct_I[1] = getRegFromStr(split[1]);
				tempInstruct_I[2] = getRegFromStr(split[2]);
				tempInstruct_I[3] = Integer.parseInt(split[3]);
				
				BinaryOP = Integer.toBinaryString(tempInstruct_I[0]);
				BinaryRS = Integer.toBinaryString(tempInstruct_I[1]);
				BinaryRT = Integer.toBinaryString(tempInstruct_I[2]);
				BinaryIM = Integer.toBinaryString(tempInstruct_I[3]);
				
				BinaryOP = appendBit(BinaryOP, 6);
				BinaryRS = appendBit(BinaryRS, 5);
				BinaryRT = appendBit(BinaryRT, 5);
				BinaryIM = appendBit(BinaryIM, 16);
					
				FullBinary.append(BinaryOP);
				FullBinary.append(BinaryRS);
				FullBinary.append(BinaryRT);
				FullBinary.append(BinaryIM);
	
				String instructionBinary = FullBinary.toString();
				instructions.add(Integer.parseInt(instructionBinary, 2));
				
				
			}		
		}
		memIndex++;
	}


	int[] convertBin (int intNUM)
	{
		
		String fullBinary = Integer.toBinaryString(intNUM);
		String binaryOP = fullBinary.substring(0, 5);
		int opcode = Integer.parseInt(binaryOP, 2);
		
		if (opcode == 0)
		{
			String binArray[] = {"-1","-1","-1","-1","-1","-1"};
			int intArray[] = {0, 0, 0, 0, 0, 0}; 
			binArray[0] = binaryOP;
			binArray[1] = fullBinary.substring(6, 10);
			binArray[2] = fullBinary.substring(11, 15);
			binArray[3] = fullBinary.substring(16, 20);
			binArray[4] = fullBinary.substring(21, 25);
			binArray[5] = fullBinary.substring(26, 31);
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
			binArray[1] = fullBinary.substring(6, 31);
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
			binArray[1] = fullBinary.substring(6, 10);
			binArray[2] = fullBinary.substring(11, 15);
			binArray[3] = fullBinary.substring(16, 31);
			for (int i = 0; i < intArray.length; i++)
			{
				intArray[i] = Integer.parseInt(binArray[i], 2);
			}
			return new Instruction(Instruction.JTYPE, intArray);
		}
	}
}
