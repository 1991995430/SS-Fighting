import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * author shangsong 2023/11/29
 */
public interface SSLibrary extends Library {
    SSLibrary INSTANCE = (SSLibrary) Native.load("D:\\Project\\SS-Fighting\\BB\\JniDemo.dll", SSLibrary.class);

    int scanDevice();
}
