package org.tinywind.server.util.mybatis;

/**
 * @author tinywind
 * @since 2017-05-14
 */
public interface CodeHasable {
	String getCode();

	default boolean ignoreCase() {
		return true;
	}
}
