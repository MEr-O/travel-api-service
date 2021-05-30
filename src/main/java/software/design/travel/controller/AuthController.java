package software.design.travel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import software.design.travel.model.Code;
import software.design.travel.payload.request.Login2FactorRequest;
import software.design.travel.payload.request.LoginRequest;
import software.design.travel.payload.request.SignupRequest;
import software.design.travel.payload.response.JwtResponse;
import software.design.travel.payload.response.MessageResponse;
import software.design.travel.model.Role;
import software.design.travel.model.User;
import software.design.travel.model.enumType.ERole;
import software.design.travel.repository.CodeRepository;
import software.design.travel.repository.PlaceBookRepository;
import software.design.travel.repository.RoleRepository;
import software.design.travel.repository.UserRepository;
import software.design.travel.security.jwt.JwtUtils;
import software.design.travel.security.services.UserDetailsImpl;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    CodeRepository codeRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    PlaceBookRepository placeBookRepository;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/send-mail")
    public ResponseEntity<?> validateEmail(@Valid @RequestBody LoginRequest loginRequest) {
        System.out.println(loginRequest.getUsername());
        System.out.println(loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        System.out.println(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        System.out.println(jwt);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        System.out.println(roles);
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        MessageResponse messageResponse = new MessageResponse("ส่งอีเมลไปแล้วจ่ะ");

        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findUserByUsername(loginRequest.getUsername());
            if(user != null) {
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");

                Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("waranyu.r@virtdigital.com", "QsPsFWYGgUE7vBX");
                    }
                });

                Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress("Let\'sTravel", false));
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
                msg.setSubject("Let\'s Travel");

                String numbers = "1234567890";
                Random random = new Random();
                char[] otp = new char[5];

                for(int i = 0; i< 5 ; i++) {
                    otp[i] = numbers.charAt(random.nextInt(numbers.length()));
                }

                Code code = new Code();
                code.setCode(String.valueOf(otp));
                code.setUsername(loginRequest.getUsername());
                codeRepository.save(code);

                msg.setText("รหัสยืนยันในการเข้าสู่ระบบคือ " + String.valueOf(otp));
                msg.setSentDate(new Date());

                Transport.send(msg);
                messageResponse.setMessage("ส่งรหัสยืนยันไปที่อีเมลเรียบร้อยแล้ว");
                messageResponse.setStatus(true);

                return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
            } else {
                messageResponse.setMessage("ผู้ใช้งานนี้ไม่มีอยู่ในระบบ");
                messageResponse.setStatus(false);

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageResponse.setMessage("ผู้ใช้งานนี้ไม่มีอยู่ในระบบ");
            messageResponse.setStatus(false);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageResponse);
        }
    }

    @PostMapping("/signin-2-factor")
    public ResponseEntity<?> authenticateUser2Factor(@Valid @RequestBody Login2FactorRequest login2FactorRequest) {
        System.out.println(login2FactorRequest.getUsername());
        System.out.println(login2FactorRequest.getPassword());

        try {
            Code code = codeRepository.findTopByUsernameOrderByIdDesc(login2FactorRequest.getUsername());

            if(code != null) {
                if(code.getCode().equalsIgnoreCase(login2FactorRequest.getCode())) {
                    Authentication authentication = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(login2FactorRequest.getUsername(), login2FactorRequest.getPassword()));

                    System.out.println(authentication);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    String jwt = jwtUtils.generateJwtToken(authentication);
                    System.out.println(jwt);

                    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                    List<String> roles = userDetails.getAuthorities().stream()
                            .map(item -> item.getAuthority())
                            .collect(Collectors.toList());

                    System.out.println(roles);
                    return ResponseEntity.ok(new JwtResponse(jwt,
                            userDetails.getId(),
                            userDetails.getUsername(),
                            userDetails.getEmail(),
                            roles));
                } else {
                    MessageResponse messageResponse = new MessageResponse("รหัสไม่ถูกต้อง");
                    messageResponse.setStatus(false);
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(messageResponse);
                }
            } else {
                MessageResponse messageResponse = new MessageResponse("รหัสไม่ถูกต้อง");
                messageResponse.setStatus(false);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(messageResponse);
            }

        } catch (Exception e) {
            e.printStackTrace();
            MessageResponse messageResponse = new MessageResponse("เกิดข้อผิดพลาดกรุณาติดต่อผู้ดูแลระบบ");
            messageResponse.setStatus(false);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(messageResponse);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "owner":
                        Role ownerRole = roleRepository.findByName(ERole.ROLE_OWNER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(ownerRole);

                        break;
                    case "user":
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);

                        break;
                    case "creator":
                        Role creatorRole = roleRepository.findByName(ERole.ROLE_CREATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(creatorRole);

                        break;
                    default:
                        userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}