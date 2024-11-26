package org.grails.forge.feature.grails

import org.grails.forge.BeanContextSpec
import org.grails.forge.BuildBuilder
import org.grails.forge.application.ApplicationType
import org.grails.forge.fixture.CommandOutputFixture
import org.grails.forge.options.JdkVersion
import org.grails.forge.options.Options
import org.grails.forge.options.TestFramework

class GrailsGradlePluginSpec extends BeanContextSpec implements CommandOutputFixture {

    void "test build gradle file and gradle properties"() {
        when:
        final def output = generate(ApplicationType.WEB, new Options(TestFramework.SPOCK, JdkVersion.JDK_11))
        final String gradleProps = output["gradle.properties"]

        then:
        gradleProps.contains("grailsGradlePluginVersion=6.2.3")
        gradleProps.contains("grailsVersion=6.2.2")
    }

    void "test dependencies are present for buildSrc"() {
        when:
        final String template = new BuildBuilder(beanContext)
                .renderBuildSrc()

        then:
        template.contains('implementation "org.grails:grails-gradle-plugin:6.2.3"')
    }

    void "test buildSrc is present for buildscript dependencies"() {
        given:
        final def output = generate(ApplicationType.WEB, new Options(TestFramework.SPOCK, JdkVersion.JDK_11))
        final def buildGradle = output["build.gradle"]

        expect:
        buildGradle != null
        buildGradle.contains("classpath \"org.grails:grails-gradle-plugin:6.2.3\"")

    }

    void "test dependencies are present for Gradle"() {
        when:
        final String template = new BuildBuilder(beanContext)
                .applicationType(ApplicationType.PLUGIN)
                .render()

        then:
        template.contains("apply plugin: \"org.grails.grails-plugin\"")
    }

}
