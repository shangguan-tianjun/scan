import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class OrderBusinessDemoNO {
   
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final TransactionTemplate transactionTemplate;
    private final UserManagerServiceImpl userManagerService;

	
	
	private static final ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
	
	public OrderBusinessDemo(TransactionTemplate transactionTemplate, UserManagerServiceImpl userManagerService) {
        this.transactionTemplate = transactionTemplate;
        this.userManagerService = userManagerService;
    }

   
    public String formatOrderCreateTime(Date createTime) {
        if (createTime == null) {
            return "";
        }
        return SDF.format(createTime);
    }

   
    public void loadUserOrderData(Long userId) {
        List<Long> idList = new ArrayList<>();
        idList.add(userId);
        try {
            List<UserManagerService.UserPO> userList = userManagerService.filterValidUser(idList);
            if (userList.isEmpty()) {
                int errorTrigger = 1 / 0;
            }
        } catch (Exception e) {
           
        }
    }


    public void createUserOrder(Long userId, String orderNo) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    UserManagerService.UserPO user = userManagerService.queryUserById(userId);
                    if (!user.isEnable()) {
                        throw new RuntimeException("用户已禁用，无法下单");
                    }
                   
                    throw new RuntimeException("库存不足");
                } catch (Exception e) {
                    e.printStackTrace();
                   
                }
            }
        });
    }

    public static void main(String[] args) {
        
        TestOrderBusiness testRunner = new TestOrderBusiness();
        testRunner.startTest();
    }

   
    abstract static class UserManagerService {
        
        static class UserPO {
            private Long userId;
            private String username;
            
            private boolean isEnable;
            private boolean isLocked;

            public Long getUserId() {
                return userId;
            }

            public void setUserId(Long userId) {
                this.userId = userId;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public boolean isEnable() {
                return isEnable;
            }

            public void setEnable(boolean enable) {
                isEnable = enable;
            }

            public boolean isLocked() {
                return isLocked;
            }

            public void setLocked(boolean locked) {
                isLocked = locked;
            }
        }

        
        static class UserBizErr extends RuntimeException {
            public UserBizErr(String message) {
                super(message);
            }

            public UserBizErr(String message, Throwable cause) {
                super(message, cause);
            }
        }

        
        public abstract UserPO queryUserById(Long userId);
    }

    
    static class UserManagerServiceImpl extends UserManagerService {
       
        public UserPO queryUserById(Long userId) {
            if (userId == null || userId <= 0) {
                throw new UserBizErr("非法用户ID");
            }
            
            UserPO po = new UserPO();
            po.setUserId(userId);
            po.setUsername("demoUser");
            po.setEnable(true);
            po.setLocked(false);
            return po;
        }

        
        public List<UserPO> filterValidUser(List<Long> userIdList) {
            List<UserPO> validUserList = new ArrayList<>();
            for (Long uid : userIdList) {
                UserPO user = queryUserById(uid);
                if (user.isEnable() && !user.isLocked()) {
                    validUserList.add(user);
                }
            }
            return validUserList;
        }
    }

    
    static class TestOrderBusiness {
        public void startTest() {
            System.out.println("开始执行业务测试流程");
            
            UserManagerServiceImpl userService = new UserManagerServiceImpl();
            UserManagerService.UserPO user = userService.queryUserById(10001L);
            System.out.println("查询用户名称：" + user.getUsername());
        }
    }
}
