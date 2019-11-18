package com.iyin.sign.system.config.aop;

import com.google.common.collect.Lists;
import com.iyin.sign.system.common.interfaces.CacheRemove;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @ClassName: CacheRemoveAspect
 * @Description: 缓存
 * @Author: yml
 * @CreateDate: 2019/8/2
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/2
 * @Version: 1.0.0
 */
@Aspect
@Component
@Slf4j
public class CacheRemoveAspect {

    private static final int INITIAL_CAPACITY = 16;

    ExpressionParser parser = new SpelExpressionParser();
    LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    @Autowired()
    CacheManager cacheManager;

    @Pointcut(value = "(execution(* *.*(..)) && @annotation(com.iyin.sign.system.common.interfaces.CacheRemove))")
    private void pointcut() {
    }

    @AfterReturning("pointcut()")
    private void process(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CacheRemove cacheRemove = method.getAnnotation(CacheRemove.class);
        if (cacheRemove != null) {
            String value = cacheRemove.value();
            log.info("com.iyin.sign.system.config.aop.CacheRemoveAspect.value,{}", value);
            Cache cache = cacheManager.getCache(value);
            if(null != cache){
                List cacheKeys = getCacheKeys(cache);
                log.info("com.iyin.sign.system.config.aop.CacheRemoveAspect.cacheKeys,{}", cacheKeys);
                //需要移除的正则key
                String key = cacheRemove.key();
                Expression expression = parser.parseExpression(key);
                String[] params = discoverer.getParameterNames(method);
                EvaluationContext context = new StandardEvaluationContext();
                for (int len = 0; len < params.length; len++) {
                    context.setVariable(params[len], args[len]);
                }
                key = expression.getValue(context).toString();
                Pattern pattern = Pattern.compile(key);
                for (Object cacheKey : cacheKeys) {
                    String cacheKeyStr = String.valueOf(cacheKey);
                    if (pattern.matcher(cacheKeyStr).find()) {
                        cache.evict(cacheKeyStr);
                        log.info("com.iyin.sign.system.config.aop.CacheRemoveAspect.evict,{}", cacheKeyStr);
                    }
                }
            }
        }
    }

    private List getCacheKeys(Cache cache) {
        Object o = cache.getNativeCache();
        List<String> list = Lists.newArrayList();
        try {
            Map<String, Object> map2 = (Map<String, Object>) objToMap(o).get("localCache");
            map2.keySet().forEach(s -> list.add(s));
        } catch (Exception e) {
            log.warn("DictionaryItem cache is error !!!");
        }
        return list;
    }

    /**
     * Obj 转换为 map
     * @param obj
     * @return
     * @throws Exception
     */
    private static Map<String, Object> objToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>(INITIAL_CAPACITY);
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }
        return map;
    }
}
