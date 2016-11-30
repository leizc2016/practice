package com.lzc.ds;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

@Intercepts({@Signature(type = Executor.class, method ="query", args = {MappedStatement.class, Object.class, RowBounds.class,  
    ResultHandler.class })})  
public class MyBaittsIntercepter implements Interceptor{

	public Object intercept(Invocation invocation) throws Throwable {
		DataSourceContextHolder.setDSKey("MyDataSource");
		
		final Object[] args = invocation.getArgs();  
		MappedStatement ms = (MappedStatement) args[0];  
        Object parameterObject = args[1];  
        BoundSql boundSql = ms.getBoundSql(parameterObject); 
		
		Object result= invocation.proceed();
		System.out.println("result="+boundSql.getSql());
		return result;
	}

	public Object plugin(Object target) {
		System.out.println("target="+target);
		return Plugin.wrap(target, this); 
	}

	public void setProperties(Properties arg0) {
		// TODO Auto-generated method stub
		
	}

}
