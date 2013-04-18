package org.jenkinsci.plugins.periodicbackup;

import hudson.Extension;

import org.kohsuke.stapler.DataBoundConstructor;

public class ExcludeByPattern extends FullBackup
{
  private final String excludesString;

  @DataBoundConstructor
  public ExcludeByPattern(String excludesString)
  {
    this.excludesString = excludesString;
  }

  public String getDisplayName()
  {
    return "ExcludeByPattern";
  }

  protected String[] getExcludes()
  {
    if (this.excludesString == null) {
      return null;
    }
    return this.excludesString.split(";");
  }
  
  public String getExcludesString() 
  {
	return excludesString;
  }

  @Extension
  public static class DescriptorImpl extends FileManagerDescriptor
  {
    public String getDisplayName()
    {
      return "ExcludeByPattern";
    }
  }
}