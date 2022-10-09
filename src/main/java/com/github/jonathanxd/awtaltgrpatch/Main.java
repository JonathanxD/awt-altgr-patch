package com.github.jonathanxd.awtaltgrpatch;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

public class Main {

    public static void premain(String agentArgs, Instrumentation inst) {
        install(inst);
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        install(inst);
    }

    private static void install(Instrumentation inst) {
        new AgentBuilder.Default()
                .type(ElementMatchers.namedOneOf("sun.awt.X11.XWindow"))
                .transform((builder, typeDescription, classLoader, module, protectionDomain) ->
                        builder.method(ElementMatchers.<MethodDescription>named("getModifiers")
                                .and(ElementMatchers.isStatic())
                                .and(it ->it.getParameters().size() == 4)
                                .and(ElementMatchers.isPrivate()))
                                .intercept(Advice.to(XWindowAdvice.class))
                )
                .with(AgentBuilder.TypeStrategy.Default.REBASE)
                .with(AgentBuilder.RedefinitionStrategy.REDEFINITION)
                .with(AgentBuilder.Listener.StreamWriting.toSystemOut())
                .ignore(ElementMatchers.none())
                .installOn(inst);

    }
}
