package application.core.security;

import application.core.admin.role.dao.RoleDao;
import application.core.admin.worker.service.WorkerService;
import application.model.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class WorkerDetailsServiceApp implements UserDetailsService {
    @Autowired
    private WorkerService workerService;
    @Autowired
    private RoleDao roleDao;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        try{
            Worker worker = workerService.findUser(identifier);
            GrantedAuthority authority = new SimpleGrantedAuthority(roleDao.findByID(String.valueOf(worker.getRoleId())).getRole());
            return (UserDetails) new User(String.valueOf(worker.getUserId()+","+worker.getName()), worker.getPassword(), Arrays.asList(authority));
        }catch (Exception e){
            throw new UsernameNotFoundException("User not found");
        }
    }
}