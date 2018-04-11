package lt.akademija.jpaexam.ex01simple;

import java.util.Date;

import lt.akademija.jpaexam.grader.Grader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class Ex01Test {

    @Autowired
    CarRepository repository;

    @Test
    public void crypticName1() {
        Grader.graded(1, () -> {
            Date aDate = new Date();
            CarEntity c = insertNew(aDate, "A model");

            assertThat(c.getId(), equalTo(1L));
            assertThat(c.getModel(), equalTo("A model"));
            assertThat(c.getManufactureDate(), equalTo(aDate));
        });
    }

    @Test
    public void crypticName2() {
        Grader.graded(2, () -> {
            Date aDate = new Date();
            CarEntity c = insertNew(aDate, "A model");

            assertThat(c.getId(), is(1L));
            assertThat(c.getModel(), is("A model"));
            assertThat(c.getManufactureDate(), is(aDate));

            CarEntity cUpdated = new CarEntity();
            cUpdated.setModel("A model updated");
            cUpdated.setManufactureDate(aDate);
            cUpdated.setId(c.getId());

            CarEntity updated = repository.saveOrUpdate(cUpdated);

            assertThat(updated.getModel(), is("A model updated"));
        });
    }

    @Test
    public void crypticName3() {
        Grader.graded(1, () -> {
            Date aDate = new Date();
            insertNew(aDate, "A model 1");
            insertNew(aDate, "A model 2");
            insertNew(aDate, "A model 3");

            long[] idx = {1};
            repository.findAll().forEach(c -> {
                long index = idx[0]++;
                assertThat(c.getId(), is(index));
                assertThat(c.getModel(), is("A model " + index));
            });
        });
    }

    @Test
    public void crypticName4() {
        Grader.graded(1, () -> {
            Date aDate = new Date();
            insertNew(aDate, "A model 1");
            insertNew(aDate, "A model 2");
            insertNew(aDate, "A model 3");

            CarEntity e = repository.find(1L);
            assertThat(e.getId(), is(1L));
            assertThat(e.getModel(), is("A model 1"));
        });
    }

    private CarEntity insertNew(Date aDate, String model) {
        CarEntity c = new CarEntity();
        c.setModel(model);
        c.setManufactureDate(aDate);
        return repository.saveOrUpdate(c);
    }
}