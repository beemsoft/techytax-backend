package org.techytax.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.techytax.domain.Project;
import org.techytax.repository.ProjectRepository;
import org.techytax.security.JwtTokenUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@RestController
public class ProjectRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ProjectRepository projectRepository;

    @RequestMapping(value = "auth/project", method = RequestMethod.GET)
    public Collection<Project> getProjects(HttpServletRequest request) {
        String username = getUser(request);
        return projectRepository.findByUser(username);
    }

    @RequestMapping(value = "auth/project/current", method = RequestMethod.GET)
    public Collection<Project> getCurrentProjects(HttpServletRequest request) {
        String username = getUser(request);
        return projectRepository.findCurrentProjects(username);
    }

    @RequestMapping(value = "auth/project/{id}", method = RequestMethod.GET)
    public Project getProject(HttpServletRequest request, @PathVariable Long id) {
        return projectRepository.findById(id).get();
    }

    @RequestMapping(value = "auth/project", method = { RequestMethod.PUT, RequestMethod.POST })
    public void saveProject(HttpServletRequest request, @RequestBody Project project) {
        String username = getUser(request);
        project.setUser(username);
        projectRepository.save(project);
    }

    @RequestMapping(value = "auth/project/{id}", method = RequestMethod.DELETE)
    public void deleteProject(HttpServletRequest request, @PathVariable Long id) {
        projectRepository.deleteById(id);
    }

    private String getUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        return jwtTokenUtil.getUsernameFromToken(token);
    }
}
