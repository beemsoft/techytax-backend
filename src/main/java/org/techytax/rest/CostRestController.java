package org.techytax.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.techytax.domain.Cost;
import org.techytax.model.security.User;
import org.techytax.repository.CostRepository;
import org.techytax.security.JwtTokenUtil;
import org.techytax.security.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collection;

@RestController
public class CostRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    private final String imageStoragePath = "images";

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

    @RequestMapping(value = "auth/vat-costs", method = RequestMethod.GET)
    public Collection<Cost> getVatCosts(HttpServletRequest request) {
        User user = getUser(request);
        return costRepository.findVatCosts(user, LocalDate.now().minusMonths(3).withDayOfMonth(1), LocalDate.now().withDayOfMonth(1).minusDays(1));
    }

    @RequestMapping(value = "auth/costs/{id}", method = RequestMethod.GET)
    public Cost getCost(@PathVariable Long id) {
        return costRepository.findById(id).get();
    }

    @RequestMapping(value = "auth/cost/image/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getCostImage(@PathVariable String fileName) throws IOException {
        Path path = Paths.get(imageStoragePath).resolve(fileName);
        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }
        byte[] image = Files.readAllBytes(path);
        String contentType = Files.probeContentType(path);
        if (contentType == null) {
            contentType = "image/jpeg";
        }
        return ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                .body(image);
    }

    @RequestMapping(value = "auth/cost", method = { RequestMethod.PUT, RequestMethod.POST }, consumes = { "multipart/form-data" })
    public void saveCost(HttpServletRequest request,
                         @RequestPart("cost") String costJson,
                         @RequestPart(value = "billImage", required = false) MultipartFile billImage,
                         @RequestPart(value = "itemImage", required = false) MultipartFile itemImage) throws IOException {
        User user = getUser(request);
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        mapper.findAndRegisterModules();
        Cost cost = mapper.readValue(costJson, Cost.class);
        cost.setUser(user);
        if (billImage != null) {
            cost.setBillImage(saveImage(billImage));
        }
        if (itemImage != null) {
            cost.setItemImage(saveImage(itemImage));
        }
        costRepository.save(cost);
    }

    private String saveImage(MultipartFile image) throws IOException {
        Path uploadPath = Paths.get(imageStoragePath);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(image.getInputStream(), filePath);
        return fileName;
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
