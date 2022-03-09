package com.it5240.sportfriend.service.cosin.similarity;

import com.it5240.sportfriend.model.dto.user.SearchInfo;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Getter
public class BaseInfo {
    private Info info;
    private int[] onehot;
    private final int[] coefficient = new int[] {4, 4, 3, 2, 2};

    public BaseInfo(SearchInfo searchInfo){
        info = new Info(searchInfo);
        onehot = new int[5];
        if(!info.getName().isBlank()) onehot[0] = 1;
        if(info.getSportIds().size() != 0) onehot[1] = 1;
        if(info.getLocation() != null && info.getLocation().getDistrict() != null) onehot[2] = 1;
        if(info.getGender() != null) onehot[3] = 1;
        onehot[4] = 1;
    }

    public UserWrapper getTargetUserWrapper(){
        UserWrapper targetUser = new UserWrapper();
        double[] items = new double[5];
        for(int i=0; i<items.length; i++){
            items[i] = onehot[i] * coefficient[i];
        }
        targetUser.setItems(items);
        return targetUser;
    }

    public void caculateItems(UserWrapper userWrapper){
        double[] items = new double[5];
        for(int i=0; i<onehot.length; i++){
            if(onehot[i] == 0) continue;
            items[i] = distance(i, userWrapper) * coefficient[i];
        }
        userWrapper.setItems(items);
    }

    private double distance(int i, UserWrapper userCtn){
        if(i == 0){
            if(userCtn.getUser().getName().toLowerCase(Locale.ROOT)
                    .contains(info.getName().toLowerCase(Locale.ROOT))){
                return 1.0;
            }
        }
        if(i == 1){
            Set<ObjectId> intersectionSportIds = new HashSet<>(info.getSportIds());
            intersectionSportIds.retainAll(userCtn.getUser().getSportIds());
            return 1.0 * intersectionSportIds.size()/info.getSportIds().size();
        }
        if(i == 2){
            if (info.getLocation().getDistrict().equals(
                    userCtn.getUser().getLocation().getDistrict()
            )){
                return 1;
            }
        }
        if(i == 3) {
            if(info.getGender().equals(userCtn.getUser().getGender())){
                return 1;
            }
        }
        if(i == 4) {
            LocalDate userBirthDay = userCtn.getUser().getBirthday();
            if(userBirthDay == null) return 0;
            int delta = Math.abs(calculateAge(userBirthDay, LocalDate.now()) - info.getAge());
            if(delta > 30) delta = 30;
            return 1.0 *(30 - delta)/30;
        }
        return 0;
    }

    public int calculateAge(LocalDate birthDate, LocalDate currentDate) {
        if ((birthDate != null) && (currentDate != null)) {
            return Period.between(birthDate, currentDate).getYears();
        } else {
            return 0;
        }
    }

}
