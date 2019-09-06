package com.github.armedis.utils;
/**
 * 박스형태의 로그를 출력하기 위한 유틸리티 클래스. 
 * @author kris
 */
public class LogFormatter {
	/**
	 * 화면에 출력할 로그의 길이.
	 */
	private static final int DISPLAY_LEN = 80;

	/**
	 * 로그를 구분하기 위한 문자열
	 */
	private static final char DEFAULT_CHAR = '=';

	/**
	 * 줄바꿈 문자열
	 */
	private static final String NEWLINE = "\n";

	/**
	 * 공백문자
	 */
	private static final char SPACE = ' ';

	/**
	 * 박스형 로그의 첫줄에 출력할 문자열을 생성한다.
	 * @param text 출력할 문자열
	 * @return Log용 문자열
	 */
	public static final String makeHeader(String text) {
		return makeHeader(text, DISPLAY_LEN);
	}

	/**
	 * Log에 출력할 Header 문자열을 생성한다. 
	 * @param text 출력할 문자열
	 * @param lineLen 생성될 문자열의 전체길이
	 * @return Log용 문자열
	 */
	public static final String makeHeader(String text, int lineLen) {
		StringBuilder builder = new StringBuilder(lineLen);
		appendChar(builder, (lineLen - text.length() - 2) / 2);
		builder.append(' ');
		builder.append(text);
		builder.append(' ');
		appendChar(builder, (lineLen - text.length() - 2) / 2);

		return builder.toString();
	}

	/**
	 * 로그 사이를 구분하기 위한 줄바꿈 문자열
	 * @return {@link #NEWLINE} 문자열
	 */
	public static final String makeReadableLine(int numberOfLine) {
		StringBuilder builder = new StringBuilder(256);
		for (int i = 0; i < numberOfLine; i++) {
			builder.append(LogFormatter.NEWLINE);
		}
		return builder.toString();
	}

	/**
	 * 포메팅된 로그 문자열 생성
	 * @param text
	 * @return
	 */
	public static final String makeBody(String text) {
		return makeBody(text, LogFormatter.DISPLAY_LEN);
	}

	public static final String makeBody(String text, int lineLen) {
		return makeBody(text, lineLen, LogFormatter.SPACE);
	}

	public static final String makeBody(String text, int lineLen, char filler) {
		StringBuilder builder = new StringBuilder(lineLen);
		builder.append(filler);
		appendChar(builder, (lineLen - text.length() - 2) / 2, filler);
		builder.append(text);
		appendChar(builder, (lineLen - text.length() - 2) / 2, filler);
		builder.append(filler);

		return builder.toString();
	}

	/**
	 * 포메팅된 로그 문자열 생성
	 * @param text
	 * @return
	 */
	public static final String makeBodyLeftAlign(String text) {
		return makeBodyLeftAlign(text, LogFormatter.DISPLAY_LEN);
	}

	public static final String makeBodyLeftAlign(String text, int lineLen) {
		return makeBodyLeftAlign(text, lineLen, LogFormatter.SPACE);
	}

	public static final String makeBodyLeftAlign(String text, int lineLen, char filler) {
		StringBuilder builder = new StringBuilder(lineLen);
		builder.append(filler);
		builder.append(LogFormatter.SPACE);
		builder.append(LogFormatter.SPACE);
		builder.append(text);
		appendChar(builder, lineLen - text.length() - 2, filler);
		builder.append(filler);

		return builder.toString();
	}

	public static final String makeFooter() {
		return makeFooter(LogFormatter.DISPLAY_LEN);
	}

	public static final String makeFooter(int size) {
		StringBuilder builder = new StringBuilder(size);
		appendChar(builder, size, LogFormatter.DEFAULT_CHAR);
		return builder.toString();
	}

	private static final void appendChar(StringBuilder builder, int size) {
		appendChar(builder, size, LogFormatter.DEFAULT_CHAR);
	}

	private static final void appendChar(StringBuilder builder, int size, char filler) {
		for (int i = 0; i < size; i++) {
			builder.append(filler);
		}
	}
}