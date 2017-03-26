package edu.asu.cassess.service.rest;

import edu.asu.cassess.persist.entity.rest.Team;
import edu.asu.cassess.persist.entity.taiga.Slugs;
import edu.asu.cassess.persist.entity.taiga.TeamNames;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Thomas on 3/25/2017.
 */
public interface ITeamsService {

    <T> Object create(Team team);

    <T> Object update(Team team);

    <T> Object read(String team);

    <T> Object delete(String team);

    <T> List<Team> listReadAll();

    <T> List<Team> listReadByCourse(String course);

    JSONObject listCreate(List<Team> teams);

    JSONObject listUpdate(List<Team> teams);

    List<Slugs> listGetSlugs(String course);

    List<TeamNames> listGetTeamNames(String course);

    <T> Object deleteByCourse(String course);
}