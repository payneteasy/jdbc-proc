/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.googlecode.jdbcproc.daofactory.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Delegates information about all methods invocation up.
 */
public abstract class AppendableLogInvocationHandler implements InvocationHandler {

    private final Object delegate;
    
    public AppendableLogInvocationHandler(Object delegate) {
        this.delegate = delegate;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        StringBuilder sb = new StringBuilder();
        sb.append("\n    ");
        sb.append(delegate.getClass().getSimpleName());
        sb.append('.');
        sb.append(method.getName());
        sb.append('(');
        if (args != null && args.length > 0) {
            sb.append(args[0]);
            for (int i = 1; i < args.length; i++) {
                sb.append(", ");
                sb.append(args[i]);
            }
        }
        sb.append(')');
        
        append(sb.toString());
        return method.invoke(delegate, args);
    }
    
    public abstract void append(String str);
}
