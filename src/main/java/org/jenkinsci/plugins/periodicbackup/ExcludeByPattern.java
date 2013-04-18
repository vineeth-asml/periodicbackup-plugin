package org.jenkinsci.plugins.periodicbackup;

import java.util.List;

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

	protected String[] getExcludes() {
		if (this.excludesString == null) {
			return null;
		}
		List<String> list = Lists.newArrayList(Splitter.on(';').trimResults().split(excludesString).iterator());
		return list.toArray(new String[list.size()]);
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