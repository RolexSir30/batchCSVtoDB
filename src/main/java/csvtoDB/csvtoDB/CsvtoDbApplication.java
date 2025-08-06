package csvtoDB.csvtoDB;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;

@SpringBootApplication
public class CsvtoDbApplication implements CommandLineRunner {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job hibernateJob;

    @Autowired
    private MeterRegistry meterRegistry;

    public static void main(String[] args) {
        SpringApplication.run(CsvtoDbApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        Timer.Sample sample = Timer.start(meterRegistry);
        try {
            JobExecution execution = jobLauncher.run(hibernateJob, params);
            if (execution.getStatus() == BatchStatus.FAILED) {
                meterRegistry.counter("batch.job.failure", "job", hibernateJob.getName()).increment();
            } else {
                meterRegistry.counter("batch.job.success", "job", hibernateJob.getName()).increment();
            }
        } finally {
            sample.stop(Timer.builder("batch.job.duration")
                    .description("Duration of Spring Batch job")
                    .tag("job", hibernateJob.getName())
                    .register(meterRegistry));
        }
    }
}
