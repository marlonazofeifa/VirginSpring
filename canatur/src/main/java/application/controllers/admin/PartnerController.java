package application.controllers.admin;

import application.core.admin.partner.service.PartnerAdminService;
import application.model.Partner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class PartnerController {

    @Autowired
    private PartnerAdminService partnerAdminService;

    @RequestMapping(value = "admin/afiliados")
    public String listPartners(@RequestParam(value = "status", required = false) String partnerStatus,
                               @RequestParam(value = "id", required = false) String partnerId,
                               Model model) {
        List<Partner> partners;
        Boolean partnerState = null;
        if (partnerStatus != null) {
            switch (partnerStatus) {
                case "Activos":
                    partnerState = true;
                    break;
                case "Inactivos":
                    partnerState = false;
                    break;
            }
        }

        Integer partnerIdNum = null;
        if (partnerId != null && partnerId != "")
            partnerIdNum = Integer.parseInt(partnerId.substring(0, partnerId.indexOf(":")));

        partners = partnerAdminService.getPartnersByIdAndStatus(partnerIdNum, partnerState);
        model.addAttribute("partners", partners);
        model.addAttribute("partnerscount", partners.size());
        model.addAttribute("partnerSelected", partnerId);
        return "admin/partners";
    }

    @RequestMapping(value = "/admin/partner/cambiar-estado")
    @ResponseBody
    public boolean getFlightSpringState(@RequestParam("numId") Integer numId,
                                        @RequestParam("state") boolean state) {
        String role = "BOSS";
        boolean check = false;
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return false;
        }
        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            return false;
        }
        for (GrantedAuthority auth : authentication.getAuthorities()) {
            if (role.equals(auth.getAuthority())) {
                check = true;
                break;
            }
        }
        if (!check) {
            return false;
        }
        Partner partner = partnerAdminService.findById(numId);
        if (partner != null) {
            partner.setState(state);
            return partnerAdminService.update(partner);
        } else {
            return false;
        }
    }

    @RequestMapping(value = "/admin/editar-afiliado")
    public String initWorkerView(@RequestParam(value = "id") String id,
                                 RedirectAttributes attributes,
                                 Model model) {
        Integer identifier = validPartner(id);
        if (identifier == null) {
            attributes.addAttribute("postMessage", "Número de afiliado inválido");
            attributes.addAttribute("postMessageType", "error");
            return "redirect:/admin/afiliados";
        }
        Partner partner = partnerAdminService.findById(identifier);
        model.addAttribute("currentUsername", id);
        model.addAttribute("currentPartner", partner);
        return "admin/editpartnerprofile";
    }

    @RequestMapping(value = {"/admin/editar-afiliado"}, method = RequestMethod.POST)
    public String editProfilePost(Partner partner,
                                  BindingResult bindingResult,
                                  @RequestParam(value = "currentUsername", required = true) String identifier,
                                  RedirectAttributes attributes) {

        Integer id = validPartner(identifier);
        if (id == null) {
            attributes.addAttribute("postMessage", "Número de afiliado inválido, porfavor, inténtelo de nuevo");
            attributes.addAttribute("postMessageType", "error");
            return "redirect:/admin/afiliados";
        }
        Partner currentPartner = partnerAdminService.findById(id);
        partner.setNumId(id);

        if (currentPartner == null) {
            attributes.addAttribute("postMessage", "Número de afiliado inválido, porfavor, inténtelo de nuevo");
            attributes.addAttribute("postMessageType", "error");
            return "redirect:/admin/afiliados";
        }

        if (partner != null) {
            /*Check if some attribute is not valid*/
            if (partner.getName().isEmpty()) {
                return "redirect:/admin/editar-afiliado?id="+identifier+"&error=emptyName";
            } else if (partnerAdminService.checkUniqueName(partner) && !(partner.getName().equals(currentPartner.getName()))) {
                return "redirect:/admin/editar-afiliado?id="+identifier+"&error=duplicateName";
            } else if (partnerAdminService.checkUniqueMail(partner) > 0) {
                return "redirect:/admin/editar-afiliado?id="+identifier+"&error=duplicateEmail";
            } else if (partner.getEmail().isEmpty() && partner.getPartnerType() == 1) {
                return "redirect:/admin/editar-afiliado?id="+identifier+"&error=emptyEmail";
            } else if (partner.getPrincipalTelephone() == null || partner.getPrincipalTelephone() == 0) {
                return "redirect:/admin/editar-afiliado?id="+identifier+"&error=wrongNumber";
            } else if (partner.getEmergencyTelephone() == null || partner.getEmergencyTelephone() == 0) {
                return "redirect:/admin/editar-afiliado?id="+identifier+"&error=wrongNumber2";
            }

            /*If all of them are valid*/

            try {
                currentPartner.setEmail(partner.getEmail());
                currentPartner.setEmergencyTelephone(partner.getEmergencyTelephone());
                currentPartner.setPrincipalTelephone(partner.getPrincipalTelephone());
                currentPartner.setPartnerType(partner.getPartnerType());
                partnerAdminService.update(currentPartner);
                attributes.addAttribute("postMessage", "La información del afiliado se ha modificado correctamente");
                attributes.addAttribute("postMessageType", "success");
            } catch (Exception e) {
                attributes.addAttribute("postMessage", "Ha ocurrido un error, por favor, intentelo de nuevo");
                attributes.addAttribute("postMessageType", "error");
                return "redirect:/admin/editar-afiliado?id="+identifier;
            }
        }
        return "redirect:/admin/afiliados";
    }

    Integer validPartner(String identifier) {
        try {
            return Integer.parseInt(identifier);
        } catch (Exception e) {
            return null;
        }
    }
}
