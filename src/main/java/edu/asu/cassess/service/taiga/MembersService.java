package edu.asu.cassess.service.taiga;

import edu.asu.cassess.dao.taiga.*;
import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.taiga.MemberData;
import edu.asu.cassess.persist.entity.taiga.ProjectIDSlug;
import edu.asu.cassess.persist.repo.taiga.MemberRepo;
import edu.asu.cassess.service.rest.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Transactional
public class MembersService {

    private RestTemplate restTemplate;

    private String membershipListURL;

    @Autowired
    private MemberRepo MemberDao;

    @Autowired
    private ICourseService courseService;

    @Autowired
    private ProjectQueryDao projectDao;

    public MembersService(){
        restTemplate = new RestTemplate();
        membershipListURL = "https://api.taiga.io/api/v1/memberships?project=";
    }

    public List<MemberData> getMembers(Long projectId, String token, int page) {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.set("Authorization", "Bearer " + token);

        //System.out.println("Headers: " + headers);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<List<MemberData>> memberList = restTemplate.exchange(membershipListURL + projectId + "&page=" + page,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<List<MemberData>>() {});

        //ResponseEntity<List<MemberData>> memberList = restTemplate.getForEntity(membershipListURL, List<>.class, request);

        List<MemberData> members = memberList.getBody();

        for (int i = 0; i < members.size(); i++) {
            MemberDao.save(members.get(i));
        }

        if (memberList.getHeaders().containsKey("x-pagination-next")) {
            page++;
            return getMembers(projectId, token, page);
        } else {

            return members;
        }
    }

    /* Method to provide single operation on
    updating the member_data table based on the course, student and project tables
     */
    public void updateMembership(String course){
        System.out.println("Updating Members");
        Course tempCourse = (Course) courseService.read(course);
        String token = tempCourse.getTaiga_token();
        List<ProjectIDSlug> idSlugList = projectDao.listGetTaigaProjectIDSlug(course);
        for(ProjectIDSlug idSlug:idSlugList){
            System.out.println("Id: " + idSlug.getId());
            getMembers(idSlug.getId(), token, 1);
        }
    }
}
