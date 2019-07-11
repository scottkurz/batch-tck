/*
 * Copyright 2012 International Business Machines Corp.
 * 
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License, 
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
*/
package com.ibm.jbatch.tck.artifacts.specialized;

import java.util.Properties;
import java.util.logging.Logger;

import javax.batch.api.chunk.listener.RetryReadListener;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

import org.testng.Reporter;

import com.ibm.jbatch.tck.artifacts.reusable.MyParentException;

@javax.inject.Named("numbersRetryReadListener")
public class NumbersRetryReadListener implements RetryReadListener {
	 private final static String sourceClass = NumbersRetryReadListener.class.getName();
	    private final static Logger logger = Logger.getLogger(sourceClass);
	    
	    @Inject
	    StepContext stepCtx;

	    @Override
	    public void onRetryReadException(Exception e) {
	    	Reporter.log("In onRetryReadException()" + e);
	    	logger.finer("In onRetryReadException()" + e);
	    	((Properties)stepCtx.getTransientUserData()).setProperty("retry.read.exception.invoked", "true");
	        if (e instanceof MyParentException){
	        	((Properties)stepCtx.getTransientUserData()).setProperty("retry.read.exception.match", "true");
	        }
	        else {
	        	((Properties)stepCtx.getTransientUserData()).setProperty("retry.read.exception.match", "false");
	        }
	    }
}

