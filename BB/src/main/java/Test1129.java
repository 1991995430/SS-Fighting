import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * author shangsong 2023/11/29
 */
public class Test1129 {

    public interface CLibrary extends Library {
        CLibrary INSTANCE = (CLibrary) Native.loadLibrary("D:\\Project\\SS-Fighting\\BB\\JniDemo.dll", CLibrary.class);

        int add(int a, int b);
    }

    public static void main(String[] args) {
        // JNI方式调用
        /*System.out.println("java.library.path:" + System.getProperty("java.library.path"));
        System.loadLibrary("JniDemo");

        SSJni dhJni = new SSJni();
        System.out.println(dhJni.scanDevice());*/

        // JNA方式
        CLibrary instance = CLibrary.INSTANCE;
        System.out.println(instance.add(26, 26));
    }

}
