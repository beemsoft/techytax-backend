package org.techytax.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.techytax.domain.CostMatch;
import org.techytax.repository.CostMatchRepository;
import org.techytax.security.JwtTokenUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@RestController
public class CostMatchRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private CostMatchRepository costMatchRepository;

    @RequestMapping(value = "auth/match", method = RequestMethod.GET)
    public Collection<CostMatch> getCostMatches(HttpServletRequest request) {
        String username = getUser(request);
        return costMatchRepository.findByUser(username);
    }

    @RequestMapping(value = "auth/match/{id}", method = RequestMethod.GET)
    public CostMatch getCostMatch(HttpServletRequest request, @PathVariable Long id) {
        return costMatchRepository.findById(id).get();
    }

    @RequestMapping(value = "auth/match", method = { RequestMethod.PUT, RequestMethod.POST })
    public void saveCostMatch(HttpServletRequest request, @RequestBody CostMatch costMatch) {
        String username = getUser(request);
        costMatch.setUser(username);
        costMatchRepository.save(costMatch);
    }

    @RequestMapping(value = "auth/match/{id}", method = RequestMethod.DELETE)
    public void deleteCostMatch(HttpServletRequest request, @PathVariable Long id) {
        costMatchRepository.deleteById(id);
    }

    private String getUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        return jwtTokenUtil.getUsernameFromToken(token);
    }
}
