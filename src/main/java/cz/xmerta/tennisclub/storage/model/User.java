package cz.xmerta.tennisclub.storage.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @NotBlank(message = "Phone number cannot be blank.")
    @Pattern(
            regexp = "\\+\\d{12}",
            message = "Phone number must be in the format +XXXXXXXXXXXX."
    )
    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @NotBlank(message = "Name cannot be blank.")
    @Size(min = 3, max = 255, message = "User name must be between 3 and 255 characters.")
    @Column(nullable = false)
    private String name;

    /**
     *  For Hibernate purposes.
     */
    protected User() {}
    /**
     * Argument constructor for testing and other.
     */
    public User(Long id, String phoneNumber, String name) {
        this.setId(id);
        this.phoneNumber = phoneNumber;
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
