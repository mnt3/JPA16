package lt.akademija.jpaexam.ex01simple;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CarRepository  {

    @Autowired
private CarRepo carRepo;

    /**
     * Searches database for all cars and returns list of them
     * @return
     */

    public List<CarEntity> findAll() {

       return carRepo.findAll();
       // throw new UnsupportedOperationException();
    }

    /**
     * Given particular ID of a car searches database and retrieves car information.
     * If car is not present `null` is returned.
     */
    public CarEntity find(Long id) {

    	if(id==null) {throw new UnsupportedOperationException();}
    	else {
        return carRepo.findOne(id);
        //throw new UnsupportedOperationException();
    }}

    /**
     * Saves or updates car information. When car with existing ID is passed then update operation is performed.
     * When id is not present new car is saved to database
     */
    public CarEntity saveOrUpdate(CarEntity e) {
        if (carRepo.findOne(e.getId())!=null){
            CarEntity arYra = carRepo.findOne(e.getId());
            arYra.setId(e.getId());
            arYra.setManufactureDate(e.getManufactureDate());
            arYra.setModel(e.getModel());

            return  arYra;
        }
       else
        {
            return carRepo.save(e);
        }
        //throw new UnsupportedOperationException();
    }
}
