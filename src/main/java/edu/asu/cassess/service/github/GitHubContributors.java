package edu.asu.cassess.service.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;


@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubContributors {
    private GitHubAuthor author;
    private ArrayList<Weeks> weeks;


    public GitHubContributors() {
        weeks = new ArrayList<Weeks>();
    }

    public ArrayList<Weeks> getWeeks() {
        return weeks;
    }

    @JsonSetter()
    public void setWeeks(JsonNode node) {
        for (JsonNode current : node) {
            int w = Integer.parseInt(current.findValue("w").toString());
            int a = Integer.parseInt(current.findValue("a").toString());
            int d = Integer.parseInt(current.findValue("d").toString());
            int c = Integer.parseInt(current.findValue("c").toString());

            Weeks week = new Weeks(w, a, d, c);
            weeks.add(week);
        }
    }

    public GitHubAuthor getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return "GitHubContributors{" +
                "weeks=" + weeks +
                ", author=" + author +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class Weeks {
        private int w;
        private int a;
        private int d;
        private int c;

        public Weeks() {
        }

        public Weeks(int w, int a, int d, int c) {
            this.w = w;
            this.a = a;
            this.d = d;
            this.c = c;
        }

        public int getW() {
            return w;
        }

        public int getA() {
            return a;
        }

        public int getD() {
            return d;
        }

        public int getC() {
            return c;
        }

        @Override
        public String toString() {
            return "Weeks{" +
                    "w=" + w +
                    ", a=" + a +
                    ", d=" + d +
                    ", c=" + c +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class GitHubAuthor {
        private String login;

        public GitHubAuthor() {

        }

        public String getLogin() {
            return login;
        }

        @Override
        public String toString() {
            return "GitHubAuthor{" +
                    "login='" + login + '\'' +
                    '}';
        }
    }

}
