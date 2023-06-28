
package pe.mybusiness.ApiAuth.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.mybusiness.ApiAuth.dto.LoginDto;
import pe.mybusiness.ApiAuth.dto.SignUpDto;
import pe.mybusiness.ApiAuth.entity.User;
import pe.mybusiness.ApiAuth.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    
    @Autowired
    private AuthenticationManager authenticarionManager;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping
    public ResponseEntity<String> Greetings(){
        return new ResponseEntity<>("Hello world",HttpStatus.OK) ;
    }
    
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto user){
        if (userService.existsByUsername(user.getUsername())){
            return new ResponseEntity<>("Username is already taken",HttpStatus.BAD_REQUEST);
        }
        
         if (userService.existsByEmail(user.getEmail())){
            return new ResponseEntity<>("Email is already taken",HttpStatus.BAD_REQUEST);
        }
         
        User userEntity = new User();
        userEntity.setName(user.getName());
        userEntity.setUserName(user.getUsername());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        
        userEntity.setRoles(user.getRoles());
        
        /*Role roles = roleService.findByName("ROLE-ADMIN");
        userEntity.setRoles(Collections.singleton(roles));*/
        
        var result = userService.add(userEntity);
        
        return new ResponseEntity<>(result,HttpStatus.CREATED);
    }
    
    @PostMapping("/signin")
    public ResponseEntity<?> authUser(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticarionManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(),
        loginDto.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>("User signin successfully", HttpStatus.OK);
        
    }
    
}
