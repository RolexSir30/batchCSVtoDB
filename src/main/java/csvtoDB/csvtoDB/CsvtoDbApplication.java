package csvtoDB.csvtoDB;



import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CsvtoDbApplication implements CommandLineRunner {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job hibernateJob;

    public static void main(String[] args) {
        SpringApplication.run(CsvtoDbApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(hibernateJob, params);
    }
}
