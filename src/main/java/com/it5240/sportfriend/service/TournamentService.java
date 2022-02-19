package com.it5240.sportfriend.service;

import com.it5240.sportfriend.exception.InvalidExceptionFactory;
import com.it5240.sportfriend.exception.NotFoundExceptionFactory;
import com.it5240.sportfriend.model.exception.ExceptionType;
import com.it5240.sportfriend.model.unit.Media;
import com.it5240.sportfriend.model.unit.TournamentBase;
import com.it5240.sportfriend.model.unit.TournamentStatus;
import com.it5240.sportfriend.model.dto.tournament.ConfirmRequest;
import com.it5240.sportfriend.model.dto.tournament.ScheduleInfo;
import com.it5240.sportfriend.model.dto.tournament.TournamentResp;
import com.it5240.sportfriend.model.entity.Tournament;
import com.it5240.sportfriend.model.entity.UserTournament;
import com.it5240.sportfriend.repository.TournamentPostRepository;
import com.it5240.sportfriend.repository.TournamentRepository;
import com.it5240.sportfriend.repository.UserRepository;
import com.it5240.sportfriend.repository.UserTournamentRepository;
import com.it5240.sportfriend.utils.RespHelper;
import com.it5240.sportfriend.utils.Uploader;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TournamentService {
    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private UserTournamentRepository userTournamentRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private Uploader uploader;
    @Autowired
    private TournamentPostRepository tournamentPostRepository;
    @Autowired
    private UserRepository userRepository;

    public List<TournamentResp> getListTournament(ObjectId lastId, int size, TournamentStatus status, String meId){
        Pageable paging = PageRequest.of(0, size, Sort.by("id").descending());
        List<Tournament> tournaments = null;
        if(lastId == null){
            tournaments = tournamentRepository.findByStatus(status, paging);
        } else {
            tournaments = tournamentRepository.findByStatusAndIdLessThan(status, lastId, paging);
        }
        return tournaments.stream()
                .map(tournament -> tournament2Response(tournament, meId))
                .collect(Collectors.toList());
    }

    public List<TournamentResp> getListTournamentV2(ObjectId lastId, int size, TournamentStatus status, String meId){
        Pageable paging = PageRequest.of(0, size, Sort.by("id").descending());
        Optional<UserTournament> myTournamentOpt = userTournamentRepository.findById(meId);

        Set<ObjectId> notInIds = null;
        if(myTournamentOpt.isPresent()){
            notInIds = myTournamentOpt.get().getTournamentIdsJoined();
        } else {
            notInIds = new HashSet<>();
        }

        List<Tournament> tournaments = null;
        if(lastId == null){
            tournaments = tournamentRepository.findByStatusAndIdNotIn(status, new ArrayList<>(notInIds), paging);
        } else {
            tournaments = tournamentRepository.findByStatusAndIdLessThanAndIdNotIn(status, lastId, new ArrayList<>(notInIds), paging);
        }
        return tournaments.stream()
                .map(tournament -> tournament2Response(tournament, meId))
                .collect(Collectors.toList());
    }

    public List<TournamentResp> getMyListTournament(String meId){
        Optional<UserTournament> myTournamentOpt = userTournamentRepository.findById(meId);

        Set<ObjectId> myTournamentIds = null;
        if(myTournamentOpt.isEmpty()){
            myTournamentIds = new HashSet<>();
        } else {
            myTournamentIds = myTournamentOpt.get().getMyTournamentIds();
        }

        return tournamentRepository
                .findByIdIn(new ArrayList<>(myTournamentIds))
                .stream()
                .map(tournament -> tournament2Response(tournament, meId))
                .collect(Collectors.toList());
    }

    public List<TournamentResp> getMyListTournamentJoined(String meId){
        Optional<UserTournament> myTournamentOpt = userTournamentRepository.findById(meId);

        Set<ObjectId> myTournamentIdsJoined = null;
        if(myTournamentOpt.isEmpty()){
            myTournamentIdsJoined = new HashSet<>();
        } else {
            myTournamentIdsJoined = myTournamentOpt.get().getTournamentIdsJoined();
        }

        return tournamentRepository
                .findByIdIn(new ArrayList<>(myTournamentIdsJoined))
                .stream()
                .map(tournament -> tournament2Response(tournament, meId))
                .collect(Collectors.toList());
    }

    public TournamentResp getTournament(ObjectId tournamentId, String meId){
        Tournament tour = findById(tournamentId);
        return tournament2Response(tour, meId);
    }

    public TournamentResp createTournament(Tournament tournament, String meId){
        tournament.setOrganizationId(meId);
        tournament.setStatus(TournamentStatus.WAITING);

        tournament.setId(null);
        tournament.setBanner(null);
        tournament.setSchedules(null);
        tournament.setParticipantIds(new HashSet<>());
        tournament.setRequesterIds(new HashSet<>());

        tournament = tournamentRepository.save(tournament);

        UserTournament myUserTournament = userTournamentRepository.findById(meId).orElse(new UserTournament(meId));
        myUserTournament.getMyTournamentIds().add(tournament.getId());

        userTournamentRepository.save(myUserTournament);

        return tournament2Response(tournament, meId);
    }

    public Map<String, Object> deleteTournament(ObjectId tournamentId, String meId){
        Tournament tournament = findByIdAndCheckCanUpdate(tournamentId, meId);
        tournamentRepository.delete(tournament);
        return RespHelper.ok();
    }

    public Map<String, Object> requestToJoinTournament(ObjectId tournamentId, String meId){
        Tournament tournament = findById(tournamentId);
        tournament.getRequesterIds().add(meId);
        tournamentRepository.save(tournament);
        return RespHelper.ok();
    }

    public Map<String, Object> confirmRequest(ConfirmRequest confirmRequest, String meId){
        Tournament tournament = findByIdAndCheckCanUpdate(confirmRequest.getTournamentId(), meId);
        String joinerId = confirmRequest.getUserId();
        if(confirmRequest.isAgree()){
            tournament.getParticipantIds()
                    .add(joinerId);

            UserTournament myUserTournament = userTournamentRepository.findById(joinerId).orElse(new UserTournament(joinerId));
            myUserTournament.getTournamentIdsJoined().add(tournament.getId());
            userTournamentRepository.save(myUserTournament);
        }

        tournament.getRequesterIds().remove(joinerId);
        tournamentRepository.save(tournament);

        return RespHelper.ok();
    }

    public Media uploadBanner(MultipartFile bannerFile, ObjectId tournamentId, String meId) {
        Tournament tournament = findByIdAndCheckCanUpdate(tournamentId, meId);
        if(bannerFile == null || !bannerFile.getContentType().startsWith("image")){
            throw InvalidExceptionFactory.get(ExceptionType.FILE_IS_NOT_IMAGE);
        }

        Media banner = uploader.uploadImage(bannerFile, "image");
        if(tournament.getBanner() != null) {
            uploader.deleteFile(tournament.getBanner().getId());
        }
        tournament.setBanner(banner);
        tournamentRepository.save(tournament);

        return banner;
    }

    public TournamentResp updateInfo(TournamentBase newInfo, String meId){
        Tournament tournament = findByIdAndCheckCanUpdate(newInfo.getId(), meId);

        tournament.setName(newInfo.getName());
        tournament.setDetails(newInfo.getDetails());
        tournament.setStartTime(newInfo.getStartTime());
        tournament.setEndTime(newInfo.getEndTime());
        tournament.setStatus(newInfo.getStatus());

        tournament = tournamentRepository.save(tournament);
        return tournament2Response(tournament, meId);
    }

    public TournamentResp updateSchedule(ScheduleInfo scheduleInfo, String meId){
        Tournament tournament = findByIdAndCheckCanUpdate(scheduleInfo.getId(), meId);
        tournament.setSchedules(scheduleInfo.getSchedules());

        tournament = tournamentRepository.save(tournament);
        return tournament2Response(tournament, meId);
    }

    private TournamentResp tournament2Response(Tournament tournament, String meId){
        TournamentResp tourResp = mapper.map(tournament, TournamentResp.class);
        tourResp.setCanEdit(tournament.getOrganizationId().equals(meId));
        tourResp.setJoined(tournament.getParticipantIds().contains(meId));
        tourResp.setRequested(tournament.getRequesterIds().contains(meId));
        return tourResp;
    }

    private Tournament findByIdAndCheckCanUpdate(ObjectId tournamentId, String meId){
        Tournament tournament = findById(tournamentId);
        checkCanUpdate(tournament, meId);
        return tournament;
    }

    private void checkCanUpdate(Tournament tournament, String meId){
        if(!tournament.getOrganizationId().equals(meId)){
            throw InvalidExceptionFactory.get(ExceptionType.UNAUTHORIZED);
        }
    }

    private Tournament findById(ObjectId tournamentId){
        if(tournamentId == null) {
            throw InvalidExceptionFactory.get(ExceptionType.PARAMETER_NOT_ENOUGH);
        }
        return tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> NotFoundExceptionFactory.get(ExceptionType.TOURNAMENT_NOT_FOUND));
    }

}
