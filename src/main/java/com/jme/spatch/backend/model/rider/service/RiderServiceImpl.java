package com.jme.spatch.backend.model.rider.service;

import com.jme.spatch.backend.config.jwt.JwtService;
import com.jme.spatch.backend.general.dto.RiderVerificationRequest;
import com.jme.spatch.backend.general.dto.*;
import com.jme.spatch.backend.general.responsehandler.ResponseHandler;
import com.jme.spatch.backend.model.rider.entity.*;
import com.jme.spatch.backend.model.order.service.OrderRepository;
import com.jme.spatch.backend.model.token.service.TokenService;
import com.jme.spatch.backend.model.user.entity.Status;
import com.jme.spatch.backend.model.user.entity.UserEntity;
import com.jme.spatch.backend.model.user.service.CustomUserDetailsService;
import com.jme.spatch.backend.model.user.service.PasswordResetRequest;
import com.jme.spatch.backend.model.user.service.UserRepository;
import com.jme.spatch.backend.model.user.service.UserUtils;
import com.jme.spatch.backend.model.wallet.service.WalletService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.jme.spatch.backend.general.mapper.DtoMapper.*;

@Service
@AllArgsConstructor
public class RiderServiceImpl implements RiderService {

    private final UserUtils userUtils;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final WalletService walletService;
    private final RiderRepository riderRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CustomUserDetailsService detailsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity saveRider(RiderRegRequest regRequest) {
        RiderEntity rider = mapRider(regRequest);
        UserEntity existingUserWithPhoneNum = userRepository.findByPhoneNo(rider.getPhoneNo());
            if(existingUserWithPhoneNum != null){
                return ResponseHandler.handle(403,"Phone no. has been taken",null);
            }
         rider.setPassword(new BCryptPasswordEncoder().encode(regRequest.getPassword()));
         riderRepository.save(rider);
         walletService.init(rider);
         tokenService.sendOtpToMobileNum(rider.getPhoneNo());

        return ResponseHandler.handle(201,"Registration Successful",null);
    }

    @Override
    public ResponseEntity addRider(RiderRegRequest regRequest) {
        RiderEntity rider = mapRider(regRequest);
        UserEntity existingUserWithPhoneNum = userRepository.findByPhoneNo(rider.getPhoneNo());
        if(existingUserWithPhoneNum != null){
            return ResponseHandler.handle(403,"Phone no. has been taken",null);
        }
        rider.setPassword(new BCryptPasswordEncoder().encode(regRequest.getPassword()));
        rider.setStatus(Status.RIDER_VERIFIED);
        rider.setLocation(DeliveryLocation.LAGOS);
        rider.setVehicle(VehicleType.BIKE);
        riderRepository.save(rider);
        walletService.init(rider);

        return ResponseHandler.handle(201,"Registration Successful",null);
    }

    @Override
    public ResponseEntity reSendMobileOtp(ResetDto resetDto) {
        String phoneNumber = resetDto.getPhoneNum();
        RiderEntity rider = riderRepository.findByPhoneNo(phoneNumber);
        if(rider == null){
            return  ResponseHandler.handle(403,"Rider not found", null);
        }

        if(rider.getStatus() != Status.RIDER_UNVERIFIED){
            return  ResponseHandler.handle(402,"You're not permitted to access this endpoint", null);
        }

        tokenService.sendOtpToMobileNum(phoneNumber);

        return  ResponseHandler.handle(200,"Otp has been sent to mobile no."+ phoneNumber, null);
    }


