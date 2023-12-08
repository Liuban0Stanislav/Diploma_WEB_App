package ru.skypro.homework.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Collection;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity extends ModelEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;

    @OneToOne
    @JsonBackReference
    private PhotoEntity photo;

    @OneToMany(mappedBy = "author")
    private Collection<AdEntity> ads;

    @OneToMany(mappedBy = "author")
    private Collection<CommentEntity> comments;

    /**
     * поле куда записывается URL для доступа к методу возврата аватарки
     */
    private String image; //URL для контроллера
    private String filePath; //путь на ПК

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", role=" + role +
                ", photo=" + photo +
                ", ads=" + ads +
                ", comments=" + comments +
                ", image='" + image + '\'' +
                '}';
    }
}
