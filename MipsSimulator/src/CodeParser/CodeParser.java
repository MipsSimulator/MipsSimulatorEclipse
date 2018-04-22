import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class CodeParser {

private HashMap<String,Integer> hashMap_op;
private HashMap<String,Integer> hashMap_funct;

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
	}
	
	
	
	
	
	public void CodeParser ()
	{
		Scanner file = new Scanner (new File ("C:\\eclipse\\EclipseWorkspace\\Parsing_MIPS\\assembly_demo.txt"));
		
		String[] token = {"op", "rs", "rt", "rd"};
		
		while (file.hasNext())
		{
			//Printing string variables read from text file "assembly_demo"
			String[] split = file.nextLine().split(", | ");
			for (int i = 0; i < split.length; i++)
			{
				System.out.print(token[i]);
				System.out.print(":  ");
				System.out.println(split[i]);
			}
			
			//HashMap<token[i],Integer> hashMap = new HashMap;
			int opcode = hashMap.get(split[0]);
			
			//Choosing between R, I, J type
			
			//R-type Parser
			if (opcode == 0)
			{	
				
			}
			
			//J-type Parser
			else if (opcode == 2 || opcode == 3)
			{
					
			}
			
			//I-type Parser
			else if (opcode == 1 || opcode > 3 && opcode < 63)
			{
				
			}
			
			
			System.out.println();
			
		}
	}
}
