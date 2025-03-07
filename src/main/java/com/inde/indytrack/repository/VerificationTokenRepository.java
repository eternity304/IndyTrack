package com.inde.indytrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.inde.indytrack.entity.VerificationToken;
import com.inde.indytrack.entity.User;
import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
    Optional<VerificationToken> findByUser(User user);

}
