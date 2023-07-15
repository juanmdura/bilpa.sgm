package app.server.util;

import java.io.File;

public class FileAdv extends File implements Comparable<File>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FileAdv(File parent, String child) {
		super(parent, child);
	}

}
