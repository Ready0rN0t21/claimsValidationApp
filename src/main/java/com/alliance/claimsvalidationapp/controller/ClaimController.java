package com.alliance.claimsvalidationapp.controller;

import com.alliance.claimsvalidationapp.entity.Claim;
import com.alliance.claimsvalidationapp.entity.User;
import com.alliance.claimsvalidationapp.service.ClaimService;
import com.alliance.claimsvalidationapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Controller
@RequestMapping("/claim")
@RequiredArgsConstructor
public class ClaimController {

    private final ClaimService claimService;

    @PostMapping("/addClaim")
    @ResponseBody
    public ModelAndView addClaimController(@RequestParam("invoice") MultipartFile multipartFile, @RequestParam String company, HttpSession httpSession) throws IOException, ParseException {
        User user = (User) httpSession.getAttribute("User");
        String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        Date dateNow = new Date();

        SimpleDateFormat billFormat = new SimpleDateFormat("YYYY-MM-dd");
        String bd = billFormat.format(dateNow);

        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
        String uiDateTime = ft.format(dateNow);
        String transNo = uiDateTime + user.getId();

        Claim claim = new Claim();
        claim.setInvoice(multipartFile.getBytes());
        claim.setFilename(filename);
        claim.setCompany(company);
        claim.setUser(user);
        claim.setTransactionNo(transNo);
        claim.setBilling_date(bd);

        claimService.addClaimService(claim);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("employeePage");
       return  modelAndView;
    }

    @GetMapping("/download")
    public void downloadFileController(@Param("id") Long id, HttpServletResponse httpServletResponse) throws Exception {
        Claim claim = claimService.getClaimRecordService(id);
        httpServletResponse.setContentType("application/octet-stream");
        String headerkey = "Content-Disposition";
        String headerValue = "attachment; filename="+ claim.getFilename();

        httpServletResponse.setHeader(headerkey, headerValue);

        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        outputStream.write(claim.getInvoice());
        outputStream.close();
    }

    @GetMapping("/getClaimRecord")
    @ResponseBody
    public Claim getClaimRecordController(Long id) throws Exception {
        Claim claim = claimService.getClaimRecordService(id);

        return claim;
    }

    @GetMapping("/getClaimStatus")
    @ResponseBody
    public String getClaimStatusController(Long id) throws Exception {
        String status = claimService.getClaimStatusService(id);

        return status;
    }

    @PostMapping("/updateClaimRelease")
    @ResponseBody
    public void updateClaimReleaseController(Long id, String net_amt, String commission) throws Exception {
        claimService.updateClaimReleaseService(id, net_amt, commission);
    }

    @PostMapping("/tagClaimDone")
    @ResponseBody
    public void tagClaimDoneController(Long id) throws Exception {
        claimService.tagClaimDoneService(id);
    }
}
