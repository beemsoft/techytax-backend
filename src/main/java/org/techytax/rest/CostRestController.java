package org.techytax.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.techytax.domain.Cost;
import org.techytax.model.security.User;
import org.techytax.repository.CostRepository;
import org.techytax.security.JwtTokenUtil;
import org.techytax.security.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collection;

@RestController
public class CostRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    private final JwtTokenUtil jwtTokenUtil;

    private final CostRepository costRepository;

    private final UserRepository userRepository;

    @Autowired
    public CostRestController(JwtTokenUtil jwtTokenUtil, CostRepository costRepository, UserRepository userRepository) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.costRepository = costRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "auth/costs", method = RequestMethod.GET)
    public Collection<Cost> getCosts(HttpServletRequest request) {
        User user = getUser(request);
        return costRepository.findByUser(user);
    }

    @RequestMapping(value = "auth/costs/{id}", method = RequestMethod.GET)
    public Cost getCost(HttpServletRequest request, @PathVariable Long id) {
        return costRepository.findById(id).get();
    }

    @RequestMapping(value = "auth/cost", method = { RequestMethod.PUT, RequestMethod.POST })
    public void saveCost(HttpServletRequest request, @RequestBody Cost cost) {
        User user = getUser(request);
        cost.setUser(user);
        costRepository.save(cost);
    }

    @RequestMapping(value = "auth/cost/{id}", method = RequestMethod.DELETE)
    public void deleteCost(HttpServletRequest request, @PathVariable Long id) {
        costRepository.deleteById(id);
    }

    private User getUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return userRepository.findByUsername(username);
    }
}
