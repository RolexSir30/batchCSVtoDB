package csvtoDB.csvtoDB.config;

import csvtoDB.csvtoDB.model.JarExecution;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableBatchProcessing
@EnableTransactionManagement
public class BatchConfig {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Bean
    public FlatFileItemReader<JarExecution> csvReader() {
        FlatFileItemReader<JarExecution> itemReader = new FlatFileItemReader<>();
        itemReader.setName("csvReader");
        // Assure-toi que le chemin du fichier CSV est correct
        itemReader.setResource(new FileSystemResource("src/main/resources/data.csv"));
        itemReader.setLinesToSkip(1); // Skip header

        DefaultLineMapper<JarExecution> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setNames("jar", "startTime", "endTime", "durationMs", "memBeforeMB", "memAfterMB", "memUsedMB");

        BeanWrapperFieldSetMapper<JarExecution> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(JarExecution.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        itemReader.setLineMapper(lineMapper);
        return itemReader;
    }

    @Bean
    public JpaItemWriter<JarExecution> itemWriter() {
        JpaItemWriter<JarExecution> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    public Step csvToPostgreSqlStep() {
        return new StepBuilder("csvToPostgreSqlStep", jobRepository)
                .<JarExecution, JarExecution>chunk(10, transactionManager)
                .reader(csvReader())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public Job hibernateJob() {
        return new JobBuilder("hibernateBatchJob", jobRepository)
                .flow(csvToPostgreSqlStep())
                .end()
                .build();
    }
}
