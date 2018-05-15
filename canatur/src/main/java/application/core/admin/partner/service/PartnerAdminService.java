package application.core.admin.partner.service;

import application.model.Partner;

import java.util.List;

public interface PartnerAdminService {

    Partner findById(int id);
    String create(Partner entity);
    boolean update(Partner entity);
    void remove(Partner entity);

    List<Partner> getPartners();
    List<Partner>getPartnersByIdAndStatus(Integer id, Boolean status);

    List getPartnersWithoutDoneReservations(String initDay, String lastDay);

    int checkUniqueMail(Partner partner);

    boolean checkUniqueName(Partner partner);
}
