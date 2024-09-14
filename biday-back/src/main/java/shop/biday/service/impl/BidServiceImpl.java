package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.BidModel;
import shop.biday.model.dto.BidDto;
import shop.biday.model.repository.BidRepository;
import shop.biday.model.repository.QBidRepository;
import shop.biday.service.BidService;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {
    private final BidRepository repository;
    private final QBidRepository bidRepository;

    @Override
    public BidModel findById(Long id) {
        try {
            return bidRepository.findById(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Slice<BidDto> findByUser(Long userId, String period, LocalDateTime cursor, Pageable pageable) {
        try {
            return bidRepository.findByUserId(userId, period, cursor, pageable);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
