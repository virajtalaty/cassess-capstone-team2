package edu.asu.cassess.service.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Date;

/**
 * Gathers needed commit data from GitHub API path:
 * https://api.github.com/repos/:owner/:repositoryName/commits/:sha
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubCommitData {
    private GitHubStats stats;
    private GitHubCommit commit;

    public GitHubCommitData() {

    }

    public GitHubStats getStats() {
        return stats;
    }

    public void setStats(GitHubStats stats) {
        this.stats = stats;
    }

    public GitHubCommit getCommit() {
        return commit;
    }

    public void setCommit(GitHubCommit commit) {
        this.commit = commit;
    }

    @Override
    public String toString() {
        return "GitHubCommitData{" +
                ", stats=" + stats +
                ", commit=" + commit +
                '}';
    }


    /**
     * Gathers commit property from GitHub API path:
     * https://api.github.com/repos/:owner/:repositoryName/commits/:sha
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    class GitHubCommit {
        private GitHubAuthor author;
        private String message;

        public GitHubCommit() {
        }

        public GitHubAuthor getAuthor() {
            return author;
        }

        public void setAuthor(GitHubAuthor author) {
            this.author = author;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "GitHubCommit{" +
                    "author=" + author +
                    ", message='" + message + '\'' +
                    '}';
        }

        /**
         * Gathers commit:author from GitHub API path:
         * https://api.github.com/repos/:owner/:repositoryName/commits/:sha
         */
        @JsonIgnoreProperties(ignoreUnknown = true)
        class GitHubAuthor{
            private String name;
            private String email;
            private Date date;

            public GitHubAuthor() {
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public Date getDate() {
                return date;
            }

            public void setDate(Date date) {
                this.date = date;
            }

            @Override
            public String toString() {
                return "GitHubAuthor{" +
                        "name='" + name + '\'' +
                        ", email='" + email + '\'' +
                        ", date=" + date +
                        '}';
            }
        }
    }

    /**
     * Gathers stats property from GitHub API path:
     * https://api.github.com/repos/:owner/:repositoryName/commits/:sha
     */
    class GitHubStats {
        private int additions;
        private int deletions;

        public GitHubStats() {
        }

        public int getAdditions() {
            return additions;
        }

        public void setAdditions(int additions) {
            this.additions = additions;
        }

        public int getDeletions() {
            return deletions;
        }

        public void setDeletions(int deletions) {
            this.deletions = deletions;
        }

        @Override
        public String toString() {
            return "GitHubStats{" +
                    "additions=" + additions +
                    ", deletions=" + deletions +
                    '}';
        }
    }
}