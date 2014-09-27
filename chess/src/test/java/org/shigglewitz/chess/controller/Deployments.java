package org.shigglewitz.chess.controller;

import java.io.File;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.shigglewitz.chess.maven.Properties;

public class Deployments {
    private static WebArchive controllerWar;
    private static final String WEBAPP_SRC = "src/main/webapp";

    @Deployment(testable = false, name = "controller", managed = true, order = 1)
    @TargetsContainer("tomcat")
    public static WebArchive createControllerDeployment() {
        if (controllerWar != null) {
            return controllerWar;
        }

        controllerWar = ShrinkWrap.create(WebArchive.class, "Chess.war");
        controllerWar.setWebXML(new File(WEBAPP_SRC, "WEB-INF/web.xml"));
        controllerWar.addPackage(GameController.class.getPackage());
        String[] jsps = getJsps(new String[] { "viewChess" });

        for (String jsp : jsps) {
            controllerWar.addAsWebResource(new File(WEBAPP_SRC, jsp), jsp);
        }

        return controllerWar;
    }

    private static String[] getJsps(String[] jspNames) {
        for (int i = 0; i < jspNames.length; i++) {
            // "/WEB-INF/jsp/" + name + ".jsp"
            jspNames[i] = Properties.VIEW_FILE_PATH + jspNames[i]
                    + Properties.VIEW_EXTENSION;
        }

        return jspNames;
    }
}
