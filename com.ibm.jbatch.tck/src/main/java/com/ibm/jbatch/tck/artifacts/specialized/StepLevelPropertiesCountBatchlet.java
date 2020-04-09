/*
 * Copyright 2013 International Business Machines Corp.
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

import java.util.Iterator;
import java.util.Properties;

import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

@javax.inject.Named("stepLevelPropertiesCountBatchlet")
public class StepLevelPropertiesCountBatchlet extends AbstractBatchlet {

    @Inject
    JobContext JobCtx;

    @Inject
    StepContext StepCtx;

    public static final String SHOULD_BE_UNAVAILABLE_PROP_PREFIX = "com.ibm.jbatch.tck.tests.jslxml.StepLevelPropertiesTests";

    public static String GOOD_EXIT_STATUS = "VERY GOOD INVOCATION";

    @Override
    public String process() throws Exception {

        Properties properties = StepCtx.getProperties();

        StringBuffer badExitStatus = new StringBuffer();

        JobCtx.setExitStatus(String.valueOf(properties.size()));

        /*
         * Verify that all three step-level properties were seen.
         */

        int found = 0;
        String[] propNames = new String[]{"foo", "super", "me"};
        String[] propVals = new String[]{"bar", "hero", "too"};
        for (int i = 0; i < 3; i++) {
            String val = properties.getProperty(propNames[i]);
            if (propVals[i].equals(val)) {
                found++;
            } else {
                badExitStatus.append("For " + propNames[0] + ", found: " + val + ":");
            }
        }

        /**
         * Verify that none of the TCK-set properties were seen
         */
        boolean seenBadProp = false;

        Iterator<Object> iter = properties.keySet().iterator();
        while (iter.hasNext()) {
            String nextProp = (String) iter.next();
            if (nextProp.startsWith(SHOULD_BE_UNAVAILABLE_PROP_PREFIX)) {
                seenBadProp = true;
                badExitStatus.append("Saw unexpected property: " + nextProp);
            }
        }

        if (found == 3 && !seenBadProp) {
            JobCtx.setExitStatus(GOOD_EXIT_STATUS);
        } else {
            JobCtx.setExitStatus(badExitStatus.toString());
        }

        return GOOD_EXIT_STATUS;
    }

}
