import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ViolationDemoNO extends ParentService {

    
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/test";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Admin@123456";

    private String name;

    
    public ViolationDemo(String name) {
        this.name = name;
        initBusinessData();
    }

    public void init() {
    }

    private void initBusinessData() {
        System.out.println("执行业务初始化加载");
    }

  
    public void printInfo(Integer id) {
        System.out.println("子类重载方法：printInfo(Integer)");
    }

	
    public void businessMethod() {
        List<String> dataList = new ArrayList<>();
        dataList.add("A");
        dataList.add("B");
        dataList.add("C");

        for (String item : dataList) {
            if ("B".equals(item)) {
                dataList.remove(item);
            }
			if ("C".equals(item)) {
                dataList.add('E'); 
            }
        }
    }
	

    public void pollStatusWithoutExit() {
      
        String taskStatus = "RUNNING";  
        while (true) {
            if ("FINISH".equals(taskStatus)) {
                System.out.println("任务完成，退出循环");
                break;
            }
           
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
        }
    }
    
    public void badForInfiniteLoop() {
        for (; ; ) {
            System.out.println("无限for循环执行中");
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    
    public void unreachableBreakLoop() {
        int num = 10;
        while (true) {
            if (num > 20) {
                System.out.println("退出循环");
                break;
            }
            
            System.out.println("当前数值：" + num);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
	
   
    public void workerThreadLoop() {
        new Thread(() -> {
            while (true) {
                System.out.println("工作线程持续消费数据");
               
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }
	
	
    public void loopWithTryCatchDemo() {
        while (true) {
            try {
                System.out.println("循环执行业务");
                int num = 1 / 0;
            } catch (Exception e) {
                e.printStackTrace();
                
            }
        }
    }

	
    public void threadPoolDemo() {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        executor.submit(() -> System.out.println("执行线程任务"));
    }

	
    public void bigDecimalDemo() {
        
		
        double amount = 0.1;
        BigDecimal num = new BigDecimal(amount);
        System.out.println(num);
		
		
		
        double d4 = 10.0;
        BigDecimal bad4 = new BigDecimal(d4);
		
		
		
        double d5 = 99999.99;
        BigDecimal bad5 = new BigDecimal(d5);
        BigDecimal good5 = new BigDecimal("99999.99");
    }

    public static void main(String[] args) {
        ViolationDemo demo = new ViolationDemo("测试");
        demo.printInfo(123);
        demo.bigDecimalDemo();
        demo.threadPoolDemo();
        demo.businessMethod();
        demo.loopWithTryCatchDemo();
    }
}


class ParentService {
    public void printInfo(Long id) {
        System.out.println("父类方法：printInfo(Long)");
    }
}
