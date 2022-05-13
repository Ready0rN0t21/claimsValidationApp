package com.alliance.claimsvalidationapp.service;

import com.alliance.claimsvalidationapp.entity.Claim;
import com.alliance.claimsvalidationapp.repository.ClaimRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClaimService {
    private final ClaimRespository claimRespository;
    private final EmailSenderService emailSenderService;

    public Claim addClaimService(Claim claim){
        String messageToAccounting = claim.getUser().getFirstName()+" "+claim.getUser().getLastName()+" submitted a claim on "
                + claim.getBilling_date() + " with transactioN number : "+claim.getTransactionNo();

        String messageToSender = "You have successfully submitted a claim on "+claim.getBilling_date()+" with transaction number: "
                + claim.getTransactionNo()+". Please wait for updates. The system will notify you through your email";

        emailSenderService.sendEmail("alliance.claims1@gmail.com", claim.getUser().getEmail(), "New Claim issued", messageToAccounting);
        emailSenderService.sendEmail(claim.getUser().getEmail(), "alliance.claims1@gmail.com", "Claim submitted", messageToSender);
        return claimRespository.save(claim);
    }

    public List<Claim> getAllClaims(){
        return claimRespository.findAll();
    }

    public Claim getClaimRecordService(Long id) throws Exception {
        Optional<Claim> result = claimRespository.findById(id);

        if(!result.isPresent()){
            throw new Exception("Could not find claim with ID no. "+id);
        }
        Claim claim = result.get();

        return claim;
    }

    public String getClaimStatusService(Long id) throws Exception {
        Claim claim = getClaimRecordService(id);
        String status = "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();

        LocalDate billingdate = LocalDate.parse(claim.getBilling_date(), formatter);
        LocalDate releaseDate = null;
        LocalDate forCollectionDays = null;

        if(claim.getRelease_date() != null) {
            releaseDate = LocalDate.parse(claim.getRelease_date(), formatter);
            forCollectionDays = releaseDate.plusDays(120);
        }
        LocalDate forReleaseDays = billingdate.plusDays(60);
        LocalDate overDueDays = billingdate.plusDays(120);


        if(now.isBefore(forReleaseDays) && claim.getRelease_date() == null){
            status = "for commission release";
        }

        if(claim.getRelease_date() != null && claim.getCollection_date() == null && now.isBefore(forCollectionDays)){
            status = "for collection";
        }

        if(claim.getCollection_date() != null) {
            status = "collected";
        }

        if(now.isAfter(overDueDays) && claim.getRelease_date() == null && claim.getCollection_date() == null) {
            status = "overdue";
        }
        return status;
    }

    public void updateClaimReleaseService(Long id, String net_amt, String commission) throws Exception {
        Claim claim = null;
        Date dateNow = new Date();
        SimpleDateFormat billFormat = new SimpleDateFormat("YYYY-MM-dd");
        String rd = billFormat.format(dateNow);
        if(claimRespository.findById(id).isPresent()){
            claim = claimRespository.findById(id).get();
            claim.setCommission(Double.parseDouble(commission));
            claim.setNet_amt(Double.parseDouble(net_amt));
            claim.setRelease_date(rd);

            String messageToEmployee = "You are now able to collect your claim that you issued on " + claim.getBilling_date() +
                    " with the transaction number: "+claim.getTransactionNo();
            emailSenderService.sendEmail(claim.getUser().getEmail(), "alliance.claims1@gmail.com", "Collect your claim", messageToEmployee);
        } else {
            throw new Exception("Claim not found");
        }


        claimRespository.save(claim);
    }

    public void tagClaimDoneService(Long id) throws Exception {
        Claim claim = null;
        Date dateNow = new Date();
        SimpleDateFormat billFormat = new SimpleDateFormat("YYYY-MM-dd");
        String cd = billFormat.format(dateNow);
        if(claimRespository.findById(id).isPresent()){
            claim = claimRespository.findById(id).get();
            claim.setCollection_date(cd);
            String messageToEmployee = "You have successfully collected your claim with transaction number: "+claim.getTransactionNo();
            String messageToAccounting = "Claim with transaction number "+ claim.getTransactionNo() + ", was tagged as done";
            emailSenderService.sendEmail("alliance.claims1@gmail.com", "alliance.claims1@gmail.com", "Claim collected", messageToAccounting);
            emailSenderService.sendEmail(claim.getUser().getEmail(), "alliance.claims1@gmail.com", "Claim collected", messageToEmployee);
        } else {
            throw new Exception("Claim not found");
        }

        claimRespository.save(claim);
    }
}
