package Model;

/**
* Model in the MVC architecture
* The MemoryModel holds the state of the Assembly Simulator
*/
public class MainMemoryModel {

   /**
    * The single instance of the MemoryModel
    */
   private MainMemoryModel memoryModel;
   
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
    * Stack Pointer. Holds index (address) to the stack.
    */
   private int sp;
   /**
    * Frame Pointer Holds index (address) to the frame.
    */
   private int fp;

   /**
    * Global pointer that points to the top of the static data segment for easy access
    * to that segment.
    */
   private int gp;

   /**
    * Program counter. Address is initially set to the bottom of the text segment. Keeps track
    * of the address to the current instruction in the Main Memory.
    */
   private int pc;

   /**
    * Initializes the memory model. Sets the stack pointer, frame pointer, and
    * global pointer to their respective locations in memory.
    */
   private MainMemoryModel() {

       memoryModel = new MainMemoryModel();
       mainMemory = new int[MEMORY_SIZE];

       this.sp = MEMORY_SIZE-1;
       this.gp = STATIC_TOP_ADDRESS;
       this.pc = TEXT_BOTTOM_ADDRESS;

   }


   /**
    * Creates an instance of MemoryModel if one does not exist. If one
    * does it exist, that instance is returned. This method implements the
    * Singleton Design Pattern functionality. (Only 1 instance of the Memory Model is allowed
    * to exist at a time)
    * @return
    */
   public MainMemoryModel getInstance(){
       if(memoryModel == null){
           memoryModel = new MainMemoryModel();
           return memoryModel;
       }

       return memoryModel;
   }
   
   
   /**
    * 
    * @return stack pointer
    */
   public int getSp() { return this.sp;}
   /**
    * 
    * @return frame pointer
    */
   public int getFp() { return this.fp;}
   /**
    * 
    * @return global pointer
    */
   public int getGp() { return this.gp;}
   /**
    * 
    * @return program counter
    */
   public int getPc() { return this.pc;}
   
   
   /**
    * Updates the program counter
    * @param memoryAddress - The address the PC should be set to.
    */
   public void setPc(int memoryAddress) { this.pc = memoryAddress ;}
   
   /**
    * Updates the frame pointer
    * @param memoryAddress
    */
   public void setFp(int memoryAddress) {this.fp = memoryAddress; }
   

   /**
    * Updates the global pointer
    * @param memoryAddress
    */
   public void setGp(int memoryAddress) {this.gp = memoryAddress; }
   
   
   /**
    * Updates the stack pointer
    * @param memoryAddress
    */
   public void setSp(int memoryAddress) {this.gp = memoryAddress; }
   
   
   
   
   
   /**
    * [Read Only] Used to read main memory at the given memory address.
    * This can be used as load word.
    * @param memoryAddress
    * @return
    */
   public int getMemory(int memoryAddress) { return mainMemory[memoryAddress]; }
   
   
   /**
    * [Write Only] used to write to memory at the given address.
    * This can be used as store word.
    * @param memoryAddress
    * @return
    */
   public int setMemory(int memoryAddress) { return mainMemory[memoryAddress]; }
   

}