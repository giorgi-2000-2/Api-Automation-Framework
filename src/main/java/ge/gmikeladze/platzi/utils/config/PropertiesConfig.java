package ge.gmikeladze.platzi.utils.config;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ge.gmikeladze.platzi.utils.reporter.IReportConfig;
import ge.gmikeladze.platzi.utils.reporter.ReportEngine;
import java.util.Properties;


@Singleton
public class PropertiesConfig implements IConfigForData, IConfigForRequest, IReportConfig {
    private static final String FILE = "config.properties";
    private final Properties props;

    @Inject
    public PropertiesConfig() {
        Properties fromFile = ConfigSource.fromClasspath(FILE);
        this.props = fromFile;
    }

    PropertiesConfig(Properties props) {
        this.props = props;
    }

    @Override public String baseUrl(){
        return require("base.url");
    }
    @Override public int responseTimeLimit(){
        return requireInt("response.time");
    }
    @Override public int maxBodyLengthInMessage(){
        return requireInt("max.body.length.in.message");
    }
    @Override public String categoryName(){
        return require("category.name");
    }
    @Override public String categoryImage(){
        return require("category.image");
    }
    @Override public String productName(){
        return require("product.name");
    }
    @Override public String userAvatar(){
        return require("user.avatar");
    }
    @Override public int categoryListLimit(){
        return requireInt("limit");
    }

    @Override public ReportEngine reportEngine(){
        String fromSystem = System.getProperty("report.engine");
        return ReportEngine.from(isUsable(fromSystem) ? fromSystem : resolve("report.engine"));
    }

    @Override
    public int maxStepTitle() {
        return requireInt("max.step.title");
    }

    private String resolve(String key) {
        return props.getProperty(key);

    }

    private String require(String key) {
        String value = resolve(key);
        if (!isUsable(value)) {
            throw new IllegalStateException("კონფიგის გასაღები აკლია " + key);
        }
        return value.trim();
    }

    private int requireInt(String key) {
        String value = require(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalStateException(
                    "კონფიგის გასაღები " + key + " არ არის რიცხვი: " + value + e);
        }
    }

    private static boolean isUsable(String value) {
        return value != null && !value.trim().isEmpty();
    }


}
