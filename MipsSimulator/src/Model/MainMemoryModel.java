package Model;

/**
* Model in the MVC architecture
* The Main Memory Model holds the state of the Assembly Simulator
*/
public class MainMemoryModel extends AbstractModel {

   /**
    * The single instance of the MemoryModel
    */
   private static MainMemoryModel memoryModel;
   
   /**
    * The main memory. Access is allowed only at every word, which is 32 bits.
    * This is why the data type is a byte.
    */
   private int[] mainMemory;

   /**
    * Addressable size of Main Memory. Main memory is 8mb by default.
    * Addressing is only allowed to every word, which is 4 bytes.
    * 1024Bytes*1024 = 1Mb*8 = 8mb
    */
   public static final int MEMORY_SIZE = 1024*1024*8;

   /**
    * Top address of the Static Segment. 5.5Mb, or 1024*1024*5.5 = 5767168
    */
   public static final int STATIC_TOP_ADDRESS = 5767168;

   /**
    * Size of the static segment is 1mb. 5767168 - 1mb = 4718592
    * or 5767168 - 1024*1024 = 4718592
    */
   public static final int STATIC_BOTTOM_ADDRESS = 4718592;

   /**
    * Size of the text segment is 4mb. 4718592 - 1024*1024*4 or 4mb = 524288
    */
   public static final int TEXT_BOTTOM_ADDRESS = 524288;

   /**
    * When the program gets built, this will be set to the address of the last instruction in memory.
    */
   private int lastInstruction;
   
   
   /**
    * Initializes the memory model. Sets the stack pointer, frame pointer, and
    * global pointer to their respective locations in memory.
    */
   private MainMemoryModel() {

       memoryModel = new MainMemoryModel();
       mainMemory = new int[MEMORY_SIZE];

   }


   /**
    * Creates an instance of MemoryModel if one does not exist. If one
    * does it exist, that instance is returned. This method implements the
    * Singleton Design Pattern functionality. (Only 1 instance of the Memory Model is allowed
    * to exist at a time)
    * @return
    */
   public static MainMemoryModel getInstance(){
       if(memoryModel == null){
           memoryModel = new MainMemoryModel();
           return memoryModel;
       }

       return memoryModel;
   }
   
   
   
   
   /**
    * Store the address of the last location in memory. This occurs when the program gets built.
    * @param memoryAddress
    */
   public void setLastInstrAddress(int memoryAddress){ this.lastInstruction = memoryAddress;}
   
   
   /**
    * Gets the address of the last instruction in memory.
    * @param memoryAddress - address of the last instruction
    * @return - last instruction in memory.
    */
   public int getLastInstrAddress() { return this.lastInstruction;}
   
   
   
   
   
   /**
    * [Read Only] Used to read main memory at the given memory address.
    * This can be used as load word.
    * @param memoryAddress
    * @return data at that memory address in the form of an int.
    */
   public int loadMemory(int memoryAddress) { return mainMemory[memoryAddress]; }
   
   
   /**
    * [Write Only] used to write to memory at the given address.
    * This can be used as store word.
    * @param memoryAddress - 32bit word addressable
    * @param value - value to be stored at the given memoryAddress
    */
   public void storeMemory(int memoryAddress, int value) { mainMemory[memoryAddress] = value; }
   

}