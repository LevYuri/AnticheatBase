package me.levyuri.anticheatbase.files.commentedfiles;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommentedFileConfigurationHelper {

    private final JavaPlugin plugin;

    public CommentedFileConfigurationHelper(final JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public CommentedFileConfiguration getNewConfig(final File file) {
        if (!this.plugin.getDataFolder().exists()) this.plugin.getDataFolder().mkdir();

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }

        return new CommentedFileConfiguration(this.getConfigContent(file), file, this.getCommentsNum(file), this.plugin);
    }

    public Reader getConfigContent(final File file) {
        if (!file.exists()) return new InputStreamReader(new ByteArrayInputStream(new byte[0]));

        try {
            int commentNum = 0;

            final String pluginName = this.getPluginName();

            final StringBuilder whole = new StringBuilder();
            final BufferedReader reader = Files.newBufferedReader(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8);

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                // Convert comments into keys
                if (currentLine.trim().startsWith("#")) {
                    final String addLine = currentLine.replaceAll(Pattern.quote("'"), Matcher.quoteReplacement("''")).replaceFirst("#", pluginName + "_COMMENT_" + commentNum++ + ": '") + "'";
                    whole.append(addLine).append("\n");
                } else {
                    whole.append(currentLine).append("\n");
                }
            }

            final String config = whole.toString();
            final Reader configStream = new StringReader(config);

            reader.close();
            return configStream;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private int getCommentsNum(final File file) {
        if (!file.exists())
            return 0;

        try {
            int comments = 0;
            String currentLine;

            final BufferedReader reader = Files.newBufferedReader(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8);

            while ((currentLine = reader.readLine()) != null) if (currentLine.trim().startsWith("#")) comments++;

            reader.close();
            return comments;
        } catch (final IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private String prepareConfigString(final String configString) {
        boolean lastLine = false;

        final String[] lines = configString.split("\n");
        final StringBuilder config = new StringBuilder();

        for (final String line : lines) {
            if (line.trim().startsWith(this.getPluginName() + "_COMMENT")) {
                final int whitespaceIndex = line.indexOf(line.trim());
                final String comment = line.substring(0, whitespaceIndex) + "#" + line.substring(line.indexOf(":") + 3, line.length() - 1);

                String normalComment;
                if (comment.trim().startsWith("#'")) {
                    normalComment = comment.substring(0, comment.length() - 1).replaceFirst("#'", "# ");
                } else {
                    normalComment = comment;
                }

                normalComment = normalComment.replaceAll("''", "'");

                if (!lastLine) {
                    config.append(normalComment).append("\n");
                } else {
                    config.append("\n").append(normalComment).append("\n");
                }

                lastLine = false;
            } else {
                config.append(line).append("\n");
                lastLine = true;
            }
        }

        return config.toString();
    }

    public void saveConfig(final String configString, final File file, final boolean compactLines) {
        final String configuration = this.prepareConfigString(configString).replaceAll("\n\n", "\n");

        // Apply post-processing to config string to make it pretty
        final StringBuilder stringBuilder = new StringBuilder();
        try (Scanner scanner = new Scanner(configuration)) {
            boolean lastLineHadContent = false;
            int lastCommentSpacing = -1;
            int lastLineSpacing = -1;
            boolean forceCompact = false;

            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine();

                boolean lineHadContent = false;
                boolean lineWasComment = false;
                int commentSpacing = -1;
                final int lineSpacing = line.indexOf(line.trim());

                if (line.trim().startsWith("#")) {
                    lineWasComment = true;
                    final String trimmed = line.trim().replaceFirst("#", "");
                    commentSpacing = trimmed.indexOf(trimmed.trim());
                } else if (!line.trim().isEmpty()) {
                    lineHadContent = true;

                    if (line.trim().startsWith("-"))
                        forceCompact = true;
                }

                if (!compactLines && !forceCompact && (
                        (lastLineSpacing != -1 && lineSpacing != lastLineSpacing)
                                || (commentSpacing != -1 && commentSpacing < lastCommentSpacing)
                                || (lastLineHadContent && lineHadContent)
                                || (lineWasComment && lastLineHadContent))
                        && !(lastLineHadContent && !lineWasComment)) {
                    stringBuilder.append('\n');
                }

                stringBuilder.append(line).append('\n');

                lastLineHadContent = lineHadContent;
                lastCommentSpacing = commentSpacing;
                lastLineSpacing = lineSpacing;
                forceCompact = false;
            }
        }

        try (final BufferedWriter writer = Files.newBufferedWriter(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8)) {
            writer.write(stringBuilder.toString());
            writer.flush();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public String getPluginName() {
        return this.plugin.getDescription().getName();
    }

}
