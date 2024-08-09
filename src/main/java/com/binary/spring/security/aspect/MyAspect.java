package com.binary.spring.security.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyAspect {
	
	
	@Before("execution(* com.binary.spring.security.controller.*.*(..))")
	public void printBefore(JoinPoint joinpoint) {
		System.out.println("Method execution Started " +joinpoint.getSignature());
	}
	
	@After("execution(* com.binary.spring.security.controller.*.*(..))")
	public void printAfter(JoinPoint joinpoint) {
		System.out.println("Method execution Ended " +joinpoint.getSignature());
	}
	
}
