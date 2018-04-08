package com.project.backend.service;

import com.project.backend.persistence.domain.backend.Company;
import com.project.backend.persistence.repositories.CompanyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zen on 02/07/17.
 */
@Service
@Transactional(readOnly = true)
public class CompanyService {

    /** The application logger */
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private CompanyRepository companyRepository;

    @Transactional
    public void updateCompanyProfile(long companyId,
                                  String companyName,
                                  String businessType,
                                  String street,
                                  String zipCode,
                                  String country,
                                  String town,
                                  String webSite,
                                  String siren,
                                  String vatNumber,
                                  String tvaNumber,
                                  String phoneNumberCompany,
                                  String emailCompany,
                                  String aboutYourBusiness,
                                  String linkedinUrl,
                                  String contactPersonEmail,
                                  String phoneNumberContact){
        companyRepository.updateCompanyProfile(companyId,companyName,businessType,street,zipCode,
                country,town,webSite,siren,vatNumber,tvaNumber,phoneNumberCompany,emailCompany,
                aboutYourBusiness,linkedinUrl,contactPersonEmail,phoneNumberContact);
        LOG.debug("company profil updated successfully for user id {} ", companyId);
    }

    public Company findByCompanyName(String companyName) {
        return companyRepository.findByCompanyName(companyName);
    }
}
