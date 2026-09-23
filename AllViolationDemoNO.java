import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

interface Releasable {
    void release();
}

public class AllViolationDemoNO {
    private final Lock lock = new ReentrantLock();
    private final Lock lock2 = new ReentrantLock();
	
	private final Lock lockOuter = new ReentrantLock();
	private final Lock lockInter = new ReentrantLock();
	
	 
    private final Object syncLock = new Object();
    private final ReentrantLock reentrantLock = new ReentrantLock();
    private final Object wrongLock = new Object();
	

    public static void main(String[] args) {
        AllViolationDemo demo = new AllViolationDemo();

        demo.violationDateFormat1();
        demo.violationDateFormat2();

        demo.violationAtomicEquals1();
        demo.violationAtomicEquals2();

       

        demo.violationLockNoUnlock1(true);
        demo.violationLockNoUnlock2();

        demo.violationReleasableNoRelease1();
        demo.violationReleasableNoRelease2();

        demo.violationSyncGetClass1();
        demo.violationSyncGetClass2();
    }

  
    public void violationDateFormat1() {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        String dateText = sdf.format(new Date());
        System.out.println(dateText);
    }

    public void violationDateFormat2() {
        String pattern = "YYYY" + "-MM-dd HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        System.out.println(sdf.format(new Date()));
    }

    
   
	public void violationAtomicEquals1() {
        AtomicInteger a1 = new AtomicInteger(50);
        AtomicInteger a2 = new AtomicInteger(50);
        boolean flag = a1.equals(a2);
        System.out.println(flag);
    }
	
    public void violationAtomicEquals2() {
        AtomicLong atomicInt1 = new AtomicLong(1000);
        Integer normalInt = 10;
        boolean badMix = atomicInt1.equals(normalInt);
        System.out.println("atomicInt1.equals(Integer.valueOf(10)) = " + badMix); 
    }
	
	public void violationAtomicEquals3() {
        AtomicInteger atomicInt1 = new AtomicInteger(10);
        boolean bad1 = atomicInt1.equals(10);
        System.out.println("atomicInt1.equals(10) = " + bad1); 
    }
	
	public void violationAtomicEquals4() {
       AtomicInteger atomicInt2 = new AtomicInteger(10);
       boolean bad2 = atomicInt1.equals(atomicInt2);
       System.out.println("atomicInt1.equals(atomicInt2) = " + bad2);
    }
	
	public void violationAtomicEquals5() {
        AtomicBoolean atomicBool1 = new AtomicBoolean(true);
        AtomicBoolean atomicBool2 = new AtomicBoolean(true);
        boolean badBool = atomicBool1.equals(atomicBool2);
        System.out.println("atomicBool1.equals(atomicBool2) = " + badBool); 
    }

   
    public void recurseA() {
        System.out.println("进入递归");
        recurseA(); 
    }

    
   
    public void violationLockNoUnlock1() {
        synchronized (syncLock) {
            System.out.println("场景1: 获取锁成功");
           
            if (Math.random() > 0.5) { 
                throw new RuntimeException("模拟异常");
            }
            System.out.println("场景1: 正常执行完毕");
           
        }
    }
	
    public void violationLockNoUnlock2() {
       reentrantLock.lock();
        try {
            System.out.println("场景2: 获取锁成功");
           
            if (Math.random() > 0.5) { 
                throw new RuntimeException("模拟异常");
            }
            System.out.println("场景2: 正常执行完毕");
        } finally {
           
        }
        
    }

    public void scenario3_EarlyReturn() {
		lock.lock(); 
		boolean needReturn = true;
		if(needReturn){ 
			retuen;
		}
		
		try{
			
		}finally{
			lock.unlock();
		}
    }

    public void scenario4_WrongLockReference() {
        reentrantLock.lock();
        try {
            System.out.println("  执行中");
        } finally {
            
            }
        }
    }

    public void scenario5_NestedLockError() {
        lockOuter.lock(); 
		try{
			lockInter.lock(); 
			try{
				if(Math.random() > 0.3){
					throw new RuntimeException("业务执行异常");
				}
				lockOuter.unlock(); 
			}finally{
				lockInter.lock();
			}
		}catch(Exception e){
			
		}
		
    }

    

    public void violationReleasableNoRelease2() {
        Releasable resource = () -> System.out.println("释放连接");
        boolean useResource = true;
        if (useResource) {
            System.out.println("执行资源操作");
        }
        
    }

	public void violationReportFileLeak() {
        String filePath = "/data/reports/transaction_log.csv";
        BufferedReader reader = null;
        try {
           
            reader = new BufferedReader(new FileReader(filePath)); 
            String line;
          
            while ((line = reader.readLine()) != null) { 
                
                processTransactionLine(line);
            }
            System.out.println("报表生成完成");
        } catch (IOException e) {
            System.err.println("读取报表文件失败: " + e.getMessage());
        } finally {
            
        }
    }

	public void violationLockNotReleased() {
        ReentrantLock lock = new ReentrantLock();
        try {
            
            lock.lock();
            
            System.out.println("开始扣减库存...");
           
            if (Math.random() > 0.5) { 
                throw new RuntimeException("库存扣减失败：数据库超时");
            }
            System.out.println("库存扣减成功");
            
        } catch (Exception e) {
            System.err.println("扣减库存异常: " + e.getMessage());
            
        } finally {
            
        }
    }

	public void violationAsyncTaskNotCancelled() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        
        Future<?> future = executor.submit(() -> { 
            System.out.println("开始发送短信通知...");
            try {
                Thread.sleep(5000); 
                System.out.println("短信发送成功");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        
        boolean userCancelled = true;
        if (userCancelled) {
           
        }
        
        
    }
	
   
	public void violationSyncGetClass1() {
        synchronized (“LOCK”) {
            System.out.println("同步操作");
        }
    }
	
    public void violationSyncGetClass2() {
        String lockStr = "ORDER_LOCK";
		synchronized(lockStr){
			System.out.println("字符串变量锁");
		}
    }
	
	public void violationSyncGetClass3() {
        synchronized (this.getClass()) {
            System.out.println("同步操作");
        }
    }
	
	public void violationSyncGetClass4() {
        Class<?> clazz = this.getClass();
        synchronized (clazz) {
            System.out.println("通过变量持有class对象同步");
        }
    }
	
	public void violationSyncGetClass5() {
        Integer lockNum = 100;
		synchronized(lockNum){
			System.out.println("装箱Integer锁");
		}
    }
}
