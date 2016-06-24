package com.jlb.tools.logging;

/**
 * This class contains SIGMA constants of the log4j.properties file. Those
 * properties are used to configure the ISigmaLogger objects instanciated by
 * SigmaLoggerFactory.
 */
public interface Log4jProperties {
	/**
	 * Key used for printing the host name in the messages thanks to the
	 * <i>Mapped Diagnostic Context</i> (MDC). You can print the host name by
	 * using placeholders like "%X{hostname}" in the appenders' layout string.
	 */
	public static final String	MDC_KEY_HOSTNAME			= "hostname";

	/**
	 * Key used for printing host's IP in the messages thanks to the <i>Mapped
	 * Diagnostic Context</i> (MDC). You can print the host's IP by using
	 * placeholders like "%X{hostaddress}" in the appenders' layout string.
	 */
	public static final String	MDC_KEY_HOSTADDRESS			= "hostaddress";

	/**
	 * Key used for printing the receiver in the messages thanks to the
	 * <i>Mapped Diagnostic Context</i> (MDC). You can print the receiver by
	 * using placeholders like "%X{receiver}" in the appenders' layout string.
	 * The receiver is traditionnaly the object from where the messages are
	 * logged.
	 */
	public static final String	MDC_KEY_RECEIVER			= "receiver";

	/**
	 * Key used for printing the current receiver's method in the messages
	 * thanks to the <i>Mapped Diagnostic Context</i> (MDC). You can print the
	 * receiver's running method by using placeholders like "%X{method}" in the
	 * appenders' layout string.
	 */
	public static final String	MDC_KEY_METHOD				= "method";

	/** Default layout for messages if no default appender is found. */
	public static final String	LOG_PATTERN					= "log.pattern";

	/** This property defines the desired encoding for output files. */
	public static final String	LOG_ENCODING				= "log.encoding";

	/** Default directory where to place log files. */
	public static final String	LOG_DIR						= "log.dir";

	/** Used to configure MaxFileSize property of all RollingFileAppender. */
	public static final String	ROLLING_MAX_FILE_SIZE		= "rolling.maxFileSize";

	/**
	 * Used to configure the MaxBackupIndex property of all RollingFileAppender.
	 */
	public static final String	ROLLING_MAX_BACKUP_INDEX	= "rolling.maxBackupIndex";
}
