package com.drestive.chatalot.service.profile;

import com.drestive.chatalot.config.OrikaBeanMapper;
import com.drestive.chatalot.domain.identity.User;
import com.drestive.chatalot.domain.profile.Invite;
import com.drestive.chatalot.domain.profile.UserProfile;
import com.drestive.chatalot.domain.profile.repository.InviteRepository;
import com.drestive.chatalot.domain.profile.repository.UserProfileRepository;
import com.drestive.chatalot.service.profile.dto.UserProfileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by mustafa on 03/05/2016.
 */
@Service
public class ProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private InviteRepository inviteRepository;

    @Autowired
    protected OrikaBeanMapper mapper;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    protected FriendService friendService;

    public static final String[] ALLOWED_PICTURE_TYPES = {"image/jpeg", "image/png", "image/gif"};

    @Transactional(readOnly = true)
    public List<UserProfileDTO> getNonFriendsAndNonInvitedProfiles(User user) {
        UserProfile profile = userProfileRepository.getByUsername(user.getUsername());

        List<Integer> friendIds = profile.getFriends().stream().map(friend -> friend.getId())
                .collect(Collectors.toList());

        List<Integer> invitedIds = profile.getInvites().stream().map(invite -> invite.getTo().getId())
                .collect(Collectors.toList());

        List<UserProfile> profiles = userProfileRepository.getAll();
        List<UserProfile> nonFriendsAndNonInvitedProfiles = profiles.
                stream().filter(p -> !(friendIds.contains(p.getId()) ||
                invitedIds.contains(p.getId()) || p.getId() == profile.getId())).collect(Collectors.toList());

        List<UserProfileDTO> invitableProfiles = new ArrayList<>();
        nonFriendsAndNonInvitedProfiles.stream()
                .forEach(invitableProfile -> invitableProfiles.add(mapper.map(invitableProfile, UserProfileDTO.class)));
        return invitableProfiles;
    }

    @Transactional(readOnly = true)
    public List<UserProfileDTO> getFriends(User user) {
        List<UserProfileDTO> friends = new ArrayList<>();
        user.getUserProfile().getFriends().stream()
                .forEach(friendProfile -> friends.add(mapper.map(friendProfile, UserProfileDTO.class)));
        return friends;
    }

    @Transactional(readOnly = true)
    public UserProfileDTO getProfile(String username) {
        UserProfile profile = userProfileRepository.getByUsername(username);
        UserProfileDTO profileDTO = mapper.map(profile, UserProfileDTO.class);
        profileDTO.setName(profile.getName());
        return profileDTO;
    }

    @Transactional
    public UserProfileDTO updateProfile(String username, UserProfileDTO profileDTO) {
        UserProfile profile = userProfileRepository.getByUsername(username);
        profile.setName(profileDTO.getName());
        profile.getUser().setEmailAddress(profileDTO.getEmailAddress());
        entityManager.persist(profile);
        return profileDTO;
    }

    /**
     * Current user invites another.
     *
     * @param user
     * @param targetUserProfileId
     * @return
     */
    @Transactional
    public UserProfileDTO inviteUser(User user, Integer targetUserProfileId) {
        UserProfile profile = user.getUserProfile();
        UserProfile targetUserProfile = userProfileRepository.getById(targetUserProfileId);

        // Check if users are already friends
        if (profile.getFriends().stream().anyMatch(friend -> friend.getId() == targetUserProfile.getId())) {
            throw new IllegalArgumentException("User is already a friend.");
        }

        // Check if users are already invited
        if (profile.getInvites().stream().anyMatch(invite -> invite.getTo().getId() == targetUserProfile.getId())) {
            throw new IllegalArgumentException("User has already been invited.");
        }

        //otherwise create an invite
        Invite invite = new Invite(profile, targetUserProfile, new Date());
        entityManager.persist(invite);
        return mapper.map(profile, UserProfileDTO.class);
    }

    /**
     * Accept user invitation. Creates a bidirectional link between two user
     * profiles.
     *
     * @param user
     * @param inviteId
     * @return
     */
    @Transactional
    public UserProfileDTO acceptInvite(User user, Integer inviteId) {
        UserProfile profile = user.getUserProfile();
        Invite invite = inviteRepository.getById(inviteId);

        // Check if invite exists
        if (invite == null) {
            throw new IllegalArgumentException("Invalid invitation id.");
        }

        // Check if invite has already been accepted
        if (invite.getAccepted()) {
            throw new IllegalStateException("Invalid invite state.");
        }

        // Check if users are already friends
        if (invite.getFrom().getFriends().stream().anyMatch(friend -> friend.getId() == invite.getTo().getId())) {
            throw new IllegalArgumentException("User is already a friend.");
        }

        //otherwise make users friends
        invite.setAccepted(true);
        invite.getFrom().addFriend(invite.getTo());
        invite.getTo().addFriend(invite.getFrom());
        entityManager.persist(invite);

        friendService.broadCastFriendEvent(profile);

        return mapper.map(profile, UserProfileDTO.class);
    }

    @Transactional
    public UserProfileDTO setProfilePicture(String username, byte[] picture, String contentType) {
        // Check if users are already friends
        if (picture.length > 102400) {
            throw new IllegalArgumentException("Picture size too big.");
        }

        if (!Arrays.asList(ALLOWED_PICTURE_TYPES).contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("File type not allowed.");
        }

        String pictureBase64 = Base64.getEncoder().encodeToString(picture);
        UserProfile profile = userProfileRepository.getByUsername(username);
        profile.setProfilePicture(pictureBase64);
        profile.setProfilePictureType(contentType.toLowerCase());
        entityManager.persist(profile);
        UserProfileDTO profileDTO = mapper.map(profile, UserProfileDTO.class);
        profileDTO.setName(profile.getName());
        return profileDTO;
    }
}
