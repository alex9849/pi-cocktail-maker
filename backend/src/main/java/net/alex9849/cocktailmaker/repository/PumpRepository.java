package net.alex9849.cocktailmaker.repository;

import net.alex9849.cocktailmaker.model.Pump;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PumpRepository extends JpaRepository<Pump, Long> {

    Optional<Pump> findByGpioPin(int gpoiPin);

    List<Pump> findAllByCurrentIngredient_IdIn(List<Long> ids);

}
