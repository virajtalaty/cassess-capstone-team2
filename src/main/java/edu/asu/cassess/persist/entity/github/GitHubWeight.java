package edu.asu.cassess.persist.entity.github;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name="github_weight")
public class GitHubWeight {
    @Id
    private String Email;

    @Column(name="date")
    private Date date;

    @Column(name="weight")
    private int weight;

    public GitHubWeight(){

    }

    public GitHubWeight(String email, Date date, int weight) {
        Email = email;
        this.date = date;
        this.weight = weight;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "GitHubWeight{" +
                "Email='" + Email + '\'' +
                ", date=" + date +
                ", weight=" + weight +
                '}';
    }
}
