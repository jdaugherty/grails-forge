package org.grails.forge.cli.command

import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import org.grails.forge.cli.Application
import org.grails.forge.cli.CommandFixture
import org.grails.forge.cli.CommandSpec
import org.grails.forge.util.VersionInfo
import spock.lang.AutoCleanup
import spock.lang.Shared

class ApplicationCommandSpec extends CommandSpec implements CommandFixture {

    @Shared
    @AutoCleanup
    ApplicationContext ctx = ApplicationContext.run(Environment.CLI)

    @Shared
    @AutoCleanup
    ApplicationContext beanContext = ApplicationContext.run()

    void "print version info via: grails #args"() {
        given:
        ByteArrayOutputStream out = new ByteArrayOutputStream()
        System.setOut(new PrintStream(out))

        when:
        PicocliRunner.run(Application, ctx, args)

        then:
        noExceptionThrown()
        out.toString().contains("Grails Version: " + VersionInfo.getGrailsVersion())
        out.toString().contains("JVM Version: " + Objects.requireNonNullElse(System.getProperty("java.version"), "<a valid java.home was not found>" ))

        where:
        args        | _
        "--version" | _
        "-V"        | _
    }
}
