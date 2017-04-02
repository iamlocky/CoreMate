package core.mate.util;

import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 用于数组操作的工具集
 *
 * @author DrkCore
 * @since 2016年2月17日17:43:03
 */
public final class DataUtil {

    private DataUtil() {
    }

    /*Reverse*/

    public static void reverse(byte[] array) {
        int len = array.length;
        if (len <= 1) {
            return;
        }

        int halfLen = len / 2;
        byte tmp;
        for (int i = 0; i < halfLen; i++) {
            tmp = array[i];
            array[i] = array[len - i - 1];
            array[len - i - 1] = tmp;
        }
    }

    public static void reverse(boolean[] array) {
        int len = array.length;
        if (len <= 1) {
            return;
        }

        int halfLen = len / 2;
        boolean tmp;
        for (int i = 0; i < halfLen; i++) {
            tmp = array[i];
            array[i] = array[len - i - 1];
            array[len - i - 1] = tmp;
        }
    }

    public static void reverse(char[] array) {
        int len = array.length;
        if (len <= 1) {
            return;
        }

        int halfLen = len / 2;
        char tmp;
        for (int i = 0; i < halfLen; i++) {
            tmp = array[i];
            array[i] = array[len - i - 1];
            array[len - i - 1] = tmp;
        }
    }

    public static void reverse(short[] array) {
        int len = array.length;
        if (len <= 1) {
            return;
        }

        int halfLen = len / 2;
        short tmp;
        for (int i = 0; i < halfLen; i++) {
            tmp = array[i];
            array[i] = array[len - i - 1];
            array[len - i - 1] = tmp;
        }
    }

    public static void reverse(int[] array) {
        int len = array.length;
        if (len <= 1) {
            return;
        }

        int halfLen = len / 2;
        int tmp;
        for (int i = 0; i < halfLen; i++) {
            tmp = array[i];
            array[i] = array[len - i - 1];
            array[len - i - 1] = tmp;
        }
    }

    public static void reverse(long[] array) {
        int len = array.length;
        if (len <= 1) {
            return;
        }

        int halfLen = len / 2;
        long tmp;
        for (int i = 0; i < halfLen; i++) {
            tmp = array[i];
            array[i] = array[len - i - 1];
            array[len - i - 1] = tmp;
        }
    }

    public static void reverse(float[] array) {
        int len = array.length;
        if (len <= 1) {
            return;
        }

        int halfLen = len / 2;
        float tmp;
        for (int i = 0; i < halfLen; i++) {
            tmp = array[i];
            array[i] = array[len - i - 1];
            array[len - i - 1] = tmp;
        }
    }

    public static void reverse(double[] array) {
        int len = array.length;
        if (len <= 1) {
            return;
        }

        int halfLen = len / 2;
        double tmp;
        for (int i = 0; i < halfLen; i++) {
            tmp = array[i];
            array[i] = array[len - i - 1];
            array[len - i - 1] = tmp;
        }
    }

    public static <T> void reverse(T[] array) {
        int len = array.length;
        if (len <= 1) {
            return;
        }

        int halfLen = len / 2;
        T tmp;
        for (int i = 0; i < halfLen; i++) {
            tmp = array[i];
            array[i] = array[len - i - 1];
            array[len - i - 1] = tmp;
        }
    }

    /*Index*/

