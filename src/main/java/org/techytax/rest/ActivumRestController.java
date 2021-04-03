package org.techytax.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.techytax.domain.Activum;
import org.techytax.domain.BusinessCar;
import org.techytax.domain.Office;
import org.techytax.repository.ActivumRepository;
import org.techytax.security.JwtTokenUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Collection;

@RestController
public class ActivumRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ActivumRepository activumRepository;

    @RequestMapping(value = "auth/activum", method = RequestMethod.GET)
    public Collection<Activum> getActiva(HttpServletRequest request) {
        String username = getUser(request);
        return activumRepository.findByUser(username);
    }

    @RequestMapping(value = "auth/activum/machine", method = { RequestMethod.PUT, RequestMethod.POST })
    public void saveActivumMachine(HttpServletRequest request, @RequestBody Activum activum) {
        String username = getUser(request);
        activum.setUser(username);
        activumRepository.save(activum);
    }

    @RequestMapping(value = "auth/activum/car", method = { RequestMethod.PUT, RequestMethod.POST })
    public void saveActivumCar(HttpServletRequest request, @RequestBody BusinessCar activum) {
        String username = getUser(request);
        activum.setUser(username);
        activumRepository.save(activum);
    }

    @RequestMapping(value = "auth/activum", method = { RequestMethod.PUT, RequestMethod.POST })
    public void saveActivum(HttpServletRequest request, @RequestBody Activum activum) {
        String username = getUser(request);
        activum.setUser(username);
        activumRepository.save(activum);
    }

    @RequestMapping(value = "auth/activum/car", method = { RequestMethod.GET })
    public BusinessCar getActivumCar(HttpServletRequest request) {
        String username = getUser(request);
        return activumRepository.findBusinessCar(username, LocalDate.now().minusYears(1).withDayOfYear(1), LocalDate.now().withDayOfYear(1).minusDays(1));
    }

    @RequestMapping(value = "auth/activum/{id}", method = { RequestMethod.GET })
    public Activum getActivum(HttpServletRequest request, @PathVariable Long id) {
        String username = getUser(request);
        return activumRepository.findById(id).get();
    }

    @RequestMapping(value = "auth/activum/office", method = { RequestMethod.PUT, RequestMethod.POST })
    public void saveActivumOffice(HttpServletRequest request, @RequestBody Office activum) {
        String username = getUser(request);
        activum.setUser(username);
        activumRepository.save(activum);
    }

    @RequestMapping(value = "auth/activum/{id}", method = RequestMethod.DELETE)
    public void deleteActivum(HttpServletRequest request, @PathVariable Long id) {
        activumRepository.deleteById(id);
    }

    private String getUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        return jwtTokenUtil.getUsernameFromToken(token);
    }
}
