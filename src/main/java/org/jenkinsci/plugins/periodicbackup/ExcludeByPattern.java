package org.jenkinsci.plugins.periodicbackup;

import java.util.Collections;

import hudson.Extension;

import org.kohsuke.stapler.DataBoundConstructor;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public class ExcludeByPattern extends FullBackup {
	private final String excludesString;

	@DataBoundConstructor
	public ExcludeByPattern(String excludesString) {
		this.excludesString = excludesString;
	}

	public String getDisplayName() {
		return "ExcludeByPattern";
	}

	protected Iterable<String> getExcludes() {
		if (this.excludesString == null) {
			return Collections.emptyList();
		}
		return Lists.newArrayList(Splitter.on(';').trimResults().split(excludesString).iterator());
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