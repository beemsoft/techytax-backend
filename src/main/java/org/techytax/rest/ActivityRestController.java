package org.techytax.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.techytax.domain.Activity;
import org.techytax.model.security.User;
import org.techytax.repository.ActivityRepository;
import org.techytax.security.JwtTokenUtil;
import org.techytax.security.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Collection;

@RestController
public class ActivityRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "auth/activity", method = RequestMethod.GET)
    public Collection<Activity> getActivities(HttpServletRequest request) {
        User user = getUser(request);
        return activityRepository.findByUser(user);
    }

    @RequestMapping(value = "auth/activity/{id}", method = RequestMethod.GET)
    public Activity getActivity(HttpServletRequest request, @PathVariable Long id) {
        return activityRepository.findById(id).get();
    }

    @RequestMapping(value = "auth/activity/project/{id}", method = RequestMethod.GET)
    public Collection<Activity> getActivitiesForProject(HttpServletRequest request, @PathVariable Long id) {
        User user = getUser(request);
        return activityRepository.getActivitiesForProject(user, id, LocalDate.now().minusMonths(1).withDayOfMonth(1), LocalDate.now().withDayOfMonth(1).minusDays(1));
    }

    @RequestMapping(value = "auth/activity", method = { RequestMethod.PUT, RequestMethod.POST })
    public void saveActivity(HttpServletRequest request, @RequestBody Activity activity) {
        User user = getUser(request);
        activity.setUser(user);
        activityRepository.save(activity);
    }

    @RequestMapping(value = "auth/activity/{id}", method = RequestMethod.DELETE)
    public void deleteActivity(HttpServletRequest request, @PathVariable Long id) {
        activityRepository.deleteById(id);
    }

    private User getUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return userRepository.findByUsername(username);
    }
}
