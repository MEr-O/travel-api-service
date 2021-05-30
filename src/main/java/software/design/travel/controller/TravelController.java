package software.design.travel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import software.design.travel.model.Place;
import software.design.travel.model.PlaceBook;
import software.design.travel.model.User;
import software.design.travel.model.enumType.ETag;
import software.design.travel.payload.request.BookPlaceRequest;
import software.design.travel.payload.response.PlaceListResponse;
import software.design.travel.payload.request.LoginRequest;
import software.design.travel.payload.request.SearchRequest;
import software.design.travel.payload.response.MessageResponse;
import software.design.travel.payload.response.PlaceDetailResponse;
import software.design.travel.payload.response.PlaceResponse;
import software.design.travel.payload.response.UserResponse;
import software.design.travel.repository.PlaceBookRepository;
import software.design.travel.repository.PlaceRepository;
import software.design.travel.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_OWNER') || hasRole('ROLE_CREATOR')")
@RequestMapping("/service")
public class TravelController {
    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PlaceBookRepository placeBookRepository;

    @PostMapping("/search")
    public ResponseEntity<?> searchByName(@RequestBody SearchRequest place) {
        PlaceResponse placeResponse = new PlaceResponse();

        try {
            if(place.getType() == null) {
                place.setType(ETag.PLACE);
            }

            List<Place> placeList = new ArrayList<>();
            switch (place.getType()) {
                case TAG:
                    placeList = placeRepository.findByTagLike(place.getQ());
                    break;
                case PLACE:
                default:
                    placeList = placeRepository.findByNameLike(place.getQ());
            }

            if (placeList != null && placeList.size() > 0) {
                placeResponse.setStatus(true);
                placeResponse.setMessage("success");
                placeResponse.setPlaces(placeList);
                placeResponse.setTotal(placeRepository.countByNameLike(place.getQ()));

                return ResponseEntity.status(HttpStatus.OK).body(placeResponse);
            } else {
                placeResponse.setStatus(false);
                placeResponse.setMessage("ไม่สถานที่ ที่ค้นหา");

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(placeResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            placeResponse.setStatus(false);
            placeResponse.setMessage("ไม่สถานที่ ที่ค้นหา");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(placeResponse);
        }
    }

    @PostMapping("/get-user")
    public ResponseEntity<?> getUser(@RequestBody LoginRequest loginRequest) {
        UserResponse userResponse = new UserResponse();
        try {
            User user = userRepository.findUserByUsername(loginRequest.getUsername());
            if(user != null) {
                UserResponse.User u = new UserResponse.User();
                u.setUsername(user.getUsername());
                u.setEmail(user.getEmail());
                u.setRoles(user.getRoles());
                u.setPlaces(user.getPlaces());
                u.setFirstname(user.getFirstname());
                u.setLastname(user.getLastname());
                u.setMobileNumber(user.getMobileNumber());
                u.setGender(user.getGender());

                userResponse.setStatus(true);
                userResponse.setMessage("Success");
                userResponse.setUser(u);

                return ResponseEntity.status(HttpStatus.OK).body(userResponse);
            } else {
                userResponse.setStatus(false);
                userResponse.setMessage("ไม่มี username นี้ในระบบ");

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
            }
        } catch (Exception e) {
            userResponse.setStatus(false);
            userResponse.setMessage("ไม่มี username นี้ในระบบ");
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }


    @PostMapping("/get-suggestion-place")
    public ResponseEntity<?> getSuggestionPlace() {
        PlaceListResponse placeListResponse = new PlaceListResponse();

        try {
            List<Place> p = placeRepository.findAllBySuggestIsTrue();
            if(p != null) {
                placeListResponse.setStatus(true);
                placeListResponse.setMessage("Success");
                placeListResponse.setPlaces(p);

                return ResponseEntity.status(HttpStatus.OK).body(placeListResponse);
            } else {
                placeListResponse.setStatus(false);
                placeListResponse.setMessage("ไม่เจอสถานที่ที่แนะนำ");

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(placeListResponse);
            }
        } catch (Exception e) {
            placeListResponse.setStatus(false);
            placeListResponse.setMessage("ไม่เจอสถานที่ที่แนะนำ");
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(placeListResponse);
        }
    }

    @PostMapping("/get-popular-place")
    public ResponseEntity<?> getPopularPlace() {
        PlaceListResponse placeListResponse = new PlaceListResponse();

        try {
            List<Place> p = placeRepository.findAllByPopularIsTrue();
            if(p != null) {
                placeListResponse.setStatus(true);
                placeListResponse.setMessage("Success");
                placeListResponse.setPlaces(p);

                return ResponseEntity.status(HttpStatus.OK).body(placeListResponse);
            } else {
                placeListResponse.setStatus(false);
                placeListResponse.setMessage("ไม่เจอสถานที่ยอดนิยม");

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(placeListResponse);
            }
        } catch (Exception e) {
            placeListResponse.setStatus(false);
            placeListResponse.setMessage("ไม่เจอสถานที่ยอดนิยม");
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(placeListResponse);
        }
    }

    @PostMapping("/get-place")
    public ResponseEntity<?> getPlace(@RequestBody Place place) {
        PlaceDetailResponse placeDetailResponse = new PlaceDetailResponse();

        try {
            Place p = placeRepository.findPlaceById(place.getId());
            if(p != null) {
                placeDetailResponse.setStatus(true);
                placeDetailResponse.setMessage("Success");
                placeDetailResponse.setPlace(p);

                return ResponseEntity.status(HttpStatus.OK).body(placeDetailResponse);
            } else {
                placeDetailResponse.setStatus(false);
                placeDetailResponse.setMessage("ไม่มีสถานที่นี้ในระบบ");

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(placeDetailResponse);
            }
        } catch (Exception e) {
            placeDetailResponse.setStatus(false);
            placeDetailResponse.setMessage("ไม่มีสถานที่นี้ในระบบ");
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(placeDetailResponse);
        }
    }


    @DeleteMapping("/book-place")
    public ResponseEntity<?> removeBookPlace(@RequestParam String placeId, @RequestParam String username) {
        MessageResponse messageResponse = new MessageResponse("ทำรายการสำเร็จ");
        try {
            User user = userRepository.findUserByUsername(username);

            if(user != null) {
                for(int i=0; i<user.getPlaces().size(); i++) {
                    if(user.getPlaces().get(i).getId().equalsIgnoreCase(placeId)) {
                        user.getPlaces().remove(i);
                        i--;
                    }
                }

                userRepository.save(user);
                messageResponse.setMessage("ลบสถานที่เรียบร้อย");
                messageResponse.setStatus(true);

                return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
            } else {
                messageResponse.setMessage("ไม่มีสถานที่นี้ในระบบ");
                messageResponse.setStatus(false);

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageResponse);
            }

        } catch (Exception e) {
            messageResponse.setMessage("ทำรายการไม่สำเร็จกรุณาติดต่อเจ้าของระบบ");
            messageResponse.setStatus(false);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageResponse);
        }
    }

    @PostMapping("/book-place")
    public ResponseEntity<?> bookPlace(@RequestBody BookPlaceRequest bookPlaceRequest) {
        MessageResponse messageResponse = new MessageResponse("ทำรายการสำเร็จ");

        try {
            User user = userRepository.findUserByUsername(bookPlaceRequest.getUsername());
            List<PlaceBook> places = user.getPlaces();

            Place place = placeRepository.findPlaceById(bookPlaceRequest.getPlaceId());
            if(place == null) {
                messageResponse.setMessage("ไม่มีสถานที่นี้ในระบบ");
                messageResponse.setStatus(false);

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageResponse);
            }

            boolean dup = false;
            for(int i=0; i<places.size(); i++) {
                if(place.getName().equalsIgnoreCase(places.get(i).getName())) {
                    for(int j=0; j < user.getPlaces().size(); j++) {
                        if(user.getPlaces().get(j).getDate().equalsIgnoreCase(bookPlaceRequest.getDate())) {
                            dup = true;
                            break;
                        }
                    }
                }
            }

            if(!dup) {
                PlaceBook placeBook = new PlaceBook();
                placeBook.setImg(place.getImg());
                placeBook.setName(place.getName());
                placeBook.setLocation(place.getLocation());
                placeBook.setTag(place.getTag());
                placeBook.setRating(place.getRating());
                placeBook.setDescription(place.getDescription());
                placeBook.setDescription(place.getDescription());
                placeBook.setFullprice(place.getFullprice());
                placeBook.setCurrentprice(place.getCurrentprice());
                placeBook.setTicket(bookPlaceRequest.getTicket());
                placeBook.setDate(bookPlaceRequest.getDate());
                placeBookRepository.save(placeBook);

                places.add(placeBook);
                user.setPlaces(places);
                userRepository.save(user);
                messageResponse.setMessage("ทำรายการสำเร็จ");
                messageResponse.setStatus(true);

                return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
            } else {
                messageResponse.setMessage("สถานที่นี้เคยทำการจองแล้ว");
                messageResponse.setStatus(false);

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageResponse);
            }

        } catch (Exception e) {
            messageResponse.setMessage("ทำรายการไม่สำเร็จกรุณาติดต่อเจ้าของระบบ");
            messageResponse.setStatus(false);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageResponse);
        }
    }
}