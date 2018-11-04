package diary.beautiful.sync;


//采用双重锁/双重检验校验（DCL，即 double-checked locking）的单例类
//用于日记的上传和下载
public class SyncEvent {
    private volatile  static SyncEvent syncEvent;
    private SyncEvent (){};
    public static SyncEvent getSyncEvent(){
        if(syncEvent == null){
            synchronized (SyncEvent.class){
                if(syncEvent == null){
                    syncEvent = new SyncEvent();
                }
            }
        }
        return syncEvent;
    }
}
