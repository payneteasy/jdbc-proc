package com.googlecode.jdbcproc.daofactory.impl;

import org.slf4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Logs out in debug mode all methods invoking
 */
public class DebugLogInvocationHandler implements InvocationHandler {

    public DebugLogInvocationHandler(Object aDelegate, Logger aLog) {
        theDelegate = aDelegate;
        theLog = aLog;
    }

    public Object invoke(Object aProxy, Method aMethod, Object[] aArgs) throws Throwable {
        StringBuilder sb = new StringBuilder();
        sb.append("    ");
        sb.append(theDelegate.getClass().getSimpleName());
        sb.append(".");
        sb.append(aMethod.getName());
        sb.append("(");
        if(aArgs!=null && aArgs.length>0) {
            sb.append(aArgs[0]);
            for(int i=1; i<aArgs.length; i++) {
                sb.append(", ");
                sb.append(aArgs[i]);
            }
        }
        sb.append(")");
        theLog.debug(sb.toString());
        
        return aMethod.invoke(theDelegate, aArgs);
    }

    
    private final Object theDelegate;
    private final Logger theLog;
}
