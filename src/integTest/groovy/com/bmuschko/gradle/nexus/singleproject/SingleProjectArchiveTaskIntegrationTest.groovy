/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bmuschko.gradle.nexus.singleproject

import com.bmuschko.gradle.nexus.ExtraArchivePlugin
import org.gradle.tooling.model.GradleProject
import org.gradle.tooling.model.Task

/**
 * Nexus plugin archive task integration tests.
 *
 * @author Benjamin Muschko
 */
class SingleProjectArchiveTaskIntegrationTest extends SingleProjectBuildIntegrationTest {
    def "Adds sources and Javadoc JAR tasks by default for Java project"() {
        when:
        GradleProject project = runTasks(integTestDir, 'tasks')

        then:
        Task sourcesJarTask = project.tasks.find { task -> task.name == ExtraArchivePlugin.SOURCES_JAR_TASK_NAME }
        sourcesJarTask
        sourcesJarTask.description == 'Assembles a jar archive containing the main sources of this project.'
        Task javadocJarTask = project.tasks.find { task -> task.name == ExtraArchivePlugin.JAVADOC_JAR_TASK_NAME }
        javadocJarTask
        javadocJarTask.description == 'Assembles a jar archive containing the generated Javadoc API documentation of this project.'
        !project.tasks.find { task -> task.name == ExtraArchivePlugin.TESTS_JAR_TASK_NAME }
    }

    def "Adds sources and Javadoc JAR tasks by default for Groovy project"() {
        when:
        GradleProject project = runTasks(integTestDir, 'tasks')

        then:
        Task sourcesJarTask = project.tasks.find { task -> task.name == ExtraArchivePlugin.SOURCES_JAR_TASK_NAME }
        sourcesJarTask
        sourcesJarTask.description == 'Assembles a jar archive containing the main sources of this project.'
        Task javadocJarTask = project.tasks.find { task -> task.name == ExtraArchivePlugin.JAVADOC_JAR_TASK_NAME }
        javadocJarTask
        javadocJarTask.description == 'Assembles a jar archive containing the generated Javadoc API documentation of this project.'
        !project.tasks.find { task -> task.name == ExtraArchivePlugin.TESTS_JAR_TASK_NAME }
    }

    def "Adds tests JAR task if configured"() {
        when:
        buildFile << """
extraArchive {
    tests = true
}
"""
        GradleProject project = runTasks(integTestDir, 'tasks')

        then:
        Task sourcesJarTask = project.tasks.find { task -> task.name == ExtraArchivePlugin.SOURCES_JAR_TASK_NAME }
        sourcesJarTask
        sourcesJarTask.description == 'Assembles a jar archive containing the main sources of this project.'
        Task javadocJarTask = project.tasks.find { task -> task.name == ExtraArchivePlugin.JAVADOC_JAR_TASK_NAME }
        javadocJarTask
        javadocJarTask.description == 'Assembles a jar archive containing the generated Javadoc API documentation of this project.'
        Task testsJarTask = project.tasks.find { task -> task.name == ExtraArchivePlugin.TESTS_JAR_TASK_NAME}
        testsJarTask
        testsJarTask.description == 'Assembles a jar archive containing the test sources of this project.'
    }

    def "Disables additional JAR creation"() {
        when:
        buildFile << """
extraArchive {
    sources = false
    javadoc = false
}
"""
        GradleProject project = runTasks(integTestDir, 'tasks')

        then:
        !project.tasks.find { task -> task.name == ExtraArchivePlugin.SOURCES_JAR_TASK_NAME}
        !project.tasks.find { task -> task.name == ExtraArchivePlugin.JAVADOC_JAR_TASK_NAME}
        !project.tasks.find { task -> task.name == ExtraArchivePlugin.TESTS_JAR_TASK_NAME}
    }
}
