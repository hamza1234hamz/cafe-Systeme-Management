package com.hamza.cafe.Repository;

import com.hamza.cafe.Wrapper.UserWrapper;
import com.hamza.cafe.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Query("select new com.hamza.cafe.Wrapper.UserWrapper(u.id,u.name,u.email,u.contactNumber,u.status) from User u where u.role='user'")
    List<UserWrapper> getAllUser();
    @Query("select u.email from User u where u.role='admin'")
    List<String> getAllAdmin();
    @Modifying
    @Transactional
    @Query("update User u SET u.status = :status WHERE u.id = :id")
    Integer updateStatus(@Param("status") String status,@Param("id") Integer id);

}
