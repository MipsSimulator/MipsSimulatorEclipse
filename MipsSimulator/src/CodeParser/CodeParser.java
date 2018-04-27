import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.lang.String;
public class CodeParser {

private static HashMap<String,Integer> hashMap_op;
private static HashMap<String,Integer> hashMap_funct;

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

        // Temp data either starts at 8 or 24 depending on digit
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
		hashMap_op.put("SW", 0x2b);
		hashMap_op.put("LW", 0x23);
		hashMap_op.put("SLL", 0);
		hashMap_op.put("SRL", 0);
		hashMap_op.put("MUL", 0x1c);
		hashMap_op.put("AND", 0);	
	}
	
	// Hashmap for Funct Values of R-type
	
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
	
	
	
	
	
	public static int CodeParser()
	{
		Scanner file = new Scanner (new File ("C:\\eclipse\\EclipseWorkspace\\Parsing_MIPS\\assembly_demo.txt"));
		
		String[] token = {"op", "rs", "rt", "rd"};
		
		while (file.hasNext())
		{
			//Binary Strings for final conversion
			String BinaryOP;
			String BinaryRS;
			String BinaryRT;
			String BinaryRD;
			String BinarySHAMT;
			String BinaryFUNCT;
			
			StringBuilder FullBinary = new StringBuilder();
			
			//Printing string variables read from text file "assembly_demo"
			String[] split = file.nextLine().split(", | ");
			String opString = split[0];
			int tempInstruction[] = {0,0,0,0,0,0};
			
			for (int i = 0; i < split.length; i++)
			{
				System.out.print(token[i]); 
				System.out.print(":  ");
				System.out.println(split[i]);
			}
			
			//random comment
			
			//HashMap<token[i],Integer> hashMap = new HashMap;
			int opcode = hashMap_op.get(opString);
			
			//Choosing between R, I, J type
			
			//R-type Parser
			if (opcode == 0)
			{	
				switch (opString)
				{
				
					case "ADD":
					case "SUB":
					case "AND":
					case "OR":
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
			}
			
			//J-type Parser
			else if (opcode == 2 || opcode == 3)
			{
					
			}
			
			//I-type Parser
			else if (opcode == 1 || opcode > 3 && opcode < 63)
			{
				
			}
			
			//Structuring the binary values of each bitfield
			//within the instruction
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
			
			System.out.println();
			
		}
	}
}
