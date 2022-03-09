package com.it5240.sportfriend.service;

import com.it5240.sportfriend.exception.InvalidExceptionFactory;
import com.it5240.sportfriend.exception.NotFoundExceptionFactory;
import com.it5240.sportfriend.model.dto.auth.ChangePasswordInfo;
import com.it5240.sportfriend.model.dto.user.SearchInfo;
import com.it5240.sportfriend.model.dto.user.UserReq;
import com.it5240.sportfriend.model.dto.user.UserResp;
import com.it5240.sportfriend.model.entity.FriendRequest;
import com.it5240.sportfriend.model.entity.SignupOrganization;
import com.it5240.sportfriend.model.entity.User;
import com.it5240.sportfriend.model.exception.ExceptionType;
import com.it5240.sportfriend.model.unit.Media;
import com.it5240.sportfriend.model.unit.Role;
import com.it5240.sportfriend.repository.FriendRequestRepository;
import com.it5240.sportfriend.repository.SignupOrganizationRepository;
import com.it5240.sportfriend.repository.UserRepository;
import com.it5240.sportfriend.repository.dao.UserDao;
import com.it5240.sportfriend.service.cosin.similarity.CosineSimilarity;
import com.it5240.sportfriend.utils.NotificationUtil;
import com.it5240.sportfriend.utils.RespHelper;
import com.it5240.sportfriend.utils.Uploader;
import com.it5240.sportfriend.utils.UserUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Uploader uploader;
    @Autowired
    private UserUtil userHelper;
    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private CosineSimilarity cosineSimilarity;
    @Autowired
    private SignupOrganizationRepository signupOrganizationRepository;
    @Autowired
    private NotificationUtil notificationUtil;

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public User banUser(String userId, boolean banned){
        User user = findById(userId);
        user.setBanned(banned);
        user = userRepository.save(user);
        return user;
    }

    public UserResp getUser(String id, String meId){
        User user = findById(id);
        UserResp resp = userHelper.toUserResp(user);

        if(id.equals(meId)){
            resp.setCanEdit(true);
            return resp;
        }
        
        if(user.getFriendIds().contains(meId)){
            resp.setFriend(true);
            return resp;
        }

        Optional<FriendRequest> frOpt = friendRequestRepository.findByUserIdSentAndUserIdReceive(meId, id);
        if(frOpt.isPresent()){
            resp.setRequestedFriend(true);
        }

        return resp;
    }

    public Media updateAvatar(MultipartFile avatarFile, String meId){
        User user = userRepository.findById(meId).get();
        if(avatarFile == null || !avatarFile.getContentType().startsWith("image")){
            throw InvalidExceptionFactory.get(ExceptionType.FILE_IS_NOT_IMAGE);
        }
        Media avatar = uploader.uploadImage(avatarFile, "avatar");
        if(user.getAvatar() != null) {
            uploader.deleteFile(user.getAvatar().getId());
        }
        user.setAvatar(avatar);
        userRepository.save(user);
        return avatar;
    }

    public UserResp updateUserInfo(UserReq newUserInfo, String phoneNumber){
        if(!newUserInfo.getPhoneNumber().equals(phoneNumber)){
           throw InvalidExceptionFactory.get(ExceptionType.UNAUTHORIZED);
        }

        User user = findById(phoneNumber);
        user.updateInfo(newUserInfo);

        User userUpdated = userRepository.save(user);

        return userHelper.toUserRespV2(userUpdated, phoneNumber);
    }

    // public UserResp updateUserInfo(UserReq newUserInfo, String phoneNumber){
    //     ...
    //     User user = userRepository.findById(phoneNumber)
    //             .orElseThrow(() -> new NotFoundException("User is not existed", 1014));
    //     ...
    // }

    // public UserResp updateUserInfo(UserReq newUserInfo, String phoneNumber){
    //     ...
    //     User user = userRepository.findById(phoneNumber)
    //             .orElseThrow(() -> NotFoundExceptionFactory.get(ExceptionType.USER_NOT_FOUND));
    //     ...
    // }

    // public Map<String, Object> changePassword(ChangePasswordInfo info, String phoneNumber){
    //     User user = userRepository.findById(phoneNumber)
    //             .orElseThrow(() -> new NotFoundException("User is not existed", 1014));
    //     ...
    // }

    public List<User> getListUser(List<String> userIds){
        List<User> result = userRepository.findByPhoneNumberIn(userIds);
        return result;
    }

    public Map<String, Object> changePassword(ChangePasswordInfo info, String phoneNumber){
        User user = findById(phoneNumber);

        // if(!user.getPassword().equals(passwordEncoder.encode(info.getOldPassword()))){
        //     throw InvalidExceptionFactory.get(ExceptionType.PASSWORD_NOT_MATCH);
        // }

        if(!passwordEncoder.matches(info.getOldPassword(), user.getPassword())){
            throw InvalidExceptionFactory.get(ExceptionType.PASSWORD_NOT_MATCH);
        }

        user.setPassword(passwordEncoder.encode(info.getPassword()));

        userRepository.save(user);

        return RespHelper.ok();
    }

    public List<UserResp> suggestFriends(SearchInfo searchInfo, String meId){
        User me = userRepository.findById(meId).get();

        Set<String> notIds = me.getFriendIds();
        notIds.add(meId);

        List<User> users = userDao.suggestFriends(searchInfo, notIds);
        users = cosineSimilarity.sortByCosineSimilarity(users, searchInfo);

        return users.stream()
                .map(user -> userHelper.toUserResp(user))
                .skip(searchInfo.getIndex())
                .limit(searchInfo.getSize())
                .collect(Collectors.toList());
    }

    public Map<String, Object> signupOrganization(MultipartFile idCardFile, String meId){
        Media idCard = uploader.uploadImage(idCardFile, "image");

        SignupOrganization signupOrganization = new SignupOrganization();
        signupOrganization.setUserId(meId);
        signupOrganization.setIdCard(idCard);

        signupOrganizationRepository.save(signupOrganization);

        return RespHelper.ok();
    }

    public Map<String, Object> confirmOrganization(ObjectId signupId, boolean value){
        SignupOrganization signupOrganization = signupOrganizationRepository
                .findById(signupId)
                .orElseThrow(() -> NotFoundExceptionFactory.get(ExceptionType.SIGNUP_NOT_FOUND));

        User user = findById(signupOrganization.getUserId());

        if(value){
            user.setRole(Role.ROLE_ORGANIZATION);
            userRepository.save(user);

            notificationUtil.sendNotificationToUser(user, "Kết quả đăng ký tổ chức", "Bạn được chấp nhận là tổ chức");
        } else {
            notificationUtil.sendNotificationToUser(user, "Kết quả đăng ký tổ chức", "Từ chối");
        }

        signupOrganizationRepository.deleteById(signupId);

        return RespHelper.ok();
    }

    public List<SignupOrganization> getAllSignupOrg() {
        return signupOrganizationRepository.findAll(Sort.by("id").descending());
    }

    private User findById(String userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> NotFoundExceptionFactory.get(ExceptionType.USER_NOT_FOUND));
    }

    public Map<String, Object> setPushToken(String token, String meId){
        User user = userRepository.findById(meId).get();
        user.setPushToken(token);
        userRepository.save(user);
        return RespHelper.ok();
    }

    public Map<String, Object> removePushToken(String meId){
        User user = userRepository.findById(meId).get();
        user.setPushToken(null);
        userRepository.save(user);
        return RespHelper.ok();
    }
}
