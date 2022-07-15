package org.jenkinsci.plugins.periodicbackup;

import io.jenkins.plugins.casc.misc.ConfiguredWithCode;
import io.jenkins.plugins.casc.misc.JenkinsConfiguredWithCodeRule;
import jenkins.model.Jenkins;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class ConfigurationAsCodeTest {
    @Rule
    public JenkinsConfiguredWithCodeRule r = new JenkinsConfiguredWithCodeRule();

    @Test
    @ConfiguredWithCode("jcasc/configuration-as-code.yml")


    public void shouldSupportConfigurationAsCode() throws Exception {
        PeriodicBackupLink target = Jenkins.get().getExtensionList(PeriodicBackupLink.class).get(0);
        assertEquals("0 8 * * *", target.getCron());
        assertEquals(7, target.getCycleDays());
        assertEquals(7, target.getCycleQuantity());
        assertEquals("/tmp", target.getTempDirectory());
        assertTrue(target.getFileManagerPlugin() instanceof ConfigOnly);
        assertEquals(3, target.getStorages().size());
        assertTrue(target.getStorages().get(0) instanceof TarGzStorage);
        assertTrue(target.getStorages().get(1) instanceof ZipStorage);
        ZipStorage zip = (ZipStorage) target.getStorages().get(1);
        assertTrue(zip.isMultiVolume());
        assertEquals(16777216, zip.getVolumeSize());
        assertEquals(2, target.getLocations().size());
        assertTrue(target.getLocations().get(0) instanceof S3);
        S3 s3 = (S3) target.getLocations().get(0);
        assertEquals("bucket1", s3.getBucket());
        assertTrue(s3.enabled);
        assertEquals("prefix1", s3.getPrefix());
        assertEquals("us-west-2", s3.getRegion());
        assertEquals("/tmp/s3backup", s3.getTmpDir());
        assertTrue(target.getLocations().get(1) instanceof LocalDirectory);
        LocalDirectory localDirectory= (LocalDirectory) target.getLocations().get(1);
        assertEquals("/var/jenkins/backup", localDirectory.getPath().getPath());
        assertTrue(localDirectory.enabled);


    }
}
