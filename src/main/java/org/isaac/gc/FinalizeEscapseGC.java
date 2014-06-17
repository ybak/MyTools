package org.isaac.gc;


public class FinalizeEscapseGC {

    public static FinalizeEscapseGC SAVE_HOOK= null;
    
    public void isAlive(){
        System.out.println("yes, i am still alive.");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("finalize method executed!");
        FinalizeEscapseGC.SAVE_HOOK = this;
    }
    
    public static void main(String[] args) throws Throwable {
        SAVE_HOOK = new FinalizeEscapseGC();

        SAVE_HOOK = null;
        System.gc();
        Thread.sleep(500);
        if(SAVE_HOOK !=null){
            SAVE_HOOK.isAlive();
        }else{
            System.out.println("i am dead.");
        }

        //second time
        SAVE_HOOK = null;
        System.gc();
        Thread.sleep(500);
        if(SAVE_HOOK !=null){
            SAVE_HOOK.isAlive();
        }else{
            System.out.println("i am dead.");
        }
    }
    
}