    public static int indexOf(boolean[] array, boolean value) {
        for (int i = 0, len = getSize(array); i < len; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(byte[] array, byte value) {
        for (int i = 0, len = getSize(array); i < len; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(char[] array, char value) {
        for (int i = 0, len = getSize(array); i < len; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(short[] array, short value) {
        for (int i = 0, len = getSize(array); i < len; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(int[] array, int value) {
        for (int i = 0, len = getSize(array); i < len; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(long[] array, long value) {
        for (int i = 0, len = getSize(array); i < len; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(float[] array, float value) {
        for (int i = 0, len = getSize(array); i < len; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(double[] array, double value) {
        for (int i = 0, len = getSize(array); i < len; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public static <T> int indexOf(T[] array, T value) {
        for (int i = 0, len = getSize(array); i < len; i++) {
            if ((array[i] != null && array[i].equals(value)) || (array[i] == null && value == null)) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(boolean[] array, boolean value) {
        for (int len = getSize(array), i = len - 1; i >= 0; i--) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(char[] array, char value) {
        for (int len = getSize(array), i = len - 1; i >= 0; i--) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(short[] array, short value) {
        for (int len = getSize(array), i = len - 1; i >= 0; i--) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(int[] array, int value) {
        for (int len = getSize(array), i = len - 1; i >= 0; i--) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(long[] array, long value) {
        for (int len = getSize(array), i = len - 1; i >= 0; i--) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(float[] array, float value) {
        for (int len = getSize(array), i = len - 1; i >= 0; i--) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(double[] array, double value) {
        for (int len = getSize(array), i = len - 1; i >= 0; i--) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public static <T> int lastIndexOf(T[] array, T value) {
        for (int len = getSize(array), i = len - 1; i >= 0; i--) {
            if ((array[i] != null && array[i].equals(value)) || (array[i] == null && value == null)) {
                return i;
            }
        }
        return -1;
    }

    /*IsEmpty*/

    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 当array为null，或者长度为0时返回true。
     *
     * @param array
     * @param <T>
     * @return
     */
    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(byte[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(boolean[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(char[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(short[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(long[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(float[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(double[] array) {
        return array == null || array.length == 0;
    }

    /*Get*/

    @Nullable
    public static <T> T get(@Nullable List<T> list, int location) {
        if (location < 0) {
            throw new IllegalArgumentException("location不允许为负数");
        }
        return list != null && list.size() > location ? list.get(location) : null;
    }

    @Nullable
    public static <T> T get(@Nullable T[] array, int location) {
        if (location < 0) {
            throw new IllegalArgumentException("location不允许为负数");
        }
        return array != null && array.length > location ? array[location] : null;
    }

    @Nullable
    public static Byte get(@Nullable byte[] array, int location) {
        if (location < 0) {
            throw new IllegalArgumentException("location不允许为负数");
        }
        return array != null && array.length > location ? array[location] : null;
    }

    @Nullable
    public static Character get(@Nullable char[] array, int location) {
        if (location < 0) {
            throw new IllegalArgumentException("location不允许为负数");
        }
        return array != null && array.length > location ? array[location] : null;
    }

    @Nullable
    public static Short get(@Nullable short[] array, int location) {
        if (location < 0) {
            throw new IllegalArgumentException("location不允许为负数");
        }
        return array != null && array.length > location ? array[location] : null;
    }

    @Nullable
    public static Integer get(@Nullable int[] array, int location) {
        if (location < 0) {
            throw new IllegalArgumentException("location不允许为负数");
        }
        return array != null && array.length > location ? array[location] : null;
    }

    @Nullable
    public static Long get(@Nullable long[] array, int location) {
        if (location < 0) {
            throw new IllegalArgumentException("location不允许为负数");
        }
        return array != null && array.length > location ? array[location] : null;
    }

    @Nullable
    public static Float get(@Nullable float[] array, int location) {
        if (location < 0) {
            throw new IllegalArgumentException("location不允许为负数");
        }
        return array != null && array.length > location ? array[location] : null;
    }

    @Nullable
    public static Double get(@Nullable double[] array, int location) {
        if (location < 0) {
            throw new IllegalArgumentException("location不允许为负数");
        }
        return array != null && array.length > location ? array[location] : null;
    }

    @Nullable
    public static <T> T getFirst(@Nullable List<T> list) {
        return get(list, 0);
    }

    @Nullable
    public static <T> T getFirst(T[] array) {
        return get(array, 0);
    }

    @Nullable
    public static Byte getFirst(@Nullable byte[] array) {
        return get(array, 0);
    }

    @Nullable
    public static Character getFirst(@Nullable char[] array) {
        return get(array, 0);
    }

    @Nullable
    public static Short getFirst(@Nullable short[] array) {
        return get(array, 0);
    }

    @Nullable
    public static Integer getFirst(@Nullable int[] array) {
        return get(array, 0);
    }

    @Nullable
    public static Long getFirst(@Nullable long[] array) {
        return get(array, 0);
    }

    @Nullable
    public static Float getFirst(@Nullable float[] array) {
        return get(array, 0);
    }

    @Nullable
    public static Double getFirst(@Nullable double[] array) {
        return get(array, 0);
    }

    @Nullable
    public static <T> T getLast(@Nullable List<T> list) {
        return list != null && getSize(list) > 0 ? get(list, list.size() - 1) : null;
    }

    @Nullable
    public static <T> T getLast(T[] array) {
        return getSize(array) > 0 ? get(array, array.length - 1) : null;
    }

    @Nullable
    public static Byte getLast(byte[] array) {
        return getSize(array) > 0 ? get(array, array.length - 1) : null;
    }

    @Nullable
    public static Character getLast(char[] array) {
        return getSize(array) > 0 ? get(array, array.length - 1) : null;
    }

    @Nullable
    public static Short getLast(short[] array) {
        return getSize(array) > 0 ? get(array, array.length - 1) : null;
    }

    @Nullable
    public static Integer getLast(int[] array) {
        return getSize(array) > 0 ? get(array, array.length - 1) : null;
    }

    @Nullable
    public static Long getLast(long[] array) {
        return getSize(array) > 0 ? get(array, array.length - 1) : null;
    }

    @Nullable
    public static Float getLast(float[] array) {
        return getSize(array) > 0 ? get(array, array.length - 1) : null;
    }

    @Nullable
    public static Double getLast(double[] array) {
        return getSize(array) > 0 ? get(array, array.length - 1) : null;
    }

    /*Size*/

    public static <T> int getSize(Collection<T> collection) {
        return collection != null ? collection.size() : 0;
    }

    public static <T> int getSize(T[] array) {
        return array != null ? array.length : 0;
    }

    public static int getSize(boolean[] array) {
        return array != null ? array.length : 0;
    }

    public static int getSize(byte[] array) {
        return array != null ? array.length : 0;
    }

    public static int getSize(char[] array) {
        return array != null ? array.length : 0;
    }

    public static int getSize(short[] array) {
        return array != null ? array.length : 0;
    }

    public static int getSize(int[] array) {
        return array != null ? array.length : 0;
    }

    public static int getSize(long[] array) {
        return array != null ? array.length : 0;
    }

    public static int getSize(float[] array) {
        return array != null ? array.length : 0;
    }

    public static int getSize(double[] array) {
        return array != null ? array.length : 0;
    }

    /*Parse*/

    public static byte parseByte(String str) {
        return parseByte(str, (byte) 0);
    }

    public static byte parseByte(String str, byte defVal) {
        if (TextUtil.isEmpty(str)) {
            return defVal;
        }
        try {
            return Byte.parseByte(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return defVal;
        }
    }

    public static boolean parseBoolean(String str) {
        return parseBoolean(str, false);
    }

    public static boolean parseBoolean(String str, boolean defVal) {
        if (TextUtil.isEmpty(str)) {
            return defVal;
        }
        try {
            return Boolean.parseBoolean(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return defVal;
        }
    }

    public static int parseInt(String str) {
        return parseInt(str, 0);
    }

    public static int parseInt(String str, int defVal) {
        if (TextUtil.isEmpty(str)) {
            return defVal;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return defVal;
        }
    }

    public static short parseShort(String str) {
        return parseShort(str, (short) 0);
    }

    public static short parseShort(String str, short defVal) {
        if (TextUtil.isEmpty(str)) {
            return defVal;
        }
        try {
            return Short.parseShort(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return defVal;
        }
    }

    public static long parseLong(String str) {
        return parseLong(str, 0);
    }

    public static long parseLong(String str, long defVal) {
        if (TextUtil.isEmpty(str)) {
            return defVal;
        }
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return defVal;
        }
    }

    public static float parseFloat(String str) {
        return parseFloat(str, 0);
    }

    public static float parseFloat(String str, float defVal) {
        if (TextUtil.isEmpty(str)) {
            return defVal;
        }
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return defVal;
        }
    }

    public static double parseDouble(String str) {
        return parseDouble(str, 0);
    }

    public static double parseDouble(String str, double defVal) {
        if (TextUtil.isEmpty(str)) {
            return defVal;
        }
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return defVal;
        }
    }

    /*Filter*/

    public interface Filter<T> {

        boolean accept(T t);

    }

    public static <T> List<T> filter(Collection<T> list, Filter<T> filter) {
        List<T> result = null;
        if (list != null) {
            for (T t : list) {
                if (filter.accept(t)) {
                    if (result == null) {
                        result = new ArrayList<>();
                    }
                    result.add(t);
                }
            }
        }
        return result;
    }

    public static <T> void filterIn(Collection<T> list, Filter<T> filter) {
        if (list != null) {
            Iterator<T> iterator = list.iterator();
            while (iterator.hasNext()) {
                if (!filter.accept(iterator.next())) {
                    iterator.remove();
                }
            }
        }
    }

    public static <T> List<T> filter(T[] array, Filter<T> filter) {
        if (array != null) {
            return filter(Arrays.asList(array), filter);
        }
        return null;
    }

    /*Concat*/

    public static byte[] concat(byte[]... array) {
        int len = array.length;
        if (len == 0) {
            return null;
        } else if (len == 1) {
            return array[0];
        } else if (len == 2) {
            return concatTwoArray(array[0], array[1]);
        } else {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                for (byte[] bytes : array) {
                    outputStream.write(bytes);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return outputStream.toByteArray();
        }
    }

    private static byte[] concatTwoArray(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }
}
