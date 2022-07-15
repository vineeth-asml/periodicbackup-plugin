package org.jenkinsci.plugins.periodicbackup;

import com.google.common.collect.ImmutableSet;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import io.jenkins.plugins.casc.BaseConfigurator;
import io.jenkins.plugins.casc.ConfigurationContext;
import io.jenkins.plugins.casc.ConfiguratorException;
import io.jenkins.plugins.casc.RootElementConfigurator;
import io.jenkins.plugins.casc.model.Mapping;
import org.jenkinsci.Symbol;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;

import java.util.Set;

@Extension(optional = true, ordinal = -50)
@Restricted(NoExternalUse.class)
@Symbol("periodicBackup")
public class PeriodicBackupConfigurator extends BaseConfigurator<PeriodicBackupLink> implements RootElementConfigurator<PeriodicBackupLink> {


    @Override
    public PeriodicBackupLink getTargetComponent(ConfigurationContext context) {
        return PeriodicBackupLink.get();
    }

    @NonNull
    @Override
    public String getName() {
        return "periodicBackup";
    }

    @Override
    public Class<PeriodicBackupLink> getTarget() {
        return PeriodicBackupLink.class;
    }


    protected Set<String> exclusions() {
        return ImmutableSet.of("message", "backupNow");
    }

    @Override
    protected PeriodicBackupLink instance(Mapping mapping, ConfigurationContext context) throws ConfiguratorException {
        return PeriodicBackupLink.get();
    }

}
