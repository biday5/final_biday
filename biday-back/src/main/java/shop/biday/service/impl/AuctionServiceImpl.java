package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.AuctionModel;
import shop.biday.model.dto.AuctionDto;
import shop.biday.model.entity.AuctionEntity;
import shop.biday.model.repository.AuctionRepository;
import shop.biday.model.repository.QAuctionRepository;
import shop.biday.service.AuctionService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {
    private final AuctionRepository repository;
    private final QAuctionRepository auctionRepository;

    @Override
    public Slice<AuctionDto> findByUser(Long userId, String period, LocalDateTime cursor, Pageable pageable) {
        try{
            return auctionRepository.findByUser(userId, period, cursor, pageable);
        } catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public AuctionModel findById(Long id) {
        try {
            return auctionRepository.findById(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean existsById(Long id) {
        try {
            return repository.existsById(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public AuctionEntity save(AuctionModel auction) {
        try {
            return repository.save(auction);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public AuctionEntity update(AuctionModel auction) {
        try {
            if(existsById(auction.getId())) {
                return repository.save(auction);
            } else return null;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
