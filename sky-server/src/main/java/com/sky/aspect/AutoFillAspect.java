package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 自定义切面，实现公共字段自动填充处理逻辑
 */
@Aspect
@Slf4j
@Component
public class AutoFillAspect {
    /*
    切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}

    /**
     * 前置通知，在通知中进行公共字段的赋值
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        log.info("开始进行公共字段自动填充");
        //获取当前被拦截方法上的数据库操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//方法签名对象
        AutoFill annotation = signature.getMethod().getAnnotation(AutoFill.class);//获得方法上的注解对象
        OperationType operationType = annotation.value();//获得数据库操作类型

        //获取被拦截方法的参数--实体对象
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0){
            return;
        }
        Object eneity=args[0];
        //准备赋值的数据
        LocalDateTime now=LocalDateTime.now();
        Long currentId= BaseContext.getCurrentId();
        ///根据不同的类型，为对应属性通过反射赋值
        if(operationType == OperationType.INSERT){
            try{
                Method setCreateTime=eneity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME,LocalDateTime.class);
                Method setCreateUssr=eneity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER,Long.class);
                Method setUpdateTime=eneity.getClass().getDeclaredMethod("setUpdateTime", LocalDate.class);
                Method setUpdateUser=eneity.getClass().getDeclaredMethod("setUpdateUser",Long.class);
                //通过反射为对象属性赋值
                setCreateTime.invoke(eneity,now);
                setCreateUssr.invoke(eneity,currentId);
                setUpdateTime.invoke(eneity,now);
                setUpdateUser.invoke(eneity,currentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (operationType == OperationType.UPDATE) {
            try{
                Method setUpdateTime=eneity.getClass().getDeclaredMethod("setUpdateTime", LocalDate.class);
                Method setUpdateUser=eneity.getClass().getDeclaredMethod("setUpdateUser",Long.class);

                setUpdateTime.invoke(eneity,now);
                setUpdateUser.invoke(eneity,currentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
