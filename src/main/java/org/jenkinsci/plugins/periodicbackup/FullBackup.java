/*
 * The MIT License
 *
 * Copyright (c) 2010 - 2011, Tomasz Blaszczynski, Emanuele Zattin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.jenkinsci.plugins.periodicbackup;

import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import hudson.Extension;
import org.apache.tools.ant.DirectoryScanner;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;
import java.util.Collections;
import java.util.List;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import jenkins.model.Jenkins;
import org.apache.commons.lang.StringUtils;


/**
 *
 * FullBackup will choose all the files in the Jenkins homedir during the backup.
 * During the restore it will delete all the deletable files in the Jenkins homedir
 * and then it will write with files in the selected backup.
 */
public class FullBackup extends FileManager {
	
    @CheckForNull
    private final String excludesString;
	private final File baseDir;
	private final boolean followSymbolicLinks;

    public FullBackup() {
        this(null, false);
    }

    @DataBoundConstructor
	public FullBackup(@CheckForNull String excludesString, boolean followSymbolicLinks) {
		this(excludesString, followSymbolicLinks, Jenkins.getActiveInstance().getRootDir());
	}

    /**
     * Test Constructor
     * 
     * @param excludesString Optional list of directories to be excluded
     * @param baseDir Base directory
     */
    FullBackup(@CheckForNull String excludesString, boolean followSymbolicLinks, @Nonnull File baseDir) {
    	super();
    	this.excludesString = StringUtils.trimToNull(excludesString);
        this.followSymbolicLinks = followSymbolicLinks;
		this.baseDir = baseDir;
    	this.restorePolicy = new ReplaceRestorePolicy();
    }
    
    
    public String getDisplayName() {
        return "FullBackup";
    }

    @Override
    public Iterable<File> getFilesToBackup() {
        DirectoryScanner directoryScanner = new DirectoryScanner(); // It will scan all files inside the root directory
        directoryScanner.setFollowSymlinks(followSymbolicLinks);
        directoryScanner.setBasedir(baseDir);
        directoryScanner.setExcludes(Iterables.toArray(getExcludes(), String.class));
        directoryScanner.scan();
        List<File> files = Lists.newArrayList();
        for (String s : directoryScanner.getIncludedFiles()) {
          files.add(new File(directoryScanner.getBasedir(), s));
        }
        return files;
      }
    
    private Iterable<String> getExcludes() {
		if (this.excludesString == null) {
			return Collections.emptyList();
		}
		return Lists.newArrayList(Splitter.on(';').trimResults().split(excludesString).iterator());
	}

    @Override
    public boolean equals(Object o) {
        if (o instanceof FullBackup) {
            FullBackup that = (FullBackup) o;
            return Objects.equal(this.restorePolicy, that.restorePolicy);
        }
        return false;

    }

    @Override
    public int hashCode() {
        return 73;
    }	
    
    @CheckForNull
    public String getExcludesString() {
        return excludesString;
    }

    public boolean isFollowSymbolicLinks() {
        return followSymbolicLinks;
    }

    @SuppressWarnings("unused")
    @Extension
    public static class DescriptorImpl extends FileManagerDescriptor {
        public String getDisplayName() {
            return "FullBackup";
        }
    }
}
