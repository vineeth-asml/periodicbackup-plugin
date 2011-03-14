package org.jvnet.hudson.plugins.periodicbackup;

import com.google.common.collect.Lists;
import hudson.Extension;
import hudson.model.Hudson;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.tar.TarArchiver;
import org.codehaus.plexus.archiver.tar.TarGZipUnArchiver;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class TarGzStorage extends Storage {

    private transient TarArchiver archiver;

    private static final Logger LOGGER = Logger.getLogger(TarGzStorage.class.getName());

    @DataBoundConstructor
    public TarGzStorage() {
        super();
    }

    @Override
    public void backupStart(String tempDirectoryPath, String archiveFilenameBase) throws PeriodicBackupException {
        // Create a new archiver
        archiver = new TarArchiver();
        // Set the destination file
        File destination = new File(new File(tempDirectoryPath), Util.createFileName(archiveFilenameBase, this.getDescriptor().getArchiveFileExtension()));
        archiver.setDestFile(destination);

        // Set the compression method
        TarArchiver.TarCompressionMethod compression = new TarArchiver.TarCompressionMethod();
        try {
            compression.setValue("gzip");
        } catch (org.codehaus.plexus.archiver.ArchiverException e) {
            LOGGER.warning("Cannot set compression value " + e.getMessage());
        }

        archiver.setCompression(compression);
    }

    @Override
    public void backupAddFile(File fileToStore) throws PeriodicBackupException {
        try {
            archiver.addFile(fileToStore, Util.getRelativePath(fileToStore, Hudson.getInstance().getRootDir()));
        } catch (ArchiverException e) {
            LOGGER.warning("Could not add file to the archive. " + e.getMessage());
        }
    }

    @Override
    public Iterable<File> backupStop() throws PeriodicBackupException {
        try {
            archiver.createArchive();
        } catch (ArchiverException e) {
            LOGGER.warning("Could not create archive " + archiver.getDestFile() + " " + e.getMessage());
        } catch (IOException e) {
            LOGGER.warning("Could not create archive " + archiver.getDestFile() + " " + e.getMessage());
        }
        return Lists.newArrayList(archiver.getDestFile());
    }

    @Override
    public void unarchiveFiles(Iterable<File> archives, File tempDir) {
        // Setting up unArchiver
        TarGZipUnArchiver unArchiver = new TarGZipUnArchiver();
        unArchiver.setDestDirectory(tempDir);
        unArchiver.enableLogging(new ConsoleLogger(org.codehaus.plexus.logging.Logger.LEVEL_INFO, "UnArchiver"));

        // Extracting each archive to the temporary directory
        for(File archive : archives) {
            unArchiver.setSourceFile(archive);
            LOGGER.info("Extracting files from " + archive.getAbsolutePath() + " to " + tempDir.getAbsolutePath());
            try {
                unArchiver.extract();
            } catch (ArchiverException e) {
                LOGGER.warning("Could not extract from " + archive.getAbsolutePath() + e.getMessage());
            }

            // Deleting the archive file
            LOGGER.info("Deleting " + archive.getAbsolutePath());
            if(!archive.delete()) {
                LOGGER.warning("Could not delete " + archive.getAbsolutePath());
            }
        }
    }

    public String getDisplayName() {
        return "TarGz";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof TarGzStorage;
    }

    @Override
    public int hashCode() {
        return 89;
    }

    @SuppressWarnings("unused")
    @Extension
    public static class DescriptorImpl extends StorageDescriptor {
        public String getDisplayName() {
            return "TarGzStorage";
        }

        @Override
        public String getArchiveFileExtension() {
            return "tar.gz";
        }

    }
}