// package com.relaxed.test.job;
//
// import com.relaxed.common.job.XxlJobLogAppender;
// import lombok.extern.slf4j.Slf4j;
// import org.apache.logging.log4j.Level;
// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.core.Appender;
// import org.apache.logging.log4j.core.Filter;
// import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
// import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
// import org.apache.logging.log4j.core.config.Configuration;
// import org.apache.logging.log4j.core.config.LoggerConfig;
// import org.apache.logging.log4j.core.config.Property;
// import org.apache.logging.log4j.core.filter.ThreadContextMapFilter;
// import org.apache.logging.log4j.core.layout.PatternLayout;
// import org.apache.logging.log4j.core.util.KeyValuePair;
// import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
// import org.springframework.context.ApplicationListener;
// import org.springframework.core.env.ConfigurableEnvironment;
// import org.apache.logging.log4j.core.LoggerContext;
// import org.springframework.stereotype.Component;
//
// import java.nio.charset.Charset;
// import java.util.Iterator;
// import java.util.Map;
//
/// **
// * @author Yakir
// * @Topic XXlJobAppenderPlugin
// * @Description https://blog.csdn.net/zhangweiocp/article/details/124078243
// * @date 2024/12/20 10:11
// * @Version 1.0
// */
// @Component
// @Slf4j
// public class XXlJobAppenderPlugin implements
// ApplicationListener<ApplicationEnvironmentPreparedEvent> {
// //appender名称
// private static final String APPENDER_NAME = "XXL-JOB-CONSOLE";
// @Override
// public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
// generateAppender(event.getEnvironment());
// }
//
//
// private void generateAppender(ConfigurableEnvironment environment){
// String applicationName = environment.getProperty("spring.application.name");
// LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
// Configuration configuration = loggerContext.getConfiguration();
// synchronized (configuration) {
// try {
// if (configuration.getAppender(APPENDER_NAME) != null) {
// return;
// }
// final PatternLayout layout = PatternLayout.newBuilder()
// .withCharset(Charset.forName("UTF-8"))
// .withConfiguration(configuration)
// .withPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n")
// .build();
//// final TriggeringPolicy policy = TimeBasedTriggeringPolicy.newBuilder()
//// .withModulate(true)
//// .withInterval(1)
//// .build();
// //过滤器
// // final KeyValuePair[] pairs =
// {KeyValuePair.newBuilder().setKey("domainId").setValue("").build()};
// // final Filter filter = ThreadContextMapFilter.createFilter(pairs, null,
// Filter.Result.ACCEPT, Filter.Result.DENY);
// // XxlJobLog4JAppender xxlJobLog4JAppender = new XxlJobLog4JAppender(APPENDER_NAME,
// null, layout, true, Property.EMPTY_ARRAY);
// https://blog.51cto.com/u_15951177/6032633
//
// configuration.addAppender(xxlJobLog4JAppender);
// // 获取根日志器配置
// // 获取根日志器配置
// LoggerConfig loggerConfig = configuration.getRootLogger();
// loggerConfig.addAppender(xxlJobLog4JAppender, Level.DEBUG, null);
//// Map<String, LoggerConfig> loggers = configuration.getLoggers();
//// Iterator<Map.Entry<String, LoggerConfig>> iterator = loggers.entrySet().iterator();
//// while (iterator.hasNext()) {
//// Map.Entry<String, LoggerConfig> next = iterator.next();
//// //给logger关联 appender
//// next.getValue().addAppender(rollingFileAppender, null, null);
//// }
// //更新 loggerContext
// loggerContext.updateLoggers(configuration);
//
//
// }catch (Exception e){
// log.error("初始化LogAppender失败,名称:{}",APPENDER_NAME,e);
// }
//
// }
// }
//
// }