    @Override
    public ResponseEntity verifyRider(RiderVerificationRequest vRequest) {
        String otp = vRequest.getOtp();
        String origin = vRequest.getOrigin();


       RiderEntity rider = riderRepository.findByPhoneNo(origin);
       if(rider == null){
           return ResponseHandler.handle(404,"No user Found with mobile no. "+origin , null);
       }

        try{
            boolean isValid =  tokenService.validateToken(origin, otp);
            if(!isValid){
                return ResponseHandler.handle(401,"Otp has expired", null);
            }else{
                rider.setStatus(Status.RIDER_VERIFIED);
                riderRepository.save(rider);
            }
        }catch (NullPointerException e){

            return ResponseHandler.handle(401,"Invalid Otp", null);
        }
        catch (IllegalIdentifierException e){
            return ResponseHandler.handle(401,"Otp was not assign to user with mobile "+ origin, null);
        }
        RiderAccount account = new RiderAccount();
        account.setFullName(rider.getFullName());
        account.setBalance(BigDecimal.valueOf(walletService.getBalance(rider)));
        account.setPhoneNo(rider.getPhoneNo());
        account.setImagePath(rider.getImagePath());
        account.setOrderList(rider.getOrders());


        return ResponseHandler.handle(200,"Registration has been completed successfully",account);
    }

