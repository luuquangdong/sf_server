package com.it5240.sportfriendfinding.service;

import com.it5240.sportfriendfinding.exception.InvalidExceptionFactory;
import com.it5240.sportfriendfinding.exception.NotFoundExceptionFactory;
import com.it5240.sportfriendfinding.exception.model.ExceptionType;
import com.it5240.sportfriendfinding.model.atom.Media;
import com.it5240.sportfriendfinding.model.atom.TournamentBase;
import com.it5240.sportfriendfinding.model.atom.TournamentStatus;
import com.it5240.sportfriendfinding.model.dto.tournament.ConfirmRequest;
import com.it5240.sportfriendfinding.model.dto.tournament.ScheduleInfo;
import com.it5240.sportfriendfinding.model.dto.tournament.TournamentResp;
import com.it5240.sportfriendfinding.model.entity.Tournament;
import com.it5240.sportfriendfinding.repository.TournamentRepository;
import com.it5240.sportfriendfinding.utils.Uploader;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TournamentService {
    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private Uploader uploader;

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
        tournament.setParticipantIds(null);
        tournament.setRequesterIds(null);

        tournament = tournamentRepository.save(tournament);

        return tournament2Response(tournament, meId);
    }

    public String deleteTournament(ObjectId tournamentId, String meId){
        Tournament tournament = findByIdAndCheckCanUpdate(tournamentId, meId);
        tournamentRepository.delete(tournament);
        return "{\"status\":\"ok\"}";
    }

    public String requestToJoinTournament(ObjectId tournamentId, String meId){
        Tournament tournament = findById(tournamentId);
        tournament.getRequesterIds().add(meId);
        tournamentRepository.save(tournament);
        return "{\"status\":\"ok\"}";
    }

    public String confirmRequest(ConfirmRequest confirmRequest, String meId){
        Tournament tournament = findByIdAndCheckCanUpdate(confirmRequest.getTournamentId(), meId);
        String joinerId = confirmRequest.getUserId();
        if(confirmRequest.isAgree()){
            tournament.getParticipantIds()
                    .add(joinerId);
        }

        tournament.getRequesterIds().remove(joinerId);

        return "{\"status\":\"ok\"}";
    }

    public String uploadBanner(MultipartFile bannerFile, ObjectId tournamentId, String meId) {
        Tournament tournament = findByIdAndCheckCanUpdate(tournamentId, meId);
        if(bannerFile != null){
            Media banner = uploader.uploadImage(bannerFile, "image");
            if(tournament.getBanner() != null) {
                uploader.deleteFile(tournament.getBanner().getId());
            }
            tournament.setBanner(banner);
        }

        tournamentRepository.save(tournament);

        return "{\"status\":\"ok\"}";
    }

    public TournamentResp updateInfo(TournamentBase newInfo, String meId){
        Tournament tournament = findByIdAndCheckCanUpdate(newInfo.getId(), meId);

        tournament.setName(newInfo.getName());
        tournament.setDescription(newInfo.getDescription());
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
        tourResp.setOwner(tournament.getOrganizationId().equals(meId));
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
        return tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> NotFoundExceptionFactory.get(ExceptionType.TOURNAMENT_NOT_FOUND));
    }

}
