package lt.akademija.jpaexam.ex01simple;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepo extends JpaRepository<CarEntity,Long>  {

}
