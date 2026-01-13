package hms.data;

import hms.model.Referral;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ReferralRepository {
    List<Referral> findAll() throws IOException;
    Optional<Referral> findById(String referralId) throws IOException;
    void saveAll(List<Referral> referrals) throws IOException;
}

