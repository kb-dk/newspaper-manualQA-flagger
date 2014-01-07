package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.AutonomousComponentUtils;
import dk.statsbiblioteket.medieplatform.autonomous.CallResult;
import dk.statsbiblioteket.medieplatform.autonomous.RunnableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ManualQAFlaggerComponent {

    private static Logger log = LoggerFactory.getLogger(ManualQAFlaggerComponent.class);

         /**
          * The class must have a main method, so it can be started as a command line tool
      *
      * @param args the arguments.
      *
      * @throws Exception
      * @see dk.statsbiblioteket.medieplatform.autonomous.AutonomousComponentUtils#parseArgs(String[])
      */
     public static void main(String[] args) throws Exception {
         log.info("Starting with args {}", args);

         //Parse the args to a properties construct
         Properties properties = AutonomousComponentUtils.parseArgs(args);

         //make a new runnable component from the properties
         RunnableComponent component = new ManualQAFlaggerRunnableComponent(properties);

         CallResult result = AutonomousComponentUtils.startAutonomousComponent(properties, component);
         log.info(result.toString());
         System.exit(result.containsFailures());
     }
}
