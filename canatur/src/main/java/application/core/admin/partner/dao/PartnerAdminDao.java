package application.core.admin.partner.dao;

import application.model.Partner;

import java.util.List;

public interface PartnerAdminDao {
    Partner findById(int id);
    String create(Partner entity);
    boolean update(Partner entity);
    void remove(Partner entity);

    List<Partner> getPartners();
    List<Partner> getPartnersByIdAndStatus(Integer id, Boolean status);

    List getPartnersWithoutDoneReservations(String initDay, String lastDay);

    int countAccountsWithMail(int numId, String email);

    boolean checkUniqueName(Partner partner);
}
