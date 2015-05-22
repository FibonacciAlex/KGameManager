package com.kola.core.data;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Description:定义增删改后修改XML文件并保存
 * 
 * @author dengzhifeng <a
 *         href="mailto:dengzhifeng@revenco.com">dengzhifeng@revenco.com</a>
 */
/*@Component
@Aspect*/
public class CheckDataBase {

//	@Pointcut("execution(* com.kola.core.service.*.update*(..))||execution(* com.kola.core.service.*.save*(..))||execution(* com.kola.core.service.*.delete*(..))")
	public void getPointcut(){
	}
	/**
	 * 定义通知advice@AfterReturning),
	 */
//	@AfterReturning(pointcut = "getPointcut()")
	public void afterReturning() {
		System.out.println("之后执行");
		DataBaseKit.getInstance().updateDataBase();
	}

}
