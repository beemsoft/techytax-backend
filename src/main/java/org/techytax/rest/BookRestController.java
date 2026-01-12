package org.techytax.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.techytax.domain.BookValue;
import org.techytax.model.security.User;
import org.techytax.repository.BookRepository;
import org.techytax.security.JwtTokenUtil;
import org.techytax.security.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@RestController
public class BookRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "auth/book", method = RequestMethod.GET)
    public Collection<BookValue> getBookValues(HttpServletRequest request) {
        User user = getUser(request);
        return bookRepository.findByUser(user);
    }

    @RequestMapping(value = "auth/book/{id}", method = RequestMethod.GET)
    public BookValue getBookValue(HttpServletRequest request, @PathVariable Long id) {
        return bookRepository.findById(id).get();
    }

    @RequestMapping(value = "auth/book", method = { RequestMethod.PUT, RequestMethod.POST })
    public void saveBookValue(HttpServletRequest request, @RequestBody BookValue bookValue) {
        User user = getUser(request);
        bookValue.setUser(user);
        bookRepository.save(bookValue);
    }

    @RequestMapping(value = "auth/book/{id}", method = RequestMethod.DELETE)
    public void deleteBookValue(HttpServletRequest request, @PathVariable Long id) {
        bookRepository.deleteById(id);
    }

    private User getUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return userRepository.findByUsername(username);
    }
}
