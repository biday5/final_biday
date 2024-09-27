package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.ShipperModel;
import shop.biday.model.entity.ShipperEntity;
import shop.biday.model.repository.ShipperRepository;
import shop.biday.model.repository.UserRepository;
import shop.biday.oauth2.jwt.JWTUtil;
import shop.biday.service.ShipperService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShipperServiceImpl implements ShipperService {
    private final ShipperRepository shipperRepository;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public List<ShipperEntity> findAll() {
        log.info("Find all shippers");
        return shipperRepository.findAll();
    }

    @Override
    public Optional<ShipperEntity> findById(Long id) {
        log.info("Find shipper by id: {}", id);
        return Optional.of(id)
                .filter(t -> {
                    boolean exists = shipperRepository.existsById(id);
                    if (!exists) {
                        log.error("Not found shipper: {}", id);
                    }
                    return exists;
                })
                .flatMap(shipperRepository::findById);
    }

    @Override
    public ShipperEntity save(String token, ShipperModel shipper) {
        log.info("Save shipper started");
        return validateUser(token)
                .map(t -> shipperRepository.save(ShipperEntity.builder()
                        .paymentId(shipper.getPaymentId())
                        .carrier(shipper.getCarrier())
                        .trackingNumber(shipper.getTrackingNumber())
                        .shipmentDate(shipper.getShipmentDate())
                        .estimatedDeliveryDate(shipper.getEstimatedDeliveryDate())
                        .deliveryAddress(shipper.getDeliveryAddress())
                        .status("준비중")
                        .deliveryAddress(shipper.getDeliveryAddress())
                        .createdAt(LocalDateTime.now())
                        .build()))
                .orElseThrow(() -> new RuntimeException("Save shipper failed"));
    }

    @Override
    public ShipperEntity update(String token, ShipperModel shipper) {
        log.info("Update shipper started");
        return validateUser(token)
                .filter(t -> {
                    boolean exists = shipperRepository.existsById(shipper.getId());
                    if (!exists) {
                        log.error("Not found shipper: {}", shipper.getId());
                    }
                    return exists;
                })
                .map(t -> shipperRepository.save(ShipperEntity.builder()
                        .id(shipper.getId())
                        .paymentId(shipper.getPaymentId())
                        .carrier(shipper.getCarrier())
                        .trackingNumber(shipper.getTrackingNumber())
                        .shipmentDate(shipper.getShipmentDate())
                        .estimatedDeliveryDate(shipper.getEstimatedDeliveryDate())
                        .deliveryAddress(shipper.getDeliveryAddress())
                        .status(shipper.getStatus())
                        .deliveryAddress(shipper.getDeliveryAddress())
                        .createdAt(shipper.getCreatedAt())
                        .updatedAt(LocalDateTime.now())
                        .build()))
                .orElseThrow(() -> new RuntimeException("Update shipper failed: shipper not found"));
    }

    @Override
    public void deleteById(String token, Long id) {
        log.info("Delete shipper started for id: {}", id);
        validateUser(token)
                .filter(t -> {
                    boolean exists = shipperRepository.existsById(id);
                    if (!exists) {
                        log.error("Not found shipper: {}", id);
                    }
                    return exists;
                })
                .ifPresentOrElse(t -> {
                    shipperRepository.deleteById(id);
                    log.info("shipper deleted: {}", id);
                }, () -> log.error("User does not have role SELLER or does not exist"));
    }

    private Optional<String> validateUser(String token) {
        /* TODO 휘재형이 뽑는거 따로 가져오게 되면 JwtClaims claims = jwtUtil.extractClaims(token); 으로 정보 담아서 String userId=claims.getUserId(); 이런식으로 userId 뽑아서 사용할 것*/
        log.info("Validate User started");
        return Optional.of(token)
                .filter(t -> jwtUtil.getRole(t).equalsIgnoreCase("ROLE_SELLER"))
                .filter(t -> userRepository.existsByEmail(jwtUtil.getEmail(t)))
                .or(() -> {
                    log.error("User does not have role SELLER or does not exist");
                    return Optional.empty();
                });
    }
}
