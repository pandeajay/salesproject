package com.sales.aspect;

import org.aopalliance.intercept.Joinpoint;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;



@Aspect
public class LoggingAspect {
 
	@Pointcut("execution(* com.sales.dao.impl.ProductDaoImpl.*(*))")
	public void logBefore(Joinpoint joinPoint) {	
		System.out.println("logBefore:: " + ((JoinPoint) joinPoint).getSignature().getName());		
	}
	
	@Pointcut("execution(* com.sales.dao.impl.ProductDaoImpl.*(*))")
	public void logAfter(Joinpoint joinPoint) {	
		System.out.println("logAfter:: " + ((JoinPoint) joinPoint).getSignature().getName());		
	}
 
}