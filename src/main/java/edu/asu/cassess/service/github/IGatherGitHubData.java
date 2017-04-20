package edu.asu.cassess.service.github;

import edu.asu.cassess.persist.entity.github.CommitData;

import java.util.List;

/**
 * Created by Thomas on 4/18/2017.
 */
public interface IGatherGitHubData {
    void fetchData(String owner, String projectName);

    List<CommitData> getCommitList();
}
