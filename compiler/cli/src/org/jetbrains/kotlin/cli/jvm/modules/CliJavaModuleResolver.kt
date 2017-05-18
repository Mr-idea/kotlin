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

package org.jetbrains.kotlin.cli.jvm.modules

import com.intellij.ide.highlighter.JavaClassFileType
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.kotlin.resolve.jvm.modules.JavaModule
import org.jetbrains.kotlin.resolve.jvm.modules.JavaModuleGraph
import org.jetbrains.kotlin.resolve.jvm.modules.JavaModuleResolver

class CliJavaModuleResolver(
        override val moduleGraph: JavaModuleGraph,
        private val javaModules: List<JavaModule>
) : JavaModuleResolver {
    override fun findJavaModule(file: VirtualFile): JavaModule? {
        val isBinary = file.fileType == JavaClassFileType.INSTANCE
        return javaModules.firstOrNull { module ->
            module.isBinary == isBinary && VfsUtilCore.isAncestor(module.moduleRoot, file, false)
        }
    }
}
