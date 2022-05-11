package net.bitkap.marketplace.repository;

import net.bitkap.marketplace.model.entity.AppParameters;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppParametersRepository extends JpaRepository<AppParameters, Long> {
}
