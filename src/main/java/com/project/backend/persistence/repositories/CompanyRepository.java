package com.project.backend.persistence.repositories;

import com.project.backend.persistence.domain.backend.Company;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zen on 11/06/17.
 */
@Repository
@Transactional(readOnly = true)
public interface CompanyRepository extends CrudRepository<Company, Integer> {

   @Modifying
    @Query("update Company c set c.companyName = :companyName, c.businessType = :businessType, c.street = :street, " +
            "c.zipCode = :zipCode, c.country = :country, c.town = :town, c.webSite = :webSite," +
            "c.siren = :siren, c.vatNumber = :vatNumber, c.tvaNumber = :tvaNumber, " +
            "c.phoneNumberCompany = :phoneNumberCompany, c.emailCompany = :emailCompany, " +
            "c.aboutYourBusiness = :aboutYourBusiness, c.linkedinUrl = :linkedinUrl," +
            "c.contactPersonEmail = :contactPersonEmail, c.phoneNumberContact = :phoneNumberContact " +
            "where c.id = :companyId")
    void updateCompanyProfile(@Param("companyId") long companyId,
                              @Param("companyName") String companyName,
                              @Param("businessType") String businessType,
                              @Param("street") String street,
                              @Param("zipCode") String zipCode,
                              @Param("country") String country,
                              @Param("town") String town,
                              @Param("webSite") String webSite,
                              @Param("siren") String siren,
                              @Param("vatNumber") String vatNumber,
                              @Param("tvaNumber") String tvaNumber,
                              @Param("phoneNumberCompany") String phoneNumberCompany,
                              @Param("emailCompany") String emailCompany,
                              @Param("aboutYourBusiness") String aboutYourBusiness,
                              @Param("linkedinUrl") String linkedinUrl,
                              @Param("contactPersonEmail") String contactPersonEmail,
                              @Param("phoneNumberContact") String phoneNumberContact);

    Company findByCompanyName(String companyName);


}
