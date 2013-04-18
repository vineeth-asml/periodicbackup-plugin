package org.jenkinsci.plugins.periodicbackup;

import java.util.Collections;
import java.util.Iterator;

import hudson.Extension;

import org.kohsuke.stapler.DataBoundConstructor;

import com.google.common.base.Splitter;

public class ExcludeByPattern extends FullBackup {
	private final String excludesString;

	@DataBoundConstructor
	public ExcludeByPattern(String excludesString) {
		this.excludesString = excludesString;
	}

	public String getDisplayName() {
		return "ExcludeByPattern";
	}

	protected Iterator<String> getExcludes() {
		if (this.excludesString == null) {
			return Collections.emptyIterator();
		}
		return Splitter.on(';').trimResults().split(excludesString).iterator();
	}

	public String getExcludesString() {
		return excludesString;
	}

	@Extension
	public static class DescriptorImpl extends FileManagerDescriptor {
		public String getDisplayName() {
			return "ExcludeByPattern";
		}
	}
}