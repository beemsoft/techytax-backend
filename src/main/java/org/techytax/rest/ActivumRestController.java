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
import org.techytax.helper.ActivaHelper;
import org.techytax.model.security.User;
import org.techytax.repository.ActivumRepository;
import org.techytax.security.JwtTokenUtil;
import org.techytax.security.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.Collection;

@RestController
public class ActivumRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ActivumRepository activumRepository;

    @Autowired
    private ActivaHelper activaHelper;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "auth/activum", method = RequestMethod.GET)
    public Collection<Activum> getActiva(HttpServletRequest request) {
        User user = getUser(request);
        return activumRepository.findByUser(user);
    }

    @RequestMapping(value = "auth/activum/machine", method = { RequestMethod.PUT, RequestMethod.POST })
    public void saveActivumMachine(HttpServletRequest request, @RequestBody Activum activum) {
        User user = getUser(request);
        activum.setUser(user);
        activumRepository.save(activum);
    }

    @RequestMapping(value = "auth/activum/car", method = { RequestMethod.PUT, RequestMethod.POST })
    public void saveActivumCar(HttpServletRequest request, @RequestBody BusinessCar activum) {
        User user = getUser(request);
        activum.setUser(user);
        activumRepository.save(activum);
    }

    @RequestMapping(value = "auth/activum", method = { RequestMethod.PUT, RequestMethod.POST })
    public void saveActivum(HttpServletRequest request, @RequestBody Activum activum) {
        User user = getUser(request);
        activum.setUser(user);
        activumRepository.save(activum);
    }

    @RequestMapping(value = "auth/activum/car/vat-correction-for-private-usage", method = { RequestMethod.GET })
    public BigInteger getActivumCar(HttpServletRequest request) {
        User user = getUser(request);
        return activaHelper.getVatCorrectionForPrivateUsageBusinessCar(user);
    }

    @RequestMapping(value = "auth/activum/{id}", method = { RequestMethod.GET })
    public Activum getActivum(HttpServletRequest request, @PathVariable Long id) {
        return activumRepository.findById(id).get();
    }

    @RequestMapping(value = "auth/activum/office", method = { RequestMethod.PUT, RequestMethod.POST })
    public void saveActivumOffice(HttpServletRequest request, @RequestBody Office activum) {
        User user = getUser(request);
        activum.setUser(user);
        activumRepository.save(activum);
    }

    @RequestMapping(value = "auth/activum/{id}", method = RequestMethod.DELETE)
    public void deleteActivum(HttpServletRequest request, @PathVariable Long id) {
        activumRepository.deleteById(id);
    }

    private User getUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return userRepository.findByUsername(username);
    }
}
