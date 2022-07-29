package ru.kata.spring.boot_security.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;

@Transactional
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u from User u join fetch u.roles where u.username = :username")
    User findByUsername(@Param("username")String username);

    @Modifying
    @Query("update User u set u.name = ?1, u.surname = ?2, u.age =?3, u.email =?4 where u.userId = ?5")
    void update(String name, String lastname, Byte age, String email, Long userId);
}