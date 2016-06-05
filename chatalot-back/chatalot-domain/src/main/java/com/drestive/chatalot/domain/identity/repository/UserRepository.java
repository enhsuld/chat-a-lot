package com.drestive.chatalot.domain.identity.repository;

/**
 * Created by mustafa on 02/01/2016.
 */

import com.drestive.chatalot.domain.common.base.AbstractRepository;
import com.drestive.chatalot.domain.identity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository extends AbstractRepository<User> {
    public void saveUser(User user) {
        save(user);
    }

    public List<User> findAllUsers() {
        return getAll();
    }

    public void deleteUserById(Integer id) {
        super.deleteById(id);
    }

    public User getByUsername(String username) {
        return getFirstBy("username", username);
    }

    public User getByEmailAddress(String emailAddress) {
        return getFirstBy("emailAddress", emailAddress);
    }

    public User getByAuthToken(String authToken) {
        return getFirstBy("authToken", authToken);
    }

    public void updateUser(User user) {
        update(user);
    }
}