    @Override
    public ResponseEntity logRiderIn(AuthenticationRequest request){
        String phoneNo = request.getEmailOrPhoneNo();
        String password = request.getPassword();

        try{
            RiderEntity rider = riderRepository.findByEmailOrPhoneNo(phoneNo,phoneNo);
                if(rider.getStatus() != Status.RIDER_VERIFIED){
                    return  ResponseHandler.handle(403,"Rider is not verified", null);
                }else{
                    String usersPassword = rider.getPassword();
                    Boolean isCorrectPassword = passwordEncoder.matches(password,usersPassword);
                        if(!isCorrectPassword){
                            return  ResponseHandler.handle(403,"Password isn't correct!", null);
                        }
                }
            final UserDetails userDetails = detailsService.loadUserByUsername(phoneNo);
            rider.setLastLogin(LocalDateTime.now());
            rider.setStatusActive(ActiveStatus.ACTIVE);
            riderRepository.save(rider);
            Authentication authentication =  authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(phoneNo,request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (NullPointerException e){
            return  ResponseHandler.handle(403,"Rider not found", null);
        }
        final String jwt = jwtService.generateToken(phoneNo);

        return  ResponseEntity.ok(new AuthenticationResponse(jwt));
    }


    @Override
    public ResponseEntity sendResetPasswordOtp(String phoneNo)
            throws MessagingException, UnsupportedEncodingException {
        RiderEntity rider = riderRepository.findByPhoneNo(phoneNo);
        if(rider == null){
            return  ResponseHandler.handle(403,"Rider not found", null);
        }

        if(rider.getStatus() != Status.RIDER_VERIFIED){
            return  ResponseHandler.handle(402,"You're not permitted to access this endpoint, Because Registration was not completed!", null);
        }

        tokenService.sendOtpToMobileNum(phoneNo);

        return  ResponseHandler.handle(200,"Otp has been sent to mobile no."+ phoneNo, null);
    }

    @Override
    public ResponseEntity resetPassword(
            PasswordResetRequest passwordRequest) {

        String otp = passwordRequest.getOtp();
        String mobileNo = passwordRequest.getEmailOrPhoneNum();
        String password = passwordRequest.getPassword();

        RiderEntity rider = riderRepository.findByPhoneNo(mobileNo);
        if(rider == null){
            return ResponseHandler.handle(403,"No Rider found with mobile "+ mobileNo, null);
        }
        try{
            boolean isValid =  tokenService.validateToken(mobileNo, otp);
            if(!isValid){
                return ResponseHandler.handle(401,"Otp has expired", null);
            }else{
                rider.setPassword(password);
                riderRepository.save(rider);
            }
        }catch (NullPointerException e){

            return ResponseHandler.handle(401,"Invalid Otp", null);
        }
        catch (IllegalIdentifierException e){
            return ResponseHandler.handle(401,"Otp was not assign to user with mobile "+ mobileNo, null);
        }


        return ResponseHandler.handle(200,"Password reset was successfully", null);
    }

    @Override
    public ResponseEntity updatePassword(
            PasswordUpdateRequest updateRequest,
            HttpServletRequest servletRequest
    ) {

        RiderEntity rider = userUtils.extractRider(servletRequest);

        String password = rider.getPassword();
        String currentPassword = updateRequest .getCurrentPassword();
        String newPassword = updateRequest.getNewPassword();
            Boolean match = new BCryptPasswordEncoder().matches(currentPassword,password);
        if(!match){
            return ResponseHandler.handle(403,"Current password is invalid",null);
        }
            rider.setPassword(new BCryptPasswordEncoder().encode(newPassword));
            riderRepository.save(rider);
        return ResponseHandler.handle(200,"Password updated successfully", rider);
    }

    @Override
    public ResponseEntity updateProfile(
            ProfileUpdateRequest updateRequest,
            HttpServletRequest servletRequest) {

        RiderEntity rider = userUtils.extractRider(servletRequest);
        String fullName = updateRequest.getFullName();
        String phoneNumber = updateRequest.getPhoneNo();

        if(fullName != null){
            if(fullName.length() > 2 ){
                rider.setFullName(fullName);
            }
        }


        if(phoneNumber != null) {
            if (phoneNumber.length() >= 11) {
                RiderEntity existingUserWithPhoneNumber = riderRepository.findByPhoneNo(phoneNumber);
                if(existingUserWithPhoneNumber != null ){
                    if(existingUserWithPhoneNumber == rider){

                    }else{
                        return ResponseHandler.handle(400,"Phone no. has been registered already", null);
                    }
                }else{
                    rider.setPhoneNo(phoneNumber);
                }
            }
        }


        riderRepository.save(rider);

        return ResponseHandler.handle(200,"Profile updated successfully", rider);
    }

    @Override
    public ResponseEntity getDetails(HttpServletRequest servletRequest) {
        RiderEntity rider = userUtils.extractRider(servletRequest);
        if(rider == null){
            return ResponseHandler.handle(404,"Rider not found",null);
        }
        RiderAccount account = new RiderAccount();
            account.setFullName(rider.getFullName());
            account.setBalance(BigDecimal.valueOf(walletService.getBalance(rider)));
            account.setPhoneNo(rider.getPhoneNo());
            account.setImagePath(rider.getImagePath());
            account.setOrderList(rider.getOrders());


        return ResponseHandler.handle(200,"Rider detail's retrieved successfully",account);
    }

    @Override
    public RiderEntity getRiderToDeliver(){
        List<RiderEntity> activeRiders = riderRepository.findByStatusActive(ActiveStatus.ACTIVE);

        if(!activeRiders.isEmpty()){
            if(activeRiders.size() == 1 ){
                RiderEntity activeRider = activeRiders.get(0);
                return activeRider;
            }else{
                Random random = new Random();
                int ranNum = random.nextInt(activeRiders.size() -1);
                return activeRiders.get(ranNum);
            }
        }else{
            return null;
        }


    }

    @Override
    public ResponseEntity getAllRiders() {
        List<RiderEntity> riders =  riderRepository.findAll();
        List<RiderDto> riderDtoList = riders.stream().map(rider -> mapRiderDto(rider)).collect(Collectors.toList());
        return ResponseHandler.handle(200,"Riders retrieved successfully ", riderDtoList);
    }

    @Override
    public ResponseEntity getRiderDetails(long userId) {

        RiderEntity rider = riderRepository.findById(userId).get();
        RiderDetails riderDetails = mapRiderDetails(rider) ;

       return ResponseHandler.handle(200,"Rider's details retrieved successfully",riderDetails);
    }

    @Override
    public List<RiderEntity> getAllActiveRiders() {
        List<RiderEntity> allRiders = riderRepository.findByStatusActive(ActiveStatus.ACTIVE);
        return allRiders;
    }


}
