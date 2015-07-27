package com.holmsted.gerrit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

public class CommandLineParser {

    private String filename;
    private String serverName;
    private int serverPort;
    private String projectName;
    private String outputFile;
    private int limit = GerritStatReader.NO_COMMIT_LIMIT;
    @Nonnull
    private final List<String> excludedEmails = new ArrayList<String>();
    @Nonnull
    private final List<String> includedBranches = new ArrayList<String>();
    @Nonnull
    private final List<String> includedEmails = new ArrayList<String>();
    @Nonnull
    private OutputType outputType = OutputType.PLAIN;

    private int listCommitsExceedingPatchSetCount = OutputRules.INVALID_PATCH_COUNT;
    private boolean listReviewComments;
    private Output output = Output.PER_PERSON_DATA;

    public boolean parse(String[] args) {
        boolean hasSyntaxError = false;
        for (int i = 0; i < args.length; ++i) {
            String arg = args[i];
            boolean isNotAtEnd = (i < args.length - 1);
            if (arg.equals("--file") && isNotAtEnd) {
                filename = args[i + 1];
                ++i;
            } else if (arg.equals("--server") && isNotAtEnd) {
                serverName = args[i + 1];
                int portSeparator = serverName.indexOf(':');
                if (portSeparator != -1) {
                    serverPort = Integer.valueOf(serverName.substring(portSeparator + 1));
                    serverName = serverName.substring(0, portSeparator);
                }
                ++i;
            } else if (arg.equals("--project") && isNotAtEnd) {
                projectName = args[i + 1];
                ++i;
            } else if (arg.equals("--output-file") && isNotAtEnd) {
                outputFile = args[i + 1];
                ++i;
            } else if (arg.equals("--limit") && isNotAtEnd) {
                try {
                    limit = Integer.parseInt(args[i + 1]);
                    ++i;
                } catch (NumberFormatException nfe) {
                    hasSyntaxError = true;
                }
            } else if (arg.equals("--exclude") && isNotAtEnd) {
                String[] emails = args[i + 1].split(",");
                Collections.addAll(excludedEmails, emails);
                ++i;
            } else if (arg.equals("--include") && isNotAtEnd) {
                String[] emails = args[i + 1].split(",");
                Collections.addAll(includedEmails, emails);
                ++i;
            } else if (arg.equals("--branches") && isNotAtEnd) {
                String[] branches = args[i + 1].split(",");
                Collections.addAll(includedBranches, branches);
                ++i;
            } else if (arg.equals("--output-type") && isNotAtEnd) {
                outputType = OutputType.fromTypeString(args[i + 1]);
                ++i;
            } else if (arg.equals("--output") && isNotAtEnd) {
                output = Output.fromString(args[i + 1]);
                ++i;
            } else if (arg.equals("--list-comments")) {
                listReviewComments = true;
            } else if (arg.equals("--list-commits-exceeding-patch-set-count") && isNotAtEnd) {
                listCommitsExceedingPatchSetCount = Integer.parseInt(args[i + 1]);
                ++i;
            }

        }

        return (filename != null || serverName != null) && !hasSyntaxError;
    }

    public String getFilename() {
        return filename;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public String getServerName() {
        return serverName;
    }

    public String getProjectName() {
        return projectName;
    }

    public int getServerPort() {
        return serverPort;
    }

    public int getCommitLimit() {
        return limit;
    }

    @Nonnull
    public List<String> getExcludedEmails() {
        return excludedEmails;
    }

    @Nonnull
    public List<String> getIncludedEmails() {
        return includedEmails;
    }

    @Nonnull
    public List<String> getIncludeBranches() {
        return includedBranches;
    }

    @Nonnull
    public OutputType getOutputType() {
        return outputType;
    }

    public Output getOutput() {
        return output;
    }

    public boolean getListReviewComments() {
        return listReviewComments;
    }

    public int getListCommitsExceedingPatchSetCount() {
        return listCommitsExceedingPatchSetCount;
    }
}