package application.util.emailservice;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    boolean sendRecoverEmail(String userEmail, String username, String password);

    boolean sendNewUserRegister(String email,String username, String password);
}
