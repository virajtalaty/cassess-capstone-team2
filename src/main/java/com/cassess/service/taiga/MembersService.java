package com.cassess.service.taiga;

import com.cassess.dao.CAssessDAO;
import com.cassess.dao.taiga.*;
import com.cassess.entity.taiga.MemberData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class MembersService {

    private RestTemplate restTemplate;

    private String membershipListURL;

    @Autowired
    private CAssessDAO MemberDao;

    public MembersService(){
        restTemplate = new RestTemplate();
        membershipListURL = "https://api.taiga.io/api/v1/memberships?project=";
    }

    public MemberData[] getMembers(Long projectId, String token, int page) {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.set("Authorization", "Bearer " + token);

        System.out.println("Headers: " + headers);

        HttpEntity<String> request = new HttpEntity<>(headers);

        membershipListURL = membershipListURL + projectId + "&page=" + page;

        ResponseEntity<MemberData[]> memberList = restTemplate.getForEntity(membershipListURL, MemberData[].class, request);

        MemberData[] members = memberList.getBody();

        for (int i = 0; i < members.length; i++) {
            MemberDao.save(members[i]);
        }

        if (memberList.getHeaders().containsKey("x-pagination-next")) {
            page++;
            return getMembers(projectId, token, page);
        } else {

            return members;
        }
    }
}
