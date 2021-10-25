package hu.me.iit.malus.thesis.filemanagement.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Defines a File's Description
 *
 * @author Ilku Krisztian
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FileDescriptor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    @Column(length = 2000)
    private String name;
    @Column(length = 5000)
    private String downloadLink;
    private long size;
    private Date uploadDate;
    private String uploadedBy;
    private String contentType;
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;
    private Long tagId;
    private boolean removed;

    public void remove() {
        this.removed = true;
    }
}
