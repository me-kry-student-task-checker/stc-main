package hu.me.iit.malus.thesis.filemanagement.model;

import com.google.common.base.Objects;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder()
@ToString
public class FileDescriptor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    Long id;

    @Column(length = 2000)
    String name;

    @Column(length = 5000)
    String downloadLink;

    long size;

    Date uploadDate;

    String uploadedBy;

    String contentType;

    @Enumerated(EnumType.STRING)
    ServiceType serviceType;

    Long tagId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileDescriptor that = (FileDescriptor) o;
        return Objects.equal(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
