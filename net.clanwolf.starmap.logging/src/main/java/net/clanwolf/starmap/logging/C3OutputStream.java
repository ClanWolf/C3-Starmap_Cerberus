package net.clanwolf.starmap.logging;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class C3OutputStream extends OutputStream {
	Logger logger;
	Level level;
	StringBuilder stringBuilder;

	PrintStream stdout = System.out;
	PrintStream stderr = System.err;

	public C3OutputStream(Logger logger, Level level) {
		this.logger = logger;
		this.level = level;
		stringBuilder = new StringBuilder();
	}

	@Override
	public final void write(int i) throws IOException {
		char c = (char) i;
		if (c == '\r' || c == '\n') {
			if (stringBuilder.length() > 0) {
				logger.log(level, "stdOut/stdErr (redirected): " + stringBuilder.toString());
				stringBuilder = new StringBuilder();
			}
		} else {
			stringBuilder.append(c);
		}
	}
}
