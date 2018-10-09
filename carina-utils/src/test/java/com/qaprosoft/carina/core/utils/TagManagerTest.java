/*******************************************************************************
 * Copyright 2013-2018 QaProSoft (http://www.qaprosoft.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.qaprosoft.carina.core.utils;

import com.qaprosoft.carina.core.foundation.utils.tag.*;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * Tests for {@link TagManager}
 */
public class TagManagerTest {

    private static final String TAG_NAME = "tag1";
    private static final String TAG_NAME2 = "tag2";
    private static final String TAG_VALUE = "testTag1";
    private static final String TAG_VALUE2 = "testTag2";


    @Test
    @TestPriority(Priority.P2)
    public void testPriority() {
        ITestResult result = Reporter.getCurrentTestResult();
        String priority = PriorityManager.getPriority(result);
        Assert.assertEquals(priority, "P2");
    }

    @Test
    @TestPriority(Priority.P0)
    public void testPriorityCompliance() {
        ITestResult result = Reporter.getCurrentTestResult();
        String priority = PriorityManager.getPriority(result);
        Assert.assertEquals(priority, "P0");
    }


    @Test
    @TestPriority(value = Priority.P1)
    @TestTag(name = TAG_NAME, value = TAG_VALUE)
    public void testTags() {
        ITestResult result = Reporter.getCurrentTestResult();
        Map<String, String> tag = TagManager.getTag(result);
        Assert.assertTrue(tag.containsKey(TAG_NAME));
        Assert.assertEquals(tag.get(TAG_NAME), TAG_VALUE);
    }

    @Test
    @TestPriority(Priority.P2)
    @TestTag(name = TAG_NAME, value = TAG_VALUE)
    @TestTag(name = TAG_NAME2, value = TAG_VALUE2)
    public void testRepeatableTags() {
        ITestResult result = Reporter.getCurrentTestResult();
        Map<String, String> tags = TagManager.getTag(result);
        Assert.assertTrue(tags.containsKey(TAG_NAME));
        Assert.assertEquals(tags.get(TAG_NAME), TAG_VALUE);
        Assert.assertTrue(tags.containsKey(TAG_NAME2));
        Assert.assertEquals(tags.get(TAG_NAME2), TAG_VALUE2);
    }

    @Test
    @TestPriority(Priority.P2)
    @TestTag(name = TAG_NAME2, value = TAG_VALUE2)
    @TestTag(name = TAG_NAME, value = TAG_VALUE)
    @TestTag(name = "priority", value = "P0")
    @TestTag(name = "feature", value = "feature1")
    public void testForbiddenTags() {
        ITestResult result = Reporter.getCurrentTestResult();
        Map<String, String> tags = TagManager.getTag(result);
        Assert.assertFalse(tags.containsKey("priority"));
        Assert.assertFalse(tags.containsKey("feature"));
        Assert.assertTrue(tags.containsKey(TAG_NAME));
        Assert.assertEquals(tags.get(TAG_NAME), TAG_VALUE);
        Assert.assertTrue(tags.containsKey(TAG_NAME2));
        Assert.assertEquals(tags.get(TAG_NAME2), TAG_VALUE2);
        Assert.assertEquals(tags.size(), 2);
    }

    @Test
    @TestPriority(Priority.P1)
    @TestTag(name = "priority", value = "P5")
    public void testForbiddenPriorityTag() {
        ITestResult result = Reporter.getCurrentTestResult();
        String priority = PriorityManager.getPriority(result);
        Assert.assertEquals(priority, "P1");
        Map<String, String> tags = TagManager.getTag(result);
        Assert.assertFalse(tags.containsKey("priority"));
        Assert.assertEquals(tags.size(), 0);
    }

    @Test
    @TestPriority(Priority.P2)
    @TestTag(name = "feature", value = "P5")
    public void testForbiddenFeatureTag() {
        ITestResult result = Reporter.getCurrentTestResult();
        String priority = PriorityManager.getPriority(result);
        Assert.assertEquals(priority, "P2");
        Map<String, String> tags = TagManager.getTag(result);
        Assert.assertFalse(tags.containsKey("feature"));
        Assert.assertEquals(tags.size(), 0);
    }

}
