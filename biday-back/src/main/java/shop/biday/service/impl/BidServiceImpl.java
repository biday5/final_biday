package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import shop.biday.model.document.BidDocument;
import shop.biday.model.domain.BidModel;
import shop.biday.model.dto.BidResponse;
import shop.biday.model.repository.BidRepository;
import shop.biday.service.BidService;

@Slf4j
@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;

    @Override
    public Mono<BidResponse> save(BidModel bid) {
        BidDocument bidDocument = BidDocument.builder()
                .auctionId(bid.auctionId())
                .userId(bid.userId())
                .currentBid(bid.currentBid())
                .build();

        return bidRepository.save(bidDocument)
                .flatMap(savedBid -> bidRepository.findFirstByAuctionIdSorted(bid.auctionId()))
                .log()
                .flatMap(findBid -> bidRepository.countByAuctionId(findBid.getAuctionId())
                        .log()
                        .map(count -> BidResponse.builder()
                                .auctionId(findBid.getAuctionId())
                                .currentBid(findBid.getCurrentBid())
                                .award(findBid.isAward())
                                .bidedAt(findBid.getBidedAt())
                                .count(count)
                                .build()
                        )
                );
    }

    @Override
    public Mono<BidDocument> findByAuctionId(Long auctionId) {
        return bidRepository.findFirstByAuctionIdOrderByCurrentBidDescAndBidedAtAsc(auctionId).log();
    }

    @Override
    public Mono<Boolean> updateAward(Long auctionId) {
        return bidRepository.updateAward(auctionId).log();
    }
}
