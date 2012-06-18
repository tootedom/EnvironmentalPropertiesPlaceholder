package org.greencheek.utils.environment.propertyplaceholder.examples.spring.profile;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String args[]) {
        Main m = new Main();
        m.runTestExample();

    }

    private void runTestExample() {
        runGenericTest("TEST");
        runGenericTest("DEV");
        runGenericTest("default");
    }

    private void runGenericTest(String profile) {
        System.setProperty("ENV", "dev");
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        if(!profile.equals("default"))
            ctx.getEnvironment().setActiveProfiles(profile);

        // Add the Property Source to the app context before the refresh.
        AppBootstrap.initialise(ctx);

        ctx.scan("org.greencheek.utils.environment.propertyplaceholder.examples.spring.profile");
        ctx.refresh();

        EnvBean cb = (EnvBean) ctx.getBean("envBean");
        print(profile, cb);
    }

    private void print(String env, EnvBean cb) {
        System.out.println("==== Test result ["+env+"] ====");
        System.out.println(
                String.format("running the %s test resulted in the message %s", env, cb.getMessage()));
    }


}

