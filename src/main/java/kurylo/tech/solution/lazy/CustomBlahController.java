package kurylo.tech.solution.lazy;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("users")
public class CustomBlahController {
    private final UserRepo userRepo;

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user){
        return ResponseEntity.status(HttpStatus.OK).body(userRepo.save(user));
    }

    @GetMapping
    public ResponseEntity<List<User>> listUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(userRepo.findAll());
    }
}
