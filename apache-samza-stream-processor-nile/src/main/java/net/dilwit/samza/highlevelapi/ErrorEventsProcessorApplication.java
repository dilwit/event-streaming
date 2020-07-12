package net.dilwit.samza.highlevelapi;

import joptsimple.OptionSet;
import net.dilwit.samza.highlevelapi.service.ErrorEventsCounterStreamProcessor;
import org.apache.samza.config.Config;
import org.apache.samza.runtime.LocalApplicationRunner;
import org.apache.samza.util.CommandLine;

public class ErrorEventsProcessorApplication {

    /**
     * ./gradlew run
     * --args="--config job.config.loader.factory=org.apache.samza.config.loaders.PropertiesConfigLoaderFactory
     * --config job.config.loader.properties.path=$BASE_DIR/src/main/config/word-count.properties"
     * @param args
     */
    public static void main(String[] args) {
        CommandLine cmdLine = new CommandLine();
        OptionSet options = cmdLine.parser().parse(args);
        Config config = cmdLine.loadConfig(options);
        LocalApplicationRunner runner = new LocalApplicationRunner(new ErrorEventsCounterStreamProcessor(), config);
        runner.run();
        runner.waitForFinish();
    }
}
