package kurylo.tech.solution.lazy;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@NamedEntityGraph(name = "user_entity_graph", attributeNodes = @NamedAttributeNode("addresses"))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(targetEntity = Address.class, mappedBy = "user", cascade = CascadeType.ALL)
    private List<Address> addresses;

}
