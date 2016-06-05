package com.drestive.chatalot.rest.profile;

import com.drestive.chatalot.rest.common.AbstractController;
import com.drestive.chatalot.service.profile.ProfileService;
import com.drestive.chatalot.service.profile.dto.ProfileViews;
import com.drestive.chatalot.service.profile.dto.UserProfileDTO;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * Created by mustafa on 03/05/2016.
 */

/**
 * Controller that handles requests related to profile operations. Forwards them to the corresponding service
 * and returns the resulting objects to the client in JSON.
 */
@RestController
@RequestMapping("/profile")
public class ProfileController extends AbstractController {

    @Autowired
    private ProfileService profileService;

    /**
     * Get the current user's profile details.
     *
     * @return current user profile.
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @JsonView(ProfileViews.ProfileFull.class)
    public UserProfileDTO profile() {
        return profileService.getProfile(getCurrentUsername());
    }

    /**
     * Get a list of users that the current user can invite to be friends.
     *
     * @return a list of invitable users.
     */
    @RequestMapping(value = "/invitable", method = RequestMethod.GET)
    @ResponseBody
    @JsonView(ProfileViews.ProfileShort.class)
    public List<UserProfileDTO> invitable() {
        return profileService.getNonFriendsAndNonInvitedProfiles(getCurrentUser());
    }

    /**
     * As the current user, issue a friend invite to a user in the system.
     *
     * @param userId whose profile is to be returned
     * @return the current user's updated profile.
     */
    @RequestMapping(value = "/invite/{userId}", method = RequestMethod.POST)
    @ResponseBody
    @JsonView(ProfileViews.ProfileFull.class)
    public UserProfileDTO invite(@PathVariable("userId") Integer userId) {
        return profileService.inviteUser(getCurrentUser(), userId);
    }

    /**
     * As the current user, accept a pending friend invite.
     *
     * @param inviteId to be accepted
     * @return the current user's updated profile.
     */
    @RequestMapping(value = "/accept/{inviteId}", method = RequestMethod.POST)
    @ResponseBody
    @JsonView(ProfileViews.ProfileFull.class)
    public UserProfileDTO accept(@PathVariable("inviteId") Integer inviteId) {
        return profileService.acceptInvite(getCurrentUser(), inviteId);
    }

    /**
     * Update my profile.
     *
     * @param profileDTO to be updated
     * @return the updated profile.
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @JsonView(ProfileViews.ProfileFull.class)
    public UserProfileDTO updateProfile(@Valid @RequestBody UserProfileDTO profileDTO) {
        return profileService.updateProfile(getCurrentUsername(), profileDTO);
    }

    /**
     * Upload a picture as a multi-part file.
     *
     * @param pictureFile to be uploaded
     * @return the current user's updated profile.
     * @throws IOException
     */
    @RequestMapping(value = "/uploadPicture", method = RequestMethod.POST)
    @ResponseBody
    @JsonView(ProfileViews.ProfileFull.class)
    public UserProfileDTO profilePictureUpload(@RequestParam("file") MultipartFile pictureFile) throws IOException {
        byte[] pictureBytes = pictureFile.getBytes();
        String contentType = pictureFile.getContentType();
        return profileService.setProfilePicture(getCurrentUsername(), pictureBytes, contentType);
    }

    /**
     * Get a list of the current user's friends.
     *
     * @return a list of user profiles of the current user's friends.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/friends")
    @ResponseBody
    @JsonView(ProfileViews.ProfileChat.class)
    public List<UserProfileDTO> friends() {
        return profileService.getFriends(getCurrentUser());
    }
}