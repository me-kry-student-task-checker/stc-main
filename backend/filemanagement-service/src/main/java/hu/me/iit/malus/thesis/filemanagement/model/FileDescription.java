package hu.me.iit.malus.thesis.filemanagement.model;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Defines a File's Description
 * @author Ilku Krisztian
 */
@Entity
@Getter @Setter
@NoArgsConstructor @ToString
public class FileDescription {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO, generator="native")
    @GenericGenerator(name="native", strategy="native")
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
    @ElementCollection
    @OrderColumn(name = "index_no")
    @CollectionTable(name = "services",
            joinColumns = @JoinColumn(name = "file_description_id"))
    Set<Service> services;
    Long tagId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileDescription that = (FileDescription) o;
        return Objects.equal(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
