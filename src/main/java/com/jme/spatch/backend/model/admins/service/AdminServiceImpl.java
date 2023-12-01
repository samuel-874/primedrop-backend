package com.jme.spatch.backend.model.admins.service;

import com.jme.spatch.backend.config.jwt.JwtService;
import com.jme.spatch.backend.general.dto.*;
import com.jme.spatch.backend.general.responsehandler.ResponseHandler;
import com.jme.spatch.backend.model.Transaction.entity.Transaction;
import com.jme.spatch.backend.model.Transaction.service.TransactionService;
import com.jme.spatch.backend.model.admins.entity.AdminEntity;
import com.jme.spatch.backend.model.admins.entity.AdminRegistration;
import com.jme.spatch.backend.model.order.entity.Order;
import com.jme.spatch.backend.model.order.entity.OrderDto;
import com.jme.spatch.backend.model.order.entity.OrderStatus;
import com.jme.spatch.backend.model.order.service.OrderService;
import com.jme.spatch.backend.model.rider.entity.ActiveStatus;
import com.jme.spatch.backend.model.rider.entity.RiderEntity;
import com.jme.spatch.backend.model.rider.service.RiderRepository;
import com.jme.spatch.backend.model.rider.service.RiderService;
import com.jme.spatch.backend.model.token.service.TokenService;
import com.jme.spatch.backend.model.user.entity.Role;
import com.jme.spatch.backend.model.user.entity.UserEntity;
import com.jme.spatch.backend.model.user.service.*;
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

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.jme.spatch.backend.general.mapper.DtoMapper.*;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final UserUtils userUtils;
    private final JwtService jwtService;
    private final UserService userService;
    private final RiderService riderService;
    private final OrderService orderService;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RiderRepository riderRepository;
    private final AdminRepository adminRepository;
    private final TransactionService transactionService;
    private final CustomUserDetailsService detailsService;
    private final AuthenticationManager authenticationManager;


    @Override
    public ResponseEntity registerAdmin(AdminRegistration registration, HttpServletRequest servletRequest) {
        AdminEntity admin = mapAdmin(registration);

            AdminEntity existingAdminWithEmail = adminRepository.findByEmail(admin.getEmail());
            if(existingAdminWithEmail != null){
                return ResponseHandler.handle(403,"Email has been taken", admin.getEmail());
            }

            admin.setPassword(new BCryptPasswordEncoder().encode(registration.getPassword()));
            adminRepository.save(admin);
        return ResponseHandler.handle(201,"Admin Registration was successfully",mapAdminDto(admin));
    }

    @Override
    public ResponseEntity registerSuperAdmin(AdminRegistration registration) {
        AdminEntity admin = mapAdmin(registration);

        AdminEntity existingAdminWithEmail = adminRepository.findByEmail(admin.getEmail());
            if(existingAdminWithEmail != null){
                return ResponseHandler.handle(403,"Email has been taken", admin.getEmail());
            }
            admin.setRole(Role.ROLE_SUPER_ADMIN);
            admin.setPassword(new BCryptPasswordEncoder().encode(registration.getPassword()));
            adminRepository.save(admin);
        return ResponseHandler.handle(201,"Super Admin Registration was successfully",mapAdminDto(admin));
    }


    @Override
    public ResponseEntity logAdminIn(AuthenticationRequest authRequest) {
        String email = authRequest.getEmailOrPhoneNo();
        String password = authRequest.getPassword();

        try{
            AdminEntity admin = adminRepository.findByEmail(email);

                String adminPassword = admin.getPassword();
                Boolean isCorrectPassword = passwordEncoder.matches(password,adminPassword);

                if(!isCorrectPassword){
                    return  ResponseHandler.handle(403,"Password isn't correct!", null);
                }

            final UserDetails userDetails = detailsService.loadUserByUsername(email);
            admin.setLastLogin(LocalDateTime.now());
            admin.setActiveStatus(ActiveStatus.ACTIVE);
            adminRepository.save(admin);

            Authentication authentication =  authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email,password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (NullPointerException e){
            return  ResponseHandler.handle(403,"Admin not found", null);
        }
        final String jwt = jwtService.generateToken(email);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @Override
    public ResponseEntity getDetails(HttpServletRequest servletRequest){
        AdminEntity admin = userUtils.extractAdmin(servletRequest);
        if(admin == null){
            return ResponseHandler.handle(404,"Admin not found",null);
        }

        AdminDetails details = new AdminDetails();
        details.setEmail(admin.getEmail());
        details.setFullName(admin.getFullName());
        details.setRole(admin.getRole());

        return ResponseHandler.handle(200,"Details Retrieved successfully",details);
    }

    @Override
    public ResponseEntity sendResetPasswordOtp(String email) {
        AdminEntity admin = adminRepository.findByEmail(email);
        if(admin == null){
             return ResponseHandler.handle(404,"Admin not found",null);
        }

        try{
               tokenService.sendOtpToEmail(email);

        }catch (Exception e){
            return ResponseHandler.handle(403,"An Error occurred",null);
        }

        return ResponseHandler.handle(200,"Otp has been Sent to Email :" +email,null);
    }

    @Override
    public ResponseEntity resetPassword(PasswordResetRequest request) {
        String email = request.getEmailOrPhoneNum();
        AdminEntity admin = adminRepository.findByEmail(email);
        if(admin == null){
            return ResponseHandler.handle(403,"Admin not found",null);

        }
        try {
            Boolean isValid = tokenService.validateToken(email, request.getOtp());
            if(!isValid){
                return ResponseHandler.handle(403,"Otp has expired",null);
            }else {
                admin.setPassword( new BCryptPasswordEncoder().encode(request.getPassword()));
                adminRepository.save(admin);
                return ResponseHandler.handle(200,"Password Reset was successful",mapAdminDto(admin));

            }
        }catch (NullPointerException e){
            return ResponseHandler.handle(403,"Token was not recognized",null);

        }catch (IllegalIdentifierException e){
            return ResponseHandler.handle(403,"Otp was not assigned to user",null);

        }

    }

    @Override
    public ResponseEntity getAppData(HttpServletRequest servletRequest){

        AppData appData = new AppData();
        List<Order> orders = orderService.getAllOrder();
        List<UserEntity> users = userService.getAllUsers();
        List<RiderEntity> riders = riderService.getAllActiveRiders();

        int allTimesJob =0;
        int todayJob=0;
        int todayRevenue=0;
        int allTimeRevenue=0;
        int allPendingJobs=0;
        int allRiders=0;
        int allUsers=0;


        for(Order order: orders){
            Date theOrderDate = new Date(order.getOrderDate());
            Date todayDate = new Date();
            Calendar orderCalender = Calendar.getInstance();
            Calendar todayCalender = Calendar.getInstance();

            orderCalender.setTime(theOrderDate);
            todayCalender.setTime(todayDate);

            Boolean isToday = orderCalender.get(Calendar.YEAR) == todayCalender.get(Calendar.YEAR) &&
                              orderCalender.get(Calendar.MONTH) == todayCalender.get(Calendar.MONTH) &&
                              orderCalender.get(Calendar.DAY_OF_MONTH) == todayCalender.get(Calendar.DAY_OF_MONTH);

            if(order.getOrderStatus() != OrderStatus.CANCELLED){
                allTimesJob++;
                if(order.getOrderStatus() == OrderStatus.DONE){
                    allTimeRevenue = allTimeRevenue + (order.getCharge() * 20/100);
                }
                if(isToday && order.getOrderStatus() == OrderStatus.DONE ){
                    todayJob++;
                    todayRevenue = todayRevenue + (order.getCharge() * 20/100);
                }

                if(order.getOrderStatus() == OrderStatus.PENDING){
                    allPendingJobs++;
                }

            }
        }

        for(UserEntity user : users){
            if(user.getRole() == Role.ROLE_USER){
                allUsers++;
            }
        }

        allRiders = riders.size();

        appData.setTodayRevenue(todayRevenue);
        appData.setTotalJobs(todayJob);
        appData.setAllTimeRevenue(allTimeRevenue);
        appData.setAllTimeJobs(allTimesJob);
        appData.setPendingJobs(allPendingJobs);
        appData.setAllUser(allUsers);
        appData.setAvailableRiders(allRiders);

        return ResponseHandler.handle(200,"App data retrieved successfully",appData);
    }



    @Override
    public ResponseEntity getAllUsers() {
        List<UserEntity> userList = userRepository.findAll();


        List<UserDto> userDtoS = userList.stream().filter(user -> user.getRole() == Role.ROLE_USER).map(user -> mapUserDto(user)).collect(Collectors.toList());
        return ResponseHandler.handle(200,"All users Fetched successfully",userDtoS);
    }




    @Override
    public ResponseEntity getAllRiders() {
        return null;
    }

    @Override
    public ResponseEntity getAllAdmins() {
        List<AdminEntity> adminList = adminRepository.findAll();
        List<AdminDto> adminDtoList = adminList.stream().map(user -> mapAdminDto(user)).collect(Collectors.toList());
        return ResponseHandler.handle(200,"All users Fetched successfully",adminDtoList);
    }

    @Override
    public ResponseEntity getUserDetails(long id) {
        UserEntity user = userRepository.findById(id).get();
        List<OrderDto> orders = orderService.getAllOrder(user);
        List<Transaction> transactionList = transactionService.getTransactions(user);
        com.jme.spatch.backend.general.dto.UserDetails userDetails = mapUserDetails(user);
            userDetails.setOrderDtoList(orders);
            userDetails.setCompletedOrders((int) orders.stream().filter( orderDto -> orderDto.getOrderStatus() == OrderStatus.DONE).count());
            userDetails.setCancelledOrders((int) orders.stream().filter( orderDto -> orderDto.getOrderStatus() == OrderStatus.CANCELLED).count());
            userDetails.setTransactions(transactionList);

        return ResponseHandler.handle(200,"User details retrieved successfully",userDetails);
    }

    @Override
    public ResponseEntity updateAdmin(AdminUpdateRequest updateRequest) {
        if(updateRequest.getId() == 0){
            return ResponseHandler.handle(404,"Id required",null);
        }
        Optional<AdminEntity> optionalAdmin = adminRepository.findById(updateRequest.getId());
        if(optionalAdmin.isEmpty()){
            return ResponseHandler.handle(404,"Admin not found",null);
        }
        AdminEntity admin = optionalAdmin.get();
        if(updateRequest.getActiveStatus() != null){
            admin.setActiveStatus(updateRequest.getActiveStatus());
        }
        if(updateRequest.getRole() != null){
            admin.setRole(updateRequest.getRole());
        }
        adminRepository.save(admin);
        return ResponseHandler.handle(200,"Admin details updated successfully",mapAdminDto(admin));
    }
}
