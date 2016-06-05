package com.drestive.chatalot.service.identity;

import com.drestive.chatalot.domain.identity.Role;
import com.drestive.chatalot.domain.identity.User;
import com.drestive.chatalot.service.identity.model.CachedAuthenticationDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Token cache service for ElasticSearch.
 */
@Service
public class TokenCacheService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisTemplate<String, CachedAuthenticationDetails> redisTemplate;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, CachedAuthenticationDetails> redisTokenCache;

/*    @Autowired
    private RedisTemplate<String, String> redisKeyTemplate;*/

    @Resource(name = "redisKeyTemplate")
    private ListOperations<String, String> redisKeyTemplateListOps;

    @Transactional
    public void saveToken(String token, CachedAuthenticationDetails cachedAuthenticationDetails)
            throws RuntimeException {
        try {
            User user = cachedAuthenticationDetails.getUser();
            User detachedPerson = new User();

            detachedPerson.setUsername(user.getUsername());
            detachedPerson.setPassword(user.getPassword());
            detachedPerson.setEnabled(user.getEnabled());
            detachedPerson.setExpired(user.getExpired());
            detachedPerson.setLocked(user.getLocked());

            List<Role> roles = new ArrayList<>();
            user.getRoles().stream()
                    .forEach(role -> roles.add(new Role(role.getId(), role.getName(), role.getDescription())));
            detachedPerson.setRoles(roles);

            cachedAuthenticationDetails.setUser(detachedPerson);
            redisTokenCache.set(token, cachedAuthenticationDetails);
            redisKeyTemplateListOps.leftPush(user.getUsername(), token);

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public CachedAuthenticationDetails getUserDetails(String token) throws RuntimeException {
        return redisTokenCache.get(token);
    }

    public void invalidateUserTokens(String username) {
        for (int i = 0; i < redisKeyTemplateListOps.size(username); i++) {
            String token = redisKeyTemplateListOps.leftPop(username);
            redisTemplate.delete(token);
        }
    }

    public void invalidateUserToken(String username, String token) {
        redisTemplate.delete(token);
        for (Long i = 0L; i < redisKeyTemplateListOps.size(username); i++) {
            String thisToken = redisKeyTemplateListOps.index(username, i);
            if(thisToken.equals(token)){
                redisKeyTemplateListOps.remove(username, i, thisToken);
            }
        }
    }
}