package application.core.admin.partner.service.impl;


import application.core.admin.partner.dao.PartnerAdminDao;
import application.core.admin.partner.service.PartnerAdminService;
import application.model.Partner;
import application.util.emailservice.impl.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class PartnerAdminServiceImpl implements PartnerAdminService {

    @Autowired
    private PartnerAdminDao partnerDao;

    @Autowired
    private EmailServiceImpl emailServiceImpl;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private StringKeyGenerator keyGenerator;


    @Override
    public Partner findById(int id) {
        return partnerDao.findById(id);
    }

    @Override
    public String create(Partner entity) {
        String temporalLoginPassword = keyGenerator.generateKey();
        entity.setPassword(passwordEncoder.encode(temporalLoginPassword));
        try {
            partnerDao.create(entity);
            if(entity.getPartnerType() == 1){// 1 reference an affiliate of the company, 0 to no affiliate
                emailServiceImpl.sendNewUserRegister(entity.getEmail(), String.valueOf(entity.getNumId()), temporalLoginPassword);
            }
            return "success";
        } catch (Exception e) {
            return "error";
        }
    }

    @Override
    public boolean update(Partner entity) {
        return partnerDao.update(entity);
    }

    @Override
    public void remove(Partner entity) {
        partnerDao.remove(entity);
    }

    @Override
    public List<Partner> getPartners() {
        return partnerDao.getPartners();
    }

    @Override
    public List<Partner> getPartnersByIdAndStatus(Integer id, Boolean status) {
        return partnerDao.getPartnersByIdAndStatus(id, status);
    }

    @Override
    public List getPartnersWithoutDoneReservations(String initDay, String lastDay) {
        return partnerDao.getPartnersWithoutDoneReservations(initDay,lastDay);
    }

    @Override
    public int checkUniqueMail(Partner partner) {
        return partnerDao.countAccountsWithMail(partner.getNumId(), partner.getEmail());
    }

    @Override
    public boolean checkUniqueName(Partner partner) { return partnerDao.checkUniqueName(partner); }
}
