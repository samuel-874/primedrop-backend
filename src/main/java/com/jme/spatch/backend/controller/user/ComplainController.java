package com.jme.spatch.backend.controller.user;

import com.jme.spatch.backend.model.complain.service.ComplainService;
import com.jme.spatch.backend.general.dto.ComplainRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/spatch")
@AllArgsConstructor
public class ComplainController {
    private final ComplainService complainService;
    private final HttpServletRequest servletRequest;


    @PostMapping("/contact_support")
    public ResponseEntity logComplain(
            @RequestBody ComplainRequest complainRequest){
        return complainService.contactSupport(servletRequest,complainRequest);
    }


    @GetMapping
    public ResponseEntity viewComplain(@RequestParam long id){
        return complainService.viewReport(id);
    }
    @GetMapping("all")
    public ResponseEntity viewUsersComplains(){
        return complainService.viewUserReports(servletRequest);
    }

}
