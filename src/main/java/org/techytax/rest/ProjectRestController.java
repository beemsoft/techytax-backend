package org.techytax.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.techytax.domain.Project;
import org.techytax.model.security.User;
import org.techytax.repository.ProjectRepository;
import org.techytax.security.JwtTokenUtil;
import org.techytax.security.repository.UserRepository;

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

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "auth/project", method = RequestMethod.GET)
    public Collection<Project> getProjects(HttpServletRequest request) {
        User user = getUser(request);
        return projectRepository.findByUser(user);
    }

    @RequestMapping(value = "auth/project/current", method = RequestMethod.GET)
    public Collection<Project> getCurrentProjects(HttpServletRequest request) {
        User user = getUser(request);
        return projectRepository.findCurrentProjects(user);
    }

    @RequestMapping(value = "auth/project/{id}", method = RequestMethod.GET)
    public Project getProject(HttpServletRequest request, @PathVariable Long id) {
        return projectRepository.findById(id).get();
    }

    @RequestMapping(value = "auth/project", method = { RequestMethod.PUT, RequestMethod.POST })
    public void saveProject(HttpServletRequest request, @RequestBody Project project) {
        User user = getUser(request);
        project.setUser(user);
        projectRepository.save(project);
    }

    @RequestMapping(value = "auth/project/{id}", method = RequestMethod.DELETE)
    public void deleteProject(HttpServletRequest request, @PathVariable Long id) {
        projectRepository.deleteById(id);
    }

    private User getUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return userRepository.findByUsername(username);
    }
}
