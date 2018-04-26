package Model;

/**
 * Models MIP's 32 registers. Each register is 32 bits in width.
 * @author Ben
 *
 */
public class RegisterModel extends AbstractModel{
	
	/**
	 * The single RegisterModel instance. (Singleton)
	 */
	private static RegisterModel registerModel;
	
	/**
	 * int array that is initialized to 32 to hold the MIPS 32 registers.
	 */
	private int[] registerFile;
	
	private RegisterModel() {
		registerFile = new int[32];
		for(int i = 0; i<32; i++)
			registerFile[i] = 0;
	}
	
	
	/**
	 * Singleton method that returns the single instance of RegisterModel
	 * @return RegisterModel singleton
	 */
	public static RegisterModel getInstance() {
		if(registerModel == null) {
			registerModel = new RegisterModel();
		}
		return registerModel;
	}
	
	
	/**
	 * Gets the value in the register at the specified register File.
	 * @param registerIndex
	 * @return
	 */
	public int getRegister(int registerIndex) {
		
		return registerFile[registerIndex];
	}
	
	/**
	 * Updates the register at the given index to the registerFile. Updates it with the data
	 * that is passed in.
	 * @param data
	 * @param registerIndex
	 */
	public void setRegister(int data, int registerIndex) {
		registerFile[registerIndex] = data;
	}
	
	
	
	
	
	
	
	
	
	

	
	

}
