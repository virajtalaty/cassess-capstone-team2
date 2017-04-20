package edu.asu.cassess.dao.github;

import edu.asu.cassess.model.Taiga.WeeklyFreqWeight;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Component
public class GitHubQueryDao implements IGitHubQueryDao {

    protected EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public List<WeeklyFreqWeight> getWeightFreqByCourse(String course) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT (@rn \\:= @rn + 1) as week, DATE_ADD(date, INTERVAL 1 DAY) as weekBeginning, DATE_ADD(date, INTERVAL 7 DAY) as weekEnding,\n" +
                "CASE \n" +
                "\t WHEN LOCA < LOCD THEN 1\n" +
                "     WHEN TOTC >= 450 THEN 3\n" +
                "     WHEN TOTC >= 200 THEN 2\n" +
                "     WHEN TOTC >= 25  THEN 1\n" +
                "     WHEN TOTC <  25  THEN 0\n" +
                "     END AS weight,\n" +
                "     \n" +
                "     CASE \n" +
                "     WHEN COMT >= 4 THEN 3\n" +
                "     WHEN COMT >= 2.5 THEN 2\n" +
                "     WHEN COMT >= 1  THEN 1\n" +
                "     WHEN COMT <  1  THEN 0\n" +
                "     END AS frequency\n" +
                "FROM\n" +
                "(SELECT course, date,\n" +
                "AVG(total_code) as TOTC, AVG(commits) as COMT, AVG(LOCA) as LOCA, AVG(LOCD) as LOCD\n" +
                "FROM\n" +
                "(SELECT students.email as email, students.course as course, students.team_name as team,\n" +
                "commit_data.lines_of_code_added + (commit_data.lines_of_code_deleted/4) as total_code, commit_data.lines_of_code_deleted as LOCD,\n" +
                "commit_data.lines_of_code_added as LOCA,\n" +
                "commit_data.commits as commits, commit_data.date as date\n" +
                "FROM \n" +
                "\t(students\n" +
                "\tinner join\n" +
                "\tcommit_data)\n" +
                "\tWHERE students.email = commit_data.email)first,\n" +
                "    (select @rn \\:= 0) var\n" +
                "    WHERE course = ?1\n" +
                "    GROUP BY date)second\n" +
                "    ORDER BY week DESC LIMIT 2", WeeklyFreqWeight.class);
        query.setParameter(1, course);
        List<WeeklyFreqWeight> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyFreqWeight> getWeightFreqByTeam(String course, String team) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT (@rn \\:= @rn + 1) as week, DATE_ADD(date, INTERVAL 1 DAY) as weekBeginning, DATE_ADD(date, INTERVAL 7 DAY) as weekEnding,\n" +
                "CASE \n" +
                "\t WHEN LOCA < LOCD THEN 1\n" +
                "     WHEN TOTC >= 450 THEN 3\n" +
                "     WHEN TOTC >= 200 THEN 2\n" +
                "     WHEN TOTC >= 25  THEN 1\n" +
                "     WHEN TOTC <  25  THEN 0\n" +
                "     END AS weight,\n" +
                "     \n" +
                "     CASE \n" +
                "     WHEN COMT >= 4 THEN 3\n" +
                "     WHEN COMT >= 2.5 THEN 2\n" +
                "     WHEN COMT >= 1  THEN 1\n" +
                "     WHEN COMT <  1  THEN 0\n" +
                "     END AS frequency\n" +
                "FROM\n" +
                "(SELECT course, date,\n" +
                "AVG(total_code) as TOTC, AVG(commits) as COMT, AVG(LOCA) as LOCA, AVG(LOCD) as LOCD\n" +
                "FROM\n" +
                "(SELECT students.email as email, students.course as course, students.team_name as team,\n" +
                "commit_data.lines_of_code_added + (commit_data.lines_of_code_deleted/4) as total_code, commit_data.lines_of_code_deleted as LOCD,\n" +
                "commit_data.lines_of_code_added as LOCA,\n" +
                "commit_data.commits as commits, commit_data.date as date\n" +
                "FROM \n" +
                "\t(students\n" +
                "\tinner join\n" +
                "\tcommit_data)\n" +
                "\tWHERE students.email = commit_data.email)first,\n" +
                "    (select @rn \\:= 0) var\n" +
                "    WHERE course = ?1\n" +
                "    AND team = ?2\n" +
                "    GROUP BY date)second\n" +
                "    ORDER BY week DESC LIMIT 2", WeeklyFreqWeight.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        List<WeeklyFreqWeight> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public List<WeeklyFreqWeight> getWeightFreqByStudent(String course, String team, String email) throws DataAccessException {
        Query query = getEntityManager().createNativeQuery("SELECT (@rn \\:= @rn + 1) as week, DATE_ADD(date, INTERVAL 1 DAY) as weekBeginning, DATE_ADD(date, INTERVAL 7 DAY) as weekEnding,\n" +
                "CASE \n" +
                "\t WHEN LOCA < LOCD THEN 1\n" +
                "     WHEN TOTC >= 350 THEN 3\n" +
                "     WHEN TOTC >= 175 THEN 2\n" +
                "     WHEN TOTC >= 25  THEN 1\n" +
                "     WHEN TOTC <  25  THEN 0\n" +
                "     END AS weight,\n" +
                "     \n" +
                "     CASE \n" +
                "     WHEN COMT >= 4 THEN 3\n" +
                "     WHEN COMT >= 2.5 THEN 2\n" +
                "     WHEN COMT >= 1  THEN 1\n" +
                "     WHEN COMT <  1  THEN 0\n" +
                "     END AS frequency\n" +
                "FROM\n" +
                "(SELECT course, date,\n" +
                "total_code as TOTC, commits as COMT, LOCA, LOCD\n" +
                "FROM\n" +
                "(SELECT students.email as email, students.course as course, students.team_name as team,\n" +
                "commit_data.lines_of_code_added + (commit_data.lines_of_code_deleted/2) as total_code, commit_data.lines_of_code_deleted as LOCD,\n" +
                "commit_data.lines_of_code_added as LOCA,\n" +
                "commit_data.commits as commits, commit_data.date as date\n" +
                "FROM \n" +
                "\t(students\n" +
                "\tinner join\n" +
                "\tcommit_data)\n" +
                "\tWHERE students.email = commit_data.email)first,\n" +
                "    (select @rn \\:= 0) var\n" +
                "    WHERE course = ?1\n" +
                "    AND team = ?2\n" +
                "    AND email = ?3\n" +
                "    GROUP BY date)second\n" +
                "    ORDER BY week DESC LIMIT 2", WeeklyFreqWeight.class);
        query.setParameter(1, course);
        query.setParameter(2, team);
        query.setParameter(3, email);
        List<WeeklyFreqWeight> resultList = query.getResultList();
        return resultList;
    }

}
