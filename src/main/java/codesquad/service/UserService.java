package codesquad.service;

import codesquad.UnAuthenticationException;
import codesquad.UnAuthorizedException;
import codesquad.domain.User;
import codesquad.domain.UserRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.naming.AuthenticationException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service("userService")
public class UserService {
    private static final Logger log = getLogger(UserService.class);
    @Resource(name = "userRepository")
    private UserRepository userRepository;


    public User add(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User update(User loginUser, long id, User updatedUser) {
        User original = findById(loginUser, id);
        original.update(loginUser, updatedUser);
        return original;
    }

    public User findById(User loginUser, long id) {
        return userRepository.findById(id)
                .filter(user -> user.equals(loginUser))
                .orElseThrow(UnAuthorizedException::new);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User login(String userId, String password) throws UnAuthenticationException {
        // TODO 로그인 기능 구현;
        return userRepository.findByUserId(userId)
                .filter(user->user.matchPassword(password))
                .orElseThrow(UnAuthenticationException::new);
    }
}
