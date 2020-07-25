package org.techytax.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.techytax.domain.Activity;
import org.techytax.repository.ActivityRepository;
import org.techytax.security.JwtTokenUtil;

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

    @RequestMapping(value = "auth/activity", method = RequestMethod.GET)
    public Collection<Activity> getActivities(HttpServletRequest request) {
        String username = getUser(request);
        return activityRepository.findByUser(username);
    }

    @RequestMapping(value = "auth/activity/{id}", method = RequestMethod.GET)
    public Activity getActivity(HttpServletRequest request, @PathVariable Long id) {
        return activityRepository.findById(id).get();
    }

    @RequestMapping(value = "auth/activity/project/{id}", method = RequestMethod.GET)
    public Collection<Activity> getActivitiesForProject(HttpServletRequest request, @PathVariable Long id) {
        String username = getUser(request);
        return activityRepository.getActivitiesForProject(username, id, LocalDate.now().minusMonths(1).withDayOfMonth(1), LocalDate.now().withDayOfMonth(1).minusDays(1));
    }

    @RequestMapping(value = "auth/activity", method = { RequestMethod.PUT, RequestMethod.POST })
    public void saveActivity(HttpServletRequest request, @RequestBody Activity activity) {
        String username = getUser(request);
        activity.setUser(username);
        activityRepository.save(activity);
    }

    @RequestMapping(value = "auth/activity/{id}", method = RequestMethod.DELETE)
    public void deleteActivity(HttpServletRequest request, @PathVariable Long id) {
        activityRepository.deleteById(id);
    }

    private String getUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        return jwtTokenUtil.getUsernameFromToken(token);
    }
}
