package csvtoDB.csvtoDB.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class JarExecution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jar;
    private String startTime;  // temporairement en String (pour test simple)
    private String endTime;    // sinon tu peux ajouter un converter LocalDateTime
    private Long durationMs;
    private Integer memBeforeMB;
    private Integer memAfterMB;
    private Integer memUsedMB;

    // getters et setters ...
}
