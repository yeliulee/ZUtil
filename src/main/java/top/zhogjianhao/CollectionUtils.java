package top.zhogjianhao;


import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IteratorUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;

/**
 * 集合工具类
 */
@Slf4j
public class CollectionUtils {

  /**
   * 是否 每个集合都 (为 null || 没有元素)
   *
   * @param colls 多个集合
   * @return 是否 每个集合都 (为 null || 没有元素)
   */
  public static boolean isEmpty(final Collection<?>... colls) {
    if (colls == null) {
      return true;
    }
    if (colls.length == 0) {
      throw new IllegalArgumentException("Colls: length should be greater than 0");
    }
    for (Collection<?> coll : colls) {
      // 如果 某个集合 (不为 null && 有元素)
      if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(coll)) {
        return false;
      }
    }
    return true;
  }

  /**
   * 是否 每个集合都 (不为 null && 有元素)
   *
   * @param colls 多个集合
   * @return 是否 每个集合都 (不为 null && 有元素)
   */
  public static boolean isNotEmpty(final Collection<?>... colls) {
    return !isEmpty(colls);
  }

  /**
   * 是否 每个对象都 (为 null || 没有元素)
   *
   * <ul>
   * <li>Collection - via collection isEmpty
   * <li>Map - via map isEmpty
   * <li>Array - using array size
   * <li>Iterator - via hasNext
   * <li>Enumeration - via hasMoreElements
   * </ul>
   *
   * @param objects 多个对象
   * @return 是否 每个对象都 (为 null || 没有元素)
   */
  public static boolean sizeIsEmpty(final Object... objects) {
    if (objects == null) {
      return true;
    }
    if (objects.length == 0) {
      throw new IllegalArgumentException("Objects: length should be greater than 0");
    }
    for (Object object : objects) {
      // 如果 某个对象 (不为 null && 有元素)
      if (!org.apache.commons.collections4.CollectionUtils.sizeIsEmpty(object)) {
        return false;
      }
    }
    return true;
  }

  /**
   * 是否 每个对象都 (不为 null && 有元素)
   *
   * @param objects 多个对象
   * @return 是否 每个对象都 (不为 null && 有元素)
   */
  public static boolean sizeIsNotEmpty(@NonNull final Object... objects) {
    return !sizeIsEmpty(objects);
  }

  /**
   * 是否 每个对象的 (所有元素都为 null || 没有元素)
   * <p>
   * 注意：会遍历 Iterator，后续使用需重新创建，但是和 {@link CollectionUtils#isAllEquals(boolean, Function, Object...)}、{@link CollectionUtils#isAllEqualsSameIndex(boolean, Function, Object...)} 使用时却无须担心，因为其内部会在调用此方法前就将 Iterator 转换为 List
   *
   * <ul>
   * <li>Collection - removeIf null, size() == 0
   * <li>Map - self(values())
   * <li>Array - noneMatch nonNull
   * <li>Iterator - !(next() != null)
   * <li>Enumeration - 同 Iterator
   * </ul>
   *
   * @param objects 多个对象
   * @return 是否 每个对象的 (所有元素都为 null || 没有元素)
   */
  public static boolean isAllEmpty(final Object... objects) {
    if (objects == null) {
      return true;
    }
    if (objects.length == 0) {
      throw new IllegalArgumentException("Objects: length should be greater than 0");
    }
    for (Object object : objects) {
      if (object == null) {
        continue;
      }
      if (object instanceof Collection) {
        Collection<?> obj1 = (Collection<?>) object;
        obj1.removeIf(Objects::isNull);
        if (obj1.size() != 0) {
          return false;
        }
      } else if (object instanceof Iterable<?>) {
        for (Object o : (Iterable<?>) object) {
          if (o != null) {
            return false;
          }
        }
      } else if (object instanceof Map<?, ?>) {
        if (!isAllEmpty(((Map<?, ?>) object).values())) {
          return false;
        }
      } else if (object instanceof Object[]) {
        Object[] obj1 = (Object[]) object;
        if (Arrays.stream(obj1).anyMatch(Objects::nonNull)) {
          return false;
        }
      } else if (object instanceof Iterator<?>) {
        Iterator<?> obj1 = (Iterator<?>) object;
        while (obj1.hasNext()) {
          if (obj1.next() != null) {
            return false;
          }
        }
      } else if (object instanceof Enumeration<?>) {
        Enumeration<?> obj1 = (Enumeration<?>) object;
        while (obj1.hasMoreElements()) {
          if (obj1.nextElement() != null) {
            return false;
          }
        }
      }
    }
    return true;
  }

  /**
   * 是否 每个对象的 (所有元素都不为 null && 有元素)
   *
   * @param objects 多个对象
   * @return 是否 每个对象的 (所有元素都不为 null && 有元素)
   */
  public static boolean isNotAllEmpty(@NonNull final Object... objects) {
    return !isAllEmpty(objects);
  }

  /**
   * 是否 任意对象 (为 null || 没有元素 || 任意元素为 null)
   * <p>
   * 注意：会遍历 Iterator，后续使用需重新创建，但是和 {@link CollectionUtils#isAllEquals(boolean, Function, Object...)}、{@link CollectionUtils#isAllEqualsSameIndex(boolean, Function, Object...)} 使用时却无须担心，因为其内部会在调用此方法前就将 Iterator 转换为 List
   *
   * <ul>
   * <li>Collection - contains null
   * <li>Map - containsValue null
   * <li>Array - anyMatch null
   * <li>Iterator - next() == null
   * <li>Enumeration - 同 Iterator
   * </ul>
   *
   * @param objects 多个对象
   * @return 是否 任意对象 (为 null || 没有元素 || 任意元素为 null)
   */
  public static boolean isAnyEmpty(final Object... objects) {
    if (objects == null) {
      return true;
    }
    if (objects.length == 0) {
      throw new IllegalArgumentException("Objects: length should be greater than 0");
    }
    for (Object object : objects) {
      if (sizeIsEmpty(object)) {
        return true;
      }
      if (object instanceof Collection<?>) {
        Collection<?> obj1 = (Collection<?>) object;
        if (obj1.contains(null)) {
          return true;
        }
      } else if (object instanceof Iterable<?>) {
        for (Object o : (Iterable<?>) object) {
          if (o == null) {
            return true;
          }
        }
      } else if (object instanceof Map<?, ?>) {
        if (((Map<?, ?>) object).containsValue(null)) {
          return true;
        }
      } else if (object instanceof Object[]) {
        Object[] obj1 = (Object[]) object;
        if (Arrays.stream(obj1).anyMatch(Objects::isNull)) {
          return true;
        }
      } else if (object instanceof Iterator<?>) {
        Iterator<?> obj1 = (Iterator<?>) object;
        while (obj1.hasNext()) {
          if (obj1.next() == null) {
            return true;
          }
        }
      } else if (object instanceof Enumeration<?>) {
        Enumeration<?> obj1 = (Enumeration<?>) object;
        while (obj1.hasMoreElements()) {
          if (obj1.nextElement() == null) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * 是否 每个对象 (不为 null && 有元素 && 每个元素都不为 null)
   *
   * @param objects 多个对象
   * @return 是否 每个对象 (不为 null && 有元素 && 每个元素都不为 null)
   */
  public static boolean isNotAnyEmpty(final Object... objects) {
    return !isAnyEmpty(objects);
  }

  /**
   * 基础类型和 BigDecimal、BigInteger 会被转换为 String，且小数点后无效的 0 会被去除
   *
   * @param object 元素
   * @return 字符串
   */
  private static Object toStringByBasic(Object object, boolean isByValue) {
    if (isByValue && (ClassUtils.isBasic(object) || object instanceof BigDecimal || object instanceof BigInteger)) {
      if (object instanceof Float || object instanceof Double || object instanceof BigDecimal) {
        object = new BigDecimal(object.toString()).stripTrailingZeros().toPlainString();
      } else {
        object = object.toString();
      }
    }
    return object;
  }

  /**
   * 是否 每个对象的每个元素都相等
   *
   * @param isByValue        是否根据值来判断是否相等，基础类型和 BigDecimal、BigInteger 会被转换为 String，且小数点后无效的 0 会被去除
   * @param continueFunction 对象何时不参与判断
   * @param objects          多个对象
   * @return 是否 每个对象的每个元素都相等
   */
  public static boolean isAllEquals(boolean isByValue, Function<Object, Boolean> continueFunction, final Object... objects) {
    if (objects == null) {
      return true;
    }
    if (objects.length == 0) {
      throw new IllegalArgumentException("Objects: length should be greater than 0");
    }
    Object prevObj = null;
    for (Object object : objects) {
      // continueFunction 中可能已经使用了 Iterator，所以在调用前转换为 List
      if (object instanceof Iterator<?>) {
        object = IteratorUtils.toList((Iterator<?>) object);
      }
      // 对象指定条件时不参与判断
      if (continueFunction != null && continueFunction.apply(object)) {
        continue;
      }
      if (object instanceof Collection<?> || object instanceof Map<?, ?>) {
        Iterator<?> iterator;
        if (object instanceof Collection<?>) {
          iterator = ((Collection<?>) object).iterator();
        } else {
          iterator = (((Map<?, ?>) object).values()).iterator();
        }
        int i = 0;
        while (iterator.hasNext()) {
          // 基础类型转为 String
          Object nextObj = toStringByBasic(iterator.next(), isByValue);
          // 首次判断前跳过第一次循环
          if (prevObj == null && i == 0) {
            prevObj = nextObj;
            i = 1;
            continue;
          }
          if (!Objects.equals(prevObj, nextObj)) {
            return false;
          }
          prevObj = nextObj;
        }
      } else if (object instanceof Iterable<?>) {
        int i = 0;
        for (Object o : (Iterable<?>) object) {
          Object nextObj = toStringByBasic(o, isByValue);
          if (prevObj == null && i == 0) {
            prevObj = nextObj;
            i = 1;
            continue;
          }
          if (!Objects.equals(prevObj, nextObj)) {
            return false;
          }
          prevObj = nextObj;
        }
      } else if (object instanceof Object[]) {
        Object[] objects1 = (Object[]) object;
        for (int i = 0; i < objects1.length; i++) {
          Object nextObj = toStringByBasic(objects1[i], isByValue);
          if (prevObj == null && i == 0) {
            prevObj = nextObj;
            i = 1;
            continue;
          }
          if (!Objects.equals(prevObj, nextObj)) {
            return false;
          }
          prevObj = nextObj;
        }
      } else if (object instanceof Enumeration<?>) {
        Enumeration<?> enumeration = (Enumeration<?>) object;
        int i = 0;
        while (enumeration.hasMoreElements()) {
          Object nextObj = toStringByBasic(enumeration.nextElement(), isByValue);
          if (prevObj == null && i == 0) {
            prevObj = nextObj;
            i = 1;
            continue;
          }
          if (!Objects.equals(prevObj, nextObj)) {
            return false;
          }
          prevObj = nextObj;
        }
      }
    }
    return true;
  }

  /**
   * 是否不满足 每个对象的每个元素都相等
   *
   * @param isByValue        是否根据值来判断是否相等，基础类型和 BigDecimal、BigInteger 会被转换为 String，且小数点后无效的 0 会被去除
   * @param continueFunction 对象何时不参与判断
   * @param objects          多个对象
   * @return 是否不满足 每个对象的每个元素都相等
   */
  public static boolean isNotAllEquals(boolean isByValue, Function<Object, Boolean> continueFunction, final Object... objects) {
    return !isAllEquals(isByValue, continueFunction, objects);
  }

  /**
   * 是否 每个对象的同一位置的元素都相等
   *
   * @param isByValue        是否根据值来判断是否相等，基础类型和 BigDecimal、BigInteger 会被转换为 String，且小数点后无效的 0 会被去除
   * @param continueFunction 对象何时不参与判断
   * @param objects          多个对象
   * @return 是否 每个对象的同一位置的元素都相等
   */
  public static boolean isAllEqualsSameIndex(boolean isByValue, Function<Object, Boolean> continueFunction, final Object... objects) {
    if (objects == null) {
      return true;
    }
    if (objects.length < 2) {
      throw new IllegalArgumentException("Objects: length should be greater than 1");
    }

    Integer objectSize = null;
    for (Object object : objects) {
      // continueFunction 中可能已经使用了 Iterator，所以在调用前转换为 List
      if (object instanceof Iterator<?>) {
        object = IteratorUtils.toList((Iterator<?>) object);
      }
      // 对象指定条件时不参与判断
      if (continueFunction != null && continueFunction.apply(object)) {
        continue;
      }
      int size = -1;
      if (object instanceof Collection<?>) {
        size = ((Collection<?>) object).size();
      } else if (object instanceof Map<?, ?>) {
        size = ((Map<?, ?>) object).values().size();
      } else if (object instanceof Iterable<?>) {
        int i = 0;
        for (Object o : (Iterable<?>) object) {
          i++;
        }
        size = i;
      } else if (object instanceof Object[]) {
        size = ((Object[]) object).length;
      } else if (object instanceof Enumeration<?>) {
        Enumeration<?> enumeration = (Enumeration<?>) object;
        int i = 0;
        for (; enumeration.hasMoreElements(); i++) {
          enumeration.nextElement();
        }
        size = i;
      }
      // 元素长度不一致时退出
      if (objectSize != null && !objectSize.equals(size)) {
        return false;
      }
      objectSize = size;
    }

    List<Object> prevList = new LinkedList<>();
    for (Object object : objects) {
      if (object instanceof Collection<?> || object instanceof Map<?, ?>) {
        Iterator<?> iterator;
        if (object instanceof Collection<?>) {
          iterator = ((Collection<?>) object).iterator();
        } else {
          iterator = (((Map<?, ?>) object).values()).iterator();
        }
        int i = 0;
        for (; iterator.hasNext(); i++) {
          // 基础类型转为 String
          Object nextObj = toStringByBasic(iterator.next(), isByValue);
          if (prevList.size() < i + 1) {
            prevList.add(nextObj);
            i = 1;
            continue;
          }
          if (!Objects.equals(prevList.get(i), nextObj)) {
            return false;
          }
          prevList.set(i, nextObj);
        }
      } else if (object instanceof Iterable<?>) {
        int i = 0;
        for (Object o : (Iterable<?>) object) {
          Object nextObj = toStringByBasic(o, isByValue);
          if (prevList.size() < i + 1) {
            prevList.add(nextObj);
            i = 1;
            continue;
          }
          if (!Objects.equals(prevList.get(i), nextObj)) {
            return false;
          }
          prevList.set(i, nextObj);
          i++;
        }
      } else if (object instanceof Object[]) {
        Object[] objects1 = (Object[]) object;
        for (int i = 0; i < objects1.length; i++) {
          Object nextObj = toStringByBasic(objects1[i], isByValue);
          if (prevList.size() < i + 1) {
            prevList.add(nextObj);
            i = 1;
            continue;
          }
          if (!Objects.equals(prevList.get(i), nextObj)) {
            return false;
          }
          prevList.set(i, nextObj);
        }
      } else if (object instanceof Enumeration<?>) {
        Enumeration<?> enumeration = (Enumeration<?>) object;
        int i = 0;
        for (; enumeration.hasMoreElements(); i++) {
          Object nextObj = toStringByBasic(enumeration.nextElement(), isByValue);
          if (prevList.size() < i + 1) {
            prevList.add(nextObj);
            i = 1;
            continue;
          }
          if (!Objects.equals(prevList.get(i), nextObj)) {
            return false;
          }
          prevList.set(i, nextObj);
        }
      }
    }
    return true;
  }

  /**
   * 是否不满足 每个对象的同一位置的元素都相等
   *
   * @param isByValue        是否根据值来判断是否相等，基础类型和 BigDecimal、BigInteger 会被转换为 String，且小数点后无效的 0 会被去除
   * @param continueFunction 对象何时不参与判断
   * @param objects          多个对象
   * @return 是否不满足 每个对象的同一位置的元素都相等
   */
  public static boolean isNotAllEqualsSameIndex(boolean isByValue, Function<Object, Boolean> continueFunction, final Object... objects) {
    return !isAllEqualsSameIndex(isByValue, continueFunction, objects);
  }

  /**
   * 从指定下标开始，将后面的元素往前复制指定位数
   *
   * @param arr        数组
   * @param arrLength  数组长度
   * @param startIndex 开始下标
   * @param length     位数
   * @return 数组
   */
  @Deprecated
  public static boolean moveForward(@NonNull Object arr, final int arrLength, final int startIndex, final int length) {
    if (!(arr instanceof Object[] || arr instanceof int[] || arr instanceof long[] || arr instanceof double[] || arr instanceof float[] || arr instanceof boolean[] || arr instanceof short[] || arr instanceof byte[] || arr instanceof char[])) {
      return false;
    }
    if (arrLength - length - startIndex >= 0) {
      System.arraycopy(arr, startIndex + length, arr, startIndex, arrLength - length - startIndex);
      return true;
    }
    return false;
  }

  /**
   * 删除指定下标的元素
   *
   * @param arr              数组
   * @param index            下标
   * @param lastElementValue 最后一个元素的值
   * @return 数组
   */
  @Deprecated
  public static boolean remove(@NonNull Object[] arr, final int index, @NonNull final Object lastElementValue) {
    if (index < 0) {
      return false;
    }
    moveForward(arr, arr.length, index, 1);
    arr[arr.length - 1] = lastElementValue;
    return true;
  }

  /**
   * 删除指定下标的元素
   *
   * @param arr   数组
   * @param index 下标
   * @return 数组
   */
  @Deprecated
  public static boolean remove(@NonNull Object[] arr, final int index) {
    return remove(arr, index, null);
  }

  /**
   * 删除指定下标的元素
   *
   * @param <T>              基本数据类型
   * @param arr              数组
   * @param index            下标
   * @param lastElementValue 最后一个元素的值
   * @return 数组
   */
  @Deprecated
  public static <T> boolean remove(@NonNull T arr, final int index, @NonNull final Object lastElementValue) {
    if (arr instanceof int[]) {
      moveForward(arr, ((int[]) arr).length, index, 1);
      if (lastElementValue != null) {
        ((int[]) arr)[((int[]) arr).length - 1] = (int) lastElementValue;
      }
    } else if (arr instanceof long[]) {
      moveForward(arr, ((long[]) arr).length, index, 1);
      if (lastElementValue != null) {
        ((long[]) arr)[((long[]) arr).length - 1] = (long) lastElementValue;
      }
    } else if (arr instanceof double[]) {
      moveForward(arr, ((double[]) arr).length, index, 1);
      if (lastElementValue != null) {
        ((double[]) arr)[((double[]) arr).length - 1] = (double) lastElementValue;
      }
    } else if (arr instanceof float[]) {
      moveForward(arr, ((float[]) arr).length, index, 1);
      if (lastElementValue != null) {
        ((float[]) arr)[((float[]) arr).length - 1] = (float) lastElementValue;
      }
    } else if (arr instanceof boolean[]) {
      moveForward(arr, ((boolean[]) arr).length, index, 1);
      if (lastElementValue != null) {
        ((boolean[]) arr)[((boolean[]) arr).length - 1] = (boolean) lastElementValue;
      }
    } else if (arr instanceof short[]) {
      moveForward(arr, ((short[]) arr).length, index, 1);
      if (lastElementValue != null) {
        ((short[]) arr)[((short[]) arr).length - 1] = (short) lastElementValue;
      }
    } else if (arr instanceof byte[]) {
      moveForward(arr, ((byte[]) arr).length, index, 1);
      if (lastElementValue != null) {
        ((byte[]) arr)[((byte[]) arr).length - 1] = (byte) lastElementValue;
      }
    } else if (arr instanceof char[]) {
      moveForward(arr, ((char[]) arr).length, index, 1);
      if (lastElementValue != null) {
        ((char[]) arr)[((char[]) arr).length - 1] = (char) lastElementValue;
      }
    }
    return true;
  }

  /**
   * 删除指定下标的元素
   *
   * @param <T>   基本数据类型
   * @param arr   数组
   * @param index 下标
   * @return 数组
   */
  @Deprecated
  public static <T> boolean remove(@NonNull T arr, final int index) {
    return remove(arr, index, null);
  }
}
