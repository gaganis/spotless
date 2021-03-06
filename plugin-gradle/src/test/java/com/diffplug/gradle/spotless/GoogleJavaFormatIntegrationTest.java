/*
 * Copyright 2016 DiffPlug
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.diffplug.gradle.spotless;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class GoogleJavaFormatIntegrationTest extends GradleIntegrationTest {
	// TODO: This test throws an exception because google-java-format-1.0 doesn't have
	// RemoveUnusedImports.java (only 1.1+ does), but despite that, the test passes!
	// Discover why this test passes and/or fix it or remove it.
	@Test
	public void integration() throws IOException {
		write("build.gradle",
				"plugins {",
				"    id 'com.diffplug.gradle.spotless'",
				"}",
				"repositories { mavenCentral() }",
				"",
				"spotless {",
				"    java {",
				"        target file('test.java')",
				"        googleJavaFormat('1.1')",
				"    }",
				"}");
		String input = getTestResource("java/googlejavaformat/JavaCodeUnformatted.test");
		write("test.java", input);
		gradleRunner().withArguments("spotlessApply").build();

		String result = read("test.java");
		String output = getTestResource("java/googlejavaformat/JavaCodeFormatted.test");
		Assert.assertEquals(output, result);

		checkRunsThenUpToDate();

		replace("build.gradle",
				"googleJavaFormat('1.1')",
				"googleJavaFormat('1.0')");
		checkRunsThenUpToDate();
	}
}
