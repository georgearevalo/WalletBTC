package wallet.repository;

import wallet.entity.Firsttb;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FirsttbRepository extends JpaRepository<Firsttb, Integer> {
    
  public Optional<Firsttb> findFirsttbByCodigo(Integer codigo);
    
}
