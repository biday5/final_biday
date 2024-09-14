package shop.biday.service;

public interface WishService {
    boolean toggleWIsh(Long userId, Long productId);
    void deleteWish(Long userId, Long productId);
    void insertWish(Long userId, Long productId);
    boolean isWish(Long userId, Long productId);

}
