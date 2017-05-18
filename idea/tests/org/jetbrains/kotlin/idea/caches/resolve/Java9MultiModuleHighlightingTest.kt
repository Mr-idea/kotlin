/*
 * Copyright 2010-2017 JetBrains s.r.o.
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
 */

package org.jetbrains.kotlin.idea.caches.resolve

import com.intellij.openapi.module.Module
import org.jetbrains.kotlin.idea.test.PluginTestCaseBase
import org.jetbrains.kotlin.test.KotlinTestUtils
import org.jetbrains.kotlin.test.MockLibraryUtil
import org.jetbrains.kotlin.test.TestJdkKind.FULL_JDK_9

class Java9MultiModuleHighlightingTest : AbstractMultiModuleHighlightingTest() {
    override fun getTestDataPath(): String = PluginTestCaseBase.getTestDataPathBase() + "/multiModuleHighlighting/java9/"

    override fun checkHighlightingInAllFiles(shouldCheckFile: () -> Boolean) {
        if (KotlinTestUtils.getJdk9HomeIfPossible() == null) {
            // Skip this test if no Java 9 is found
            return
        }
        super.checkHighlightingInAllFiles(shouldCheckFile)
    }

    private fun module(name: String): Module = super.module(name, FULL_JDK_9, false)

    fun testSimpleModuleExportsPackage() {
        module("main").addDependency(module("dependency"))
        checkHighlightingInAllFiles()
    }

    fun testSimpleLibraryExportsPackage() {
        val jdk9Home = KotlinTestUtils.getJdk9HomeIfPossible() ?: return
        val library = MockLibraryUtil.compileJvmLibraryToJar(
                testDataPath + "${getTestName(true)}/library", "library",
                extraOptions = listOf("-jdk-home", jdk9Home.path),
                useJava9 = true
        )

        module("main").addLibrary(library, "library")
        checkHighlightingInAllFiles()
    }

    fun testNamedDependsOnUnnamed() {
        module("main").addDependency(module("dependency"))
        checkHighlightingInAllFiles()
    }

    fun testUnnamedDependsOnNamed() {
        module("main").addDependency(module("dependency"))
        checkHighlightingInAllFiles()
    }

    fun testDeclarationKinds() {
        module("main").addDependency(module("dependency"))
        checkHighlightingInAllFiles()
    }

    fun testExportsTo() {
        val d = module("dependency")
        module("first").addDependency(d)
        module("second").addDependency(d)
        module("unnamed").addDependency(d)
        checkHighlightingInAllFiles()
    }
}
