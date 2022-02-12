package com.it5240.sportfriendfinding.service.cosin.similarity;

import com.it5240.sportfriendfinding.model.dto.user.SearchInfo;
import com.it5240.sportfriendfinding.model.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CosineSimilarity {

    public List<User> sortByCosineSimilarity(List<User> users, SearchInfo searchInfo){
        BaseInfo baseInfo = new BaseInfo(searchInfo);

        UserWrapper targetUser = baseInfo.getTargetUserWrapper();

        List<UserWrapper> userCtns = new ArrayList<>();
        for(User u : users){
            UserWrapper userWrapper = new UserWrapper(u);
            baseInfo.caculateItems(userWrapper);
//            userWrapper.setSim(cosineSimilarity(userWrapper, targetUser));

            userCtns.add(userWrapper);
        }

//        targetUser.normalized();
        for(UserWrapper uw : userCtns){
//            uw.normalized();
            uw.setSim(cosineSimilarity(uw, targetUser));
        }

        // List<UserWrapper> uWs = userCtns.stream()
        //         .sorted(Comparator.comparingDouble(UserWrapper::getSim).reversed())
        //         .collect(Collectors.toList());

        // for(UserWrapper uw : uWs){
        //     System.out.println(uw.getSim());
        // }

        return userCtns.stream()
                .sorted(Comparator.comparingDouble(UserWrapper::getSim).reversed())
                .map(uw -> uw.getUser())
                .collect(Collectors.toList());
    }

    public double cosineSimilarity(UserWrapper u1, UserWrapper u2){
        double top = 0;
        for(int i=0; i<u1.getItems().length; i++){
            top += u1.getItem(i) * u2.getItem(i);
        }

        double bottom = u1.itemsVectorLength() * u2.itemsVectorLength();
        if(bottom == 0) return 0.01;

        return top/(bottom);
    }
}
