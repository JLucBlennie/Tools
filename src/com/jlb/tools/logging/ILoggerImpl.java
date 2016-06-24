package com.jlb.tools.logging;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

/**
 * Implementation de ILogger
 * 
 * @author jluc
 *
 */
public class ILoggerImpl implements ILogger, Log4jProperties {

	/** The underlying log4j logger. */
	private Logger myLogger;

	/** An internal buffer used for appending strings. */
	private StringBuilder myBuffer;

	private InetAddress myAddress;

	/**
	 * Constructs a new ISigmaLoggerImpl from a Log4J logger.
	 * 
	 * @param logger
	 *            The underlying log4j logger.
	 */
	public ILoggerImpl(Logger logger) {
		myLogger = logger;
		myBuffer = new StringBuilder("Receiver: ");

		// Il faut initialiser ces variables dans le thread courant.
		try {
			myAddress = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDebugEnabled() {
		return myLogger.isDebugEnabled();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return myLogger.getName();
	}

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
	@Override
	public synchronized void debug(Object receiver, String message) {
		MDC.put(MDC_KEY_RECEIVER, getReceiverString(receiver));
		MDC.put(MDC_KEY_METHOD, getUserMethodString());
		MDC.put(MDC_KEY_HOSTNAME, myAddress.getHostName());
		MDC.put(MDC_KEY_HOSTADDRESS, myAddress.getHostAddress());
		myLogger.debug(message);
		MDC.remove(MDC_KEY_HOSTNAME);
		MDC.remove(MDC_KEY_HOSTADDRESS);
		MDC.remove(MDC_KEY_RECEIVER);
		MDC.remove(MDC_KEY_METHOD);
	}

	/**
	 * Prints a message in all appenders of the underlying log4j logger. This
	 * message will appear only if the logger's level is at least INFO.
	 * 
	 * @param receiver
	 *            The object that is related to the logged message.
	 * @param message
	 *            The message to be logged.
	 */
	@Override
	public synchronized void info(Object receiver, String message) {
		MDC.put(LoggerFactory.MDC_KEY_RECEIVER, getReceiverString(receiver));
		MDC.put(LoggerFactory.MDC_KEY_METHOD, getUserMethodString());
		MDC.put(MDC_KEY_HOSTNAME, myAddress.getHostName());
		MDC.put(MDC_KEY_HOSTADDRESS, myAddress.getHostAddress());
		myLogger.info(message);
		MDC.remove(MDC_KEY_HOSTNAME);
		MDC.remove(MDC_KEY_HOSTADDRESS);
		MDC.remove(LoggerFactory.MDC_KEY_RECEIVER);
		MDC.remove(LoggerFactory.MDC_KEY_METHOD);
	}

	/**
	 * Prints a message in all appenders of the underlying log4j logger. This
	 * message will appear only if the logger's level is at least WARN.
	 * 
	 * @param receiver
	 *            The object that is related to the logged message.
	 * @param message
	 *            The message to be logged.
	 */
	@Override
	public synchronized void warning(Object receiver, String message) {
		MDC.put(MDC_KEY_RECEIVER, getReceiverString(receiver));
		MDC.put(MDC_KEY_METHOD, getUserMethodString());
		MDC.put(MDC_KEY_HOSTNAME, myAddress.getHostName());
		MDC.put(MDC_KEY_HOSTADDRESS, myAddress.getHostAddress());
		myLogger.warn(message);
		MDC.remove(MDC_KEY_HOSTNAME);
		MDC.remove(MDC_KEY_HOSTADDRESS);
		MDC.remove(MDC_KEY_RECEIVER);
		MDC.remove(MDC_KEY_METHOD);
	}

	/**
	 * Prints a message in all appenders of the underlying log4j logger. This
	 * message will appear only if the logger's level is at least ERROR.
	 * 
	 * @param receiver
	 *            The object that is related to the logged message.
	 * @param message
	 *            The message to be logged.
	 * @param exception
	 *            An optionnal exception you want to print (as a stack trace).
	 */
	@Override
	public synchronized void error(Object receiver, String message, Throwable exception) {
		MDC.put(MDC_KEY_RECEIVER, getReceiverString(receiver));
		MDC.put(MDC_KEY_METHOD, getUserMethodString());
		MDC.put(MDC_KEY_HOSTNAME, myAddress.getHostName());
		MDC.put(MDC_KEY_HOSTADDRESS, myAddress.getHostAddress());
		if (exception != null) {
			myLogger.error(message, exception);
		} else {
			myLogger.error(message);
		}
		MDC.remove(MDC_KEY_RECEIVER);
		MDC.remove(MDC_KEY_METHOD);
		MDC.remove(MDC_KEY_HOSTNAME);
		MDC.remove(MDC_KEY_HOSTADDRESS);
	}

	/**
	 * Prints a message in all appenders of the underlying log4j logger. This
	 * message will appear only if the logger's level is at least FATAL.
	 * 
	 * @param receiver
	 *            The object that is related to the logged message.
	 * @param message
	 *            The message to be logged.
	 * @param exception
	 *            An optionnal exception you want to print (as a stack trace).
	 */
	@Override
	public synchronized void fatal(Object receiver, String message, Throwable exception) {
		MDC.put(MDC_KEY_RECEIVER, getReceiverString(receiver));
		MDC.put(MDC_KEY_METHOD, getUserMethodString());
		MDC.put(MDC_KEY_HOSTNAME, myAddress.getHostName());
		MDC.put(MDC_KEY_HOSTADDRESS, myAddress.getHostAddress());
		if (exception != null) {
			myLogger.fatal(message, exception);
		} else {
			myLogger.fatal(message);
		}
		MDC.remove(MDC_KEY_HOSTNAME);
		MDC.remove(MDC_KEY_HOSTADDRESS);
		MDC.remove(MDC_KEY_RECEIVER);
		MDC.remove(MDC_KEY_METHOD);
	}

	/**
	 * Returns a string concatenation like "Receiver=xxxxxxxxxx" where
	 * "xxxxxxxxxx" is your object's memory address.
	 * 
	 * @param receiver
	 *            The object you want to print.
	 * @return A string like "Receiver=xxxxxxxxxx".
	 */
	private String getReceiverString(Object receiver) {
		StringBuilder sb = myBuffer;
		sb.delete(0, myBuffer.length());

		return sb.append(System.identityHashCode(receiver)).toString();
	}

	/**
	 * Returns the name of the first user's method encountered in the stack
	 * trace and the line number.
	 * 
	 * @return A string like "foo():25".
	 */
	private String getUserMethodString() {
		StringBuilder sb = myBuffer;
		sb.delete(0, myBuffer.length());

		StackTraceElement[] stack = new Throwable().getStackTrace();
		for (int i = 2, stop = stack.length; i < stop; i++) {
			StackTraceElement element = stack[i];
			if (element.getClass().equals(getClass())) {
				continue;
			}
			return sb.append(element.getClassName()).append("#").append(element.getMethodName()).append(":")
					.append(element.getLineNumber()).toString();
		}
		return "<unknown method>";
	}
}
