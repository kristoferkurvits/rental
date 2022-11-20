package ee.kristofer.rental.serviceImpl;

import ee.kristofer.rental.exception.AuthorizationException;
import ee.kristofer.rental.exception.NotFoundException;
import ee.kristofer.rental.model.UserReservationsRequest;
import ee.kristofer.rental.repository.ReservationRepository;
import ee.kristofer.rental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

import static ee.kristofer.rental.util.ValidationUtil.validUser;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ReservationRepository reservationRepository;

    @Override
    public Map<String, Object> getReservations(UserReservationsRequest userReservationsRequest, int page, int size) {
        if (!validUser(userReservationsRequest.getUserId()))  {
            throw new AuthorizationException();
        }
        var reservations = reservationRepository
                .findByUserId(userReservationsRequest.getUserId(), PageRequest.of(page, size));
        var content = reservations.getContent();
        if(content.isEmpty()) {
            throw new NotFoundException();
        }
        return Map.of(
                "reservations", content,
                "currentPage", reservations.getNumber(),
                "totalItems", reservations.getTotalElements(),
                "totalPages", reservations.getTotalPages()
        );
    }
}
