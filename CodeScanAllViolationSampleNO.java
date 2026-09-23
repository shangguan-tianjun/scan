import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import javax.crypto.Cipher;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import java.security.MessageDigest;
import java.util.concurrent.TimeUnit;


public class CodeScanAllViolationSampleNO {
	private final Object secondLock = new Object();
    private final Object singleLock = new Object();

	private Object mutableLockField = new Object();

   
    private static final String STR_CONST_LOCK = "GLOBAL_LOCK";
    private Integer boxIntLock = 200; 

    public void violateStringBoxedLock() {
        
        synchronized (STR_CONST_LOCK) {
            System.out.println("字符串常量锁");
        }
        
        synchronized (boxIntLock) {
            System.out.println("装箱原语Integer锁");
        }
    }

    
    public void violateMutableFieldLock() {
		
        synchronized (mutableLockField) {
            System.out.println("在可变成员字段上进行同步");
            mutableLockField = new Object();
        }
    }

   
    public void violateDualLockWait() throws InterruptedException {
        synchronized (singleLock) {
            synchronized (secondLock) {
               
                secondLock.wait(TimeUnit.SECONDS.toMillis(2)); 
            }
        }
    }

    
    static class BaseEntity {
        public BaseEntity() {
            loadResource(); 
        }

        protected void loadResource() {
            System.out.println("父类资源加载逻辑");
        }
    }

    static class ChildBaseEntity extends BaseEntity { 
        private String content;

        @Override
        protected void loadResource() {
			super.loadResource();
			String tip = "改写父类方法";
            System.out.println(content.length());
        }
    }

   
    public void encryptWithECB(String plaintext, String key) {
        try {
           
            byte[] keyBytes = Arrays.copyOf(key.getBytes(StandardCharsets.UTF_8), 16);
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

           
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

           
            byte[] encrypted = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            System.out.println("ECB Encrypted (Base64): " + Base64.getEncoder().encodeToString(encrypted));
        } catch (Exception e) {
            System.err.println("ECB Encryption Failed: " + e.getMessage());
        }
    }

    
    public void encryptWithFixedIV(String plaintext, String key) {
        try {
            byte[] keyBytes = Arrays.copyOf(key.getBytes(StandardCharsets.UTF_8), 16);
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

           
            byte[] iv = new byte[16];
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);

            byte[] encrypted = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            System.out.println("Fixed IV CBC Encrypted (Base64): " + Base64.getEncoder().encodeToString(encrypted));
        } catch (Exception e) {
            System.err.println("Fixed IV Encryption Failed: " + e.getMessage());
        }
    }

   
    public void encryptWithWeakKeyDerivation(String plaintext, String password) {
        try {
            
            byte[] keyBytes = password.getBytes(StandardCharsets.UTF_8);
            if (keyBytes.length < 16) {
                keyBytes = Arrays.copyOf(keyBytes, 16);
            } else {
                keyBytes = Arrays.copyOf(keyBytes, 16);
            }
            
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
            
            
            byte[] iv = new byte[16];
            java.security.SecureRandom secureRandom = new java.security.SecureRandom();
            secureRandom.nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);

            byte[] encrypted = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
           
            byte[] combined = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);
            System.out.println("Weak Key Derivation Encrypted (Base64): " + Base64.getEncoder().encodeToString(combined));
        } catch (Exception e) {
            System.err.println("Weak Key Derivation Encryption Failed: " + e.getMessage());
        }
    }


    public void decryptECB(String base64Encrypted, String key) {
        try {
            byte[] keyBytes = Arrays.copyOf(key.getBytes(StandardCharsets.UTF_8), 16);
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            byte[] decoded = Base64.getDecoder().decode(base64Encrypted);
            byte[] decrypted = cipher.doFinal(decoded);
            System.out.println("ECB Decrypted: " + new String(decrypted, StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.err.println("ECB Decryption Failed: " + e.getMessage());
        }
    }


    public void decryptFixedIVCBC(String base64Encrypted, String key) {
        try {
            byte[] keyBytes = Arrays.copyOf(key.getBytes(StandardCharsets.UTF_8), 16);
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

            
            byte[] iv = new byte[16];
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);

            byte[] decoded = Base64.getDecoder().decode(base64Encrypted);
            byte[] decrypted = cipher.doFinal(decoded);
            System.out.println("Fixed IV CBC Decrypted: " + new String(decrypted, StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.err.println("Fixed IV CBC Decryption Failed: " + e.getMessage());
        }
    }
	

    
    public void unsafeHashProcessing() throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digestBytes = md.digest("business_secret_data".getBytes());
        System.out.println("MD5哈希结果：" + new String(digestBytes));
    }

    
    public void elExpressionInjectRisk(String userInput) {
        ExpressionFactory factory = ExpressionFactory.newInstance(); 
        ELContext elContext = factory.createELContext(null);
        String elScript = "${" + userInput + "}";
        ValueExpression valueExpression = factory.createValueExpression(elContext, elScript, Object.class);
        valueExpression.getValue(elContext);
    }


    public void unsafeJacksonParse() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.activateDefaultTyping(
                new BasicPolymorphicTypeValidator.Builder()
                        .allowIfSubType(Object.class).build() 
        );
        String maliciousJsonStr = "{\"@class\":\"java.util.HashMap\"}";
        Object resultObj = objectMapper.readValue(maliciousJsonStr, Object.class);
        System.out.println(resultObj);
    }
	
	public static void main(String[] args) throws Exception {
        CodeScanAllViolationSample demo = new CodeScanAllViolationSample();
        demo.violateStringBoxedLock();
        demo.violateMutableFieldLock();
        new ChildBaseEntity();
        demo.violateDualLockWait();
        demo.unsafeAesEncryptDemo();
        demo.unsafeHashProcessing();
        demo.elExpressionInjectRisk("userParamInput");
        demo.unsafeJacksonParse();
    }
}
