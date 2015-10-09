package OpenstackAuth;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.*;
import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;


public class Utils {

    /*
     * NullFilter for disabling log output
     */
    private static class NullFilter implements Filter {
        // isLoggable says everything is NOT logabble.
        public boolean isLoggable(LogRecord record) {
            //System.out.println("DEBUG: " + record.getLevel());
            return false;
        }
    }

    public static void setupLog() {
        /*
         * research purpose code chunk to see all log handlers in the system.
         * LogManager lm  = LogManager.getLogManager();
         * for (Enumeration l = lm.getLoggerNames();l.hasMoreElements();) {
         *    String s = (String) l.nextElement();
         *    System.out.println(s);
         * }
         */
        Logger l = Logger.getLogger("os");
        l.setFilter(new NullFilter());
        System.out.println("DEBUG: Filter : " + l.getFilter());
        for (Handler h : l.getHandlers()) {
            System.out.println("DEBUG: Handlers: " + h);
        }


    }

    public static void printJson(Object o) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            //System.out.println(mapper.writeValueAsString(o));
            /*
              DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
              pp.indentArrayWith(new Lf2SpacesIndenter());
              System.out.println(mapper.writer(pp).writeValueAsString(o));
            */
            System.out.println(mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(o));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String returnJson(Object o) {
        ObjectMapper mapper = new ObjectMapper();
        String output="";
        try {
            //System.out.println(mapper.writeValueAsString(o));
            /*
              DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
              pp.indentArrayWith(new Lf2SpacesIndenter());
              System.out.println(mapper.writer(pp).writeValueAsString(o));
            */
            output= mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    //from openstack-java-sdk/openstack-examples/../SwiftExample.java
    public static void write(InputStream is, String path) {
        try {
            OutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(path));
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                stream.write(buffer, 0, len);
            }
            stream.close();
        } catch(IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
