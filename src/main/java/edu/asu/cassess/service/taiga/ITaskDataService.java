package edu.asu.cassess.service.taiga;

import edu.asu.cassess.persist.entity.taiga.TaskData;

/**
 * Created by Thomas on 4/11/2017.
 */
public interface ITaskDataService {
    TaskData[] getTasks(Long id, String token, int page);

    void getTaskTotals(String slug, String course);

    /* Method to obtain all task totals for members of a particular course,
            occurring on a schedule
         */
    void updateTaskTotals(String course);
}
