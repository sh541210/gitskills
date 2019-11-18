package com.iyin.sign.system.common.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * @author:liushuqiao
 * @title: BeanAdaptUtil
 * @description: 类适配成各种类型，亦或各种类型适配成类的一个适配器工具类
 * @date: 16:20 2017/7/25
 * @version: v1.0.0
 */
public class BeanAdaptUtil {

    private static final Log LOG = LogFactory.getLog(BeanAdaptUtil.class);

    public static final int CODE_LENGTH_FIFTEEN = 15;

    public static final int CODE_LENGTH_NINE = 9;

    public static final int CODE_LENGTH_EIGHTEEN = 18;

    /**
     * 把Object转换为Map
     *
     * @param object
     * @return
     */
    public static Map<String, String> obj2Map(Object object) {
        Map<String, String> map = new HashMap<>(16);
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                //不能转换为String类型的不放入Map中
                String str = (String) field.get(object);
                if (str != null) {
                    map.put(field.getName(), str);
                }
            } catch (IllegalAccessException e) {
                LOG.debug(e);
            }
        }
        return map;
    }

    /**
     * Map转对象
     *
     * @param map
     * @param clz
     * @return
     */
    public static Object map2Obj(Map<String, String> map, Class<?> clz) {
        Object obj = null;
        try {
            obj = clz.newInstance();
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);
                field.set(obj, map.get(field.getName()));
            }
        } catch (Exception e) {
            LOG.error("Map转对象失败");
        }
        return obj;
    }


    /**
     * Map转对象2
     *
     * @param map
     * @param clz
     * @return
     */
    public static Object map2Obj2(Map<String, Object> map, Class<?> clz) {
        Object obj = null;
        try {
            obj = clz.newInstance();
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers())) {
                    field.setAccessible(true);
                    field.set(obj, map.get(field.getName()));
                }
            }
        } catch (Exception e) {
            LOG.error("Map转对象失败");
        }
        return obj;
    }


    /**
     * 把Object转换为Map2
     *
     * @param object
     * @return
     */
    public static Map<String, Object> obj2Map2(Object object) {
        if (object == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>(16);
        for (Field field : object.getClass().getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers())) {
                field.setAccessible(true);
                try {
                    if (field.get(object) != null) {
                        map.put(field.getName(), field.get(object));
                    }
                } catch (IllegalAccessException e) {
                    LOG.debug(e);
                }
            }
        }
        return map;
    }

    public static Map<String, Object> obj2Map3(Object object) {
        if (object == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>(16);
        for (Field field : object.getClass().getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers())) {
                field.setAccessible(true);
                try {
                    if (field.get(object) != null) {
                        map.put(field.getName(), field.get(object)+"");
                    }
                } catch (IllegalAccessException e) {
                    LOG.debug(e);
                }
            }
        }
        return map;
    }


    /**
     * 企业组织机构代码适配
     *
     * @param enterpriseCode
     * @return
     * @author liushuqiao
     * @date 2017-12-22
     */
    public static String uniformCode2OrgCode(String enterpriseCode) {
        if (StringUtils.isEmpty(enterpriseCode)) {
            return null;
        } else if (enterpriseCode.length() == CODE_LENGTH_FIFTEEN || enterpriseCode.length() == CODE_LENGTH_NINE) {
            //15位和9位的不需要做转换
            return enterpriseCode;
        } else if (enterpriseCode.length() == CODE_LENGTH_EIGHTEEN) {
            //载取9位组织机构代码
            return enterpriseCode.substring(8, 17);
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        LOG.info(uniformCode2OrgCode("91440300359183712P"));
    }

}
