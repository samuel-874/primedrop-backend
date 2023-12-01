package com.jme.spatch.backend.model.user.service;

import com.jme.spatch.backend.config.jwt.JwtService;
import com.jme.spatch.backend.config.twilio.TwilioSenderService;
import com.jme.spatch.backend.general.dto.*;
import com.jme.spatch.backend.general.responsehandler.ResponseHandler;
import com.jme.spatch.backend.model.token.service.TokenService;
import com.jme.spatch.backend.model.user.entity.Role;
import com.jme.spatch.backend.model.user.entity.Status;
import com.jme.spatch.backend.model.user.entity.UserEntity;
import com.jme.spatch.backend.model.wallet.entity.Wallet;
import com.jme.spatch.backend.model.wallet.service.WalletRepository;
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

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    private final  UserRepository userRepository;
    private final TwilioSenderService senderService;
    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UserUtils userUtils;




    @Override
    public ResponseEntity sendVerificationMail(
            String email
    ) throws
            MessagingException,
            UnsupportedEncodingException
    {
        UserEntity existingUser = userRepository.findByEmail(email);
        if(existingUser != null){
            return ResponseHandler.handle(403,"Email has been taken", null);
        }

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setStatus(Status.UNVERIFIED);
        userRepository.save(user);

            tokenService.sendOtpToEmail(email);


        return ResponseHandler.handle(200,"Verify  email", null);
    }

    @Override
    public Boolean isEmailAvailable(String email){
        UserEntity user = userRepository.findByEmail(email);
       return user == null;
    }

       @Override
    public ResponseEntity reSendVerificationMail(
            String email
    ) throws
            MessagingException,
            UnsupportedEncodingException
    {
        UserEntity existingUser = userRepository.findByEmail(email);
        if(existingUser == null){
            return ResponseHandler.handle(403,"User with email " + email + "  was not found", null);
        }
        if(existingUser.getStatus() != Status.UNVERIFIED){
            return ResponseHandler.handle(403,"You are not permitted to access this service", null);
        }
        tokenService.sendOtpToEmail(email);

        return ResponseHandler.handle(200,"Verify  email has been resent", null);
    }



    @Override
    public ResponseEntity verifyEmail(
            String email, String otp
    ) {
        UserEntity user = userRepository.findByEmail(email);

            if(user == null){
                return ResponseHandler.handle(403,"User with email " + email+ " was not found", null);
            }
            try{
                boolean valid = tokenService.validateToken(email, otp);
                if(!valid){
                    return ResponseHandler.handle(401,"Token has expired", null);
                }
            }catch (NullPointerException e){
                return ResponseHandler.handle(401,"Invalid Otp", null);
            }
            catch (IllegalIdentifierException e){
                return ResponseHandler.handle(401,"Otp was not assign to user!", null);
            }


            user.setRole(Role.ROLE_USER);
            user.setStatus(Status.VERIFIED);

            userRepository.save(user);
            Wallet wallet = new Wallet();
            wallet.setUser(user);
            wallet.setBalance(BigDecimal.valueOf(0));
            walletRepository.save(wallet);

        return ResponseHandler.handle(200,"Email verified", null);
    }

    @Override
    public ResponseEntity sendMobileOtp(
            RegCompletionRequest completionRequest,
                                    String email
    ) {
            UserEntity user = userRepository.findByEmail(email);
            UserEntity existingUserWithPhoneNum = userRepository.findByPhoneNo(completionRequest.getPhoneNo());
                if(user == null){
                    return  ResponseHandler.handle(404,"User with email :" + email + "was not found",null);
                } else if (user.getStatus() == Status.UNVERIFIED) {
                    return ResponseHandler.handle(401,"Email has not been verified", email);
                } else if (user.getStatus() == Status.PROFILE_COMPLETED) {
                    return ResponseHandler.handle( 403,"User has completed profile already", user);
                } else if (existingUserWithPhoneNum != null) {
                    return ResponseHandler.handle( 403,"Mobile no. has been taken", user);
                }

        user.setStatus(Status.VERIFIED);
                user.setFullName(completionRequest.getFullName());
                user.setPassword(new BCryptPasswordEncoder().encode(completionRequest.getPassword()));
                user.setPhoneNo(completionRequest.getPhoneNo());
                userRepository.save(user);
                tokenService.sendOtpToMobileNum(completionRequest.getPhoneNo());


        return ResponseHandler.handle(200,"Verify mobile no.", null);
    }

    @Override
    public ResponseEntity reSendMobileOtp(

            MobileVerificationRequest vRequest) {

            String phoneNum = vRequest.getMobile();
            if(phoneNum == null){
                return ResponseHandler.handle(401,"Mobile no. required",null);

            }
            UserEntity user = userRepository.findByPhoneNo(phoneNum);

                if(user == null){
                    return ResponseHandler.handle(401,"User with mobile no. :" + phoneNum + " was not found",null);
                } else if (user.getStatus() == Status.UNVERIFIED) {
                    return ResponseHandler.handle(401,"User has not been verified",null);
                } else if (user.getStatus() == Status.PROFILE_COMPLETED) {
                    return ResponseHandler.handle( 403,"User has completed profile already", user);
                } else if(user.getPhoneNo() == null){
                    throw new RuntimeException("Invalid dto to re-send mobile otp");
                }
                tokenService.sendOtpToMobileNum(phoneNum);


        return ResponseHandler.handle(200,"Otp has been resent ", null);
    }

    @Override
    public ResponseEntity verifyMobileNum(
            MobileVerificationRequest verificationRequest){
        String mobile = verificationRequest.getMobile();
        String otp = verificationRequest.getOtp();
        UserEntity user = userRepository.findByPhoneNo(mobile);
        if(user == null){
            return ResponseHandler.handle(401,"User with mobile number " + mobile + " not found", null);
        }

        try{
                boolean isValid =  tokenService.validateToken(mobile, otp);
            if(!isValid){
                return ResponseHandler.handle(401,"Otp has expired", null);
            }
        }catch (NullPointerException e){

            return ResponseHandler.handle(401,"Invalid Otp", null);
        }
        catch (IllegalIdentifierException e){
            return ResponseHandler.handle(401,"Otp was not assign to user with mobile "+ mobile, null);
        }

            user.setStatus(Status.PROFILE_COMPLETED);
            userRepository.save(user);

        return ResponseHandler.handle(200,"Mobile number has been verified", null);
    }

    @Override
    public ResponseEntity logUserIn(AuthenticationRequest request){

        String emailOrPhoneNo = request.getEmailOrPhoneNo();
        String password = request.getPassword();
        UserEntity user;
        try{
            user = userRepository.findByEmailOrPhoneNo(emailOrPhoneNo, emailOrPhoneNo);
            if(user.getStatus() != Status.PROFILE_COMPLETED){
                return ResponseHandler.handle(403,"Registration not completed",null);
            }
            String userPassword = user.getPassword();

            Boolean isCorrectPassword = passwordEncoder.matches(password,userPassword);
               if(!isCorrectPassword){
                   return  ResponseHandler.handle(402,"Incorrect password", null);
               }
               final UserDetails userDetails = userDetailsService.loadUserByUsername(emailOrPhoneNo);
                    user.setLastLogin(LocalDateTime.now());
                    userRepository.save(user);
            Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(emailOrPhoneNo,request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (NullPointerException e){
             return  ResponseHandler.handle(403,"User not found", null);
        }
        final String jwt = jwtService.generateToken(user.getEmail());
        return  ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @Override
    public ResponseEntity sendResetPasswordToken(String email)
            throws MessagingException, UnsupportedEncodingException {

        UserEntity user = userRepository.findByEmail(email);
        if(user == null){
            return  ResponseHandler.handle(401,"User not found", null);
        }

            tokenService.sendOtpToEmail(email);
        return  ResponseHandler.handle(200,"Reset Password verification Mail has been sent to "+ email, null);
    }




    @Override
    public ResponseEntity resetPassword(
            PasswordResetRequest passwordRequest
    ) {
        String email = passwordRequest.getEmailOrPhoneNum();
        String otp = passwordRequest.getOtp();
        UserEntity user = userRepository.findByEmail(email);

        if(user == null){
            return ResponseHandler.handle(404,"User with email "+ email +" not found", null);
        }

        try{
            boolean valid = tokenService.validateToken(email, otp);
            if(!valid){
                return ResponseHandler.handle(401,"Token has expired", null);
            }
        }catch (IllegalIdentifierException e){
            return ResponseHandler.handle(401,"Otp was not assigned to user", null);
        }catch (NullPointerException e){
            return ResponseHandler.handle(401,"Otp was not recognized", null);
        }

        user.setPassword(new BCryptPasswordEncoder().encode(passwordRequest.getPassword()));

        userRepository.save(user);
        return ResponseHandler.handle(200,"Email verified", user);
    }

    @Override
    public ResponseEntity updatePassword(
            PasswordUpdateRequest updateRequest,
            HttpServletRequest servletRequest) {
        UserEntity user = userUtils.extractUser(servletRequest);
        String password = user.getPassword();
        String currentPassword = updateRequest .getCurrentPassword();
        String newPassword = updateRequest.getNewPassword();
        Boolean match = new BCryptPasswordEncoder().matches(currentPassword,password);
            if(!match){
                return ResponseHandler.handle(403,"Current password is invalid", currentPassword);
            }
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(user);
        return ResponseHandler.handle(200,"Password updated successfully", user);
    }

    @Override
    public ResponseEntity updateProfile(
            ProfileUpdateRequest updateRequest,
            HttpServletRequest servletRequest
    ){
        UserEntity user = userUtils.extractUser(servletRequest);
        String fullName = updateRequest.getFullName();
        String email = updateRequest.getEmail();
        String phoneNumber = updateRequest.getPhoneNo();

        if(fullName != null){
            if(fullName.length() > 2 ){
                user.setFullName(fullName);
            }
        }


        if(email != null) {
            if (email.length() > 5) {
                UserEntity existingUserWithEmail = userRepository.findByEmail(email);
                if(existingUserWithEmail != null ){
                    if(existingUserWithEmail == user){

                    }else{
                        return ResponseHandler.handle(400,"Email has been registered already", null);
                    }
                }else{
                    user.setEmail(email);
                }

            }
        }

        if(phoneNumber != null) {
            if (phoneNumber.length() >= 11) {
                UserEntity existingUserWithPhoneNumber = userRepository.findByPhoneNo(phoneNumber);
                if(existingUserWithPhoneNumber != null ){
                    if(existingUserWithPhoneNumber == user){

                    }else{
                        return ResponseHandler.handle(400,"Phone no. has been registered already", null);
                    }
                }else{
                    user.setPhoneNo(phoneNumber);
                }
            }
        }

         userRepository.save(user);

        return ResponseHandler.handle(200,"Profile updated successfully", user);
    }

    @Override
    public ResponseEntity getDetails(
            HttpServletRequest servletRequest){
        UserEntity user = userUtils.extractUser(servletRequest);
        Wallet wallet = walletRepository.findByUser(user);

        Account account = new Account();
          account.setEmail(user.getEmail());
          account.setFullName(user.getFullName());
          account.setImagePath(user.getImagePath());
          account.setPhoneNo(user.getPhoneNo());

          if(wallet != null ){
              account.setBalance(wallet.getBalance());
          }

        return ResponseHandler.handle(200,"Profile fetched successfully", account);
    }

    @Override
    public List<UserEntity> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users;
    }


}
