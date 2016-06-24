package com.jlb.tools.logging;

/**
 * This interface defines available operations of a logger. Meta information are
 * added to the messages provided in parameter of defined methods. Those
 * information are mainly : Date/Time, machine ID (IP address and/or host name)
 * thread's and logger's names and thread's stack (only in case of errors, if an
 * exception is provided).
 */
public interface ILogger {

	/**
	 * Returns whether the level "DEBUG" is enabled or not.
	 * 
	 * @return True if DEBUG-level is enabled, false otherwise.
	 */
	public boolean isDebugEnabled();

	/**
	 * Prints a message in all appenders of the underlying log4j logger. This
	 * message will appear only if the logger's level is at least DEBUG. All
	 * messages will be printed at least in the high level appender which is a
	 * DailyRollingFileAppender.
	 *
	 * @param receiver
	 *            The object that is related to the logged message.
	 * @param message
	 *            The message to be logged.
	 */
	public abstract void debug(Object receiver, String message);

	/**
	 * Prints a message in all appenders of the underlying log4j logger. This
	 * message will appear only if the logger's level is at least INFO. All
	 * messages will be printed at least in the high level appender which is a
	 * DailyRollingFileAppender.
	 *
	 * @param receiver
	 *            The object that is related to the logged message.
	 * @param message
	 *            The message to be logged.
	 *
	 */
	public abstract void info(Object receiver, String message);

	/**
	 * Prints a message in all appenders of the underlying log4j logger. This
	 * message will appear only if the logger's level is at least WARN. All
	 * messages will be printed at least in the high level appender which is a
	 * DailyRollingFileAppender.
	 *
	 * @param receiver
	 *            The object that is related to the logged message.
	 * @param message
	 *            The message to be logged.
	 */
	public abstract void warning(Object receiver, String message);

	/**
	 * Prints a message in all appenders of the underlying log4j logger. This
	 * message will appear only if the logger's level is at least ERROR. All
	 * messages will be printed at least in the high level appender which is a
	 * DailyRollingFileAppender.
	 *
	 * @param receiver
	 *            The object that is related to the logged message.
	 * @param message
	 *            The message to be logged.
	 * @param exception
	 *            An optionnal exception you want to print (as a stack trace).
	 */
	public abstract void error(Object receiver, String message, Throwable exception);

	/**
	 * Prints a message in all appenders of the underlying log4j logger. This
	 * message will appear only if the logger's level is at least FATAL. All
	 * messages will be printed at least in the high level appender which is a
	 * DailyRollingFileAppender.
	 *
	 * @param receiver
	 *            The object that is related to the logged message.
	 * @param message
	 *            The message to be logged.
	 * @param exception
	 *            An optionnal exception you want to print (as a stack trace).
	 */
	public abstract void fatal(Object receiver, String message, Throwable exception);

	/**
	 * Get the logger's name as defined in log4j.properties or as provided at
	 * the logger's creation.
	 *
	 * @return The same name as provided when the method createLogger() was
	 *         called.
	 */
	public abstract String getName();
}
