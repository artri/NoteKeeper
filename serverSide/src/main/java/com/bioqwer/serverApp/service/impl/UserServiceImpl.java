package com.bioqwer.serverApp.service.impl;

import com.bioqwer.serverApp.model.User;
import com.bioqwer.serverApp.repository.UserRepository;
import com.bioqwer.serverApp.service.MonitoringService;
import com.bioqwer.serverApp.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Provide implementation of {@link com.bioqwer.serverApp.service.UserService} logic.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Qualifier("monitoringServiceImpl")
    @Autowired
    private MonitoringService monitoringService;

    @Override
    public User addUser(User user) {
        user = userRepository.saveAndFlush(user);
        logger.debug("Success addUser " + user);
        monitoringService.addUserMonitoring(user);
        return user;
    }

    @Override
    public void delete(User deleteUser) {
        logger.debug("Prepare to delete " + deleteUser);
        userRepository.delete(deleteUser.getUserId());
        logger.debug("Sucess delete");
    }

    @Override
    public User getById(long userId) {
        return userRepository.findOne(userId);
    }

    @Override
    public User getByLogin(String login) {
        User user = userRepository.findByLogin(login);
        logger.debug("GetByLogin " + user);
        return user;
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User editUser(User user) {
        User before = userRepository.findOne(user.getUserId());
        if (user.equals(before)) {
            logger.debug("Don't need persist  editUser " + user);
            return user;
        } else {
            user = userRepository.saveAndFlush(user);
            logger.debug("Success editUser " + user);
            monitoringService.addUserMonitoring(user);
            return user;
        }
    }

    @Override
    public Collection<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Collection<User> searchByUserLogin(String partOfLogin) {
        Collection<User> result = userRepository.findWhereLogin("%" + partOfLogin + "%");
        logger.debug("Success searchByUserLogin Find by partOfLogin " + partOfLogin + " is " + result);
        return result;
    }
}
