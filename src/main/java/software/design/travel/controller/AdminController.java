package software.design.travel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import software.design.travel.model.Image;
import software.design.travel.model.Place;
import software.design.travel.payload.request.PlaceRequest;
import software.design.travel.payload.response.PlaceResponse;
import software.design.travel.payload.response.UploadFileResponse;
import software.design.travel.repository.ImageRepository;
import software.design.travel.repository.PlaceRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    FileController fileController;

    @GetMapping("/add-place")
    public ModelAndView addPlace() {
        ModelAndView modelAndView = new ModelAndView("/pages/addPlace.html");
        PlaceRequest placeRequest = new PlaceRequest();
        modelAndView.addObject("placeRequest", placeRequest);

        PlaceResponse places = new PlaceResponse();
        places.setPlaces(placeRepository.findAll());
        places.setTotal(placeRepository.count());
        modelAndView.addObject("places", places);

        return modelAndView;
    }

    @GetMapping("/edit-place")
    public ModelAndView addPlace(@RequestParam String id) {
        ModelAndView modelAndView = new ModelAndView("/pages/editPlace.html");

        Place p = placeRepository.findPlaceById(id);
        PlaceRequest placeRequest = new PlaceRequest();

        placeRequest.setPlace(p);
        modelAndView.addObject("placeRequest", placeRequest);
        return modelAndView;
    }

    @RequestMapping(value = "save-place", method = RequestMethod.POST)
    public ModelAndView savePlace(@ModelAttribute PlaceRequest placeRequest) {
        Place place = placeRequest.getPlace();

        try {
            //save asset
            if(placeRequest.getPlace().getId() != null) {
                place = placeRepository.findPlaceById(placeRequest.getPlace().getId());

                if(placeRequest.getMultipartFilesDocument() != null && !"".equals(placeRequest.getMultipartFilesDocument().getOriginalFilename())) {
                    String originFilename = placeRequest.getMultipartFilesDocument().getOriginalFilename();
                    Image image = new Image();
                    image.setOriginName(originFilename);
                    image = imageRepository.save(image);
                    String filename = image.getId() + originFilename.substring(originFilename.indexOf(".")); // Give a random filename here.
                    UploadFileResponse uploadFileResponse = fileController.uploadFile(placeRequest.getMultipartFilesDocument(), filename);
                    image.setImagePath("/get-image/" + filename);
                    image = imageRepository.save(image);

                    placeRequest.getPlace().setImg(image);
                } else {
                    placeRequest.getPlace().setImg(place.getImg());
                }
                placeRepository.save(placeRequest.getPlace());
            } else {
                if(placeRequest.getMultipartFilesDocument() != null && !"".equals(placeRequest.getMultipartFilesDocument().getOriginalFilename())) {
                    String originFilename = placeRequest.getMultipartFilesDocument().getOriginalFilename();
                    Image image = new Image();
                    image.setOriginName(originFilename);
                    image = imageRepository.save(image);
                    String filename = image.getId() + originFilename.substring(originFilename.indexOf(".")); // Give a random filename here.
                    UploadFileResponse uploadFileResponse = fileController.uploadFile(placeRequest.getMultipartFilesDocument(), filename);
                    image.setImagePath("/get-image/" + filename);
                    image = imageRepository.save(image);

                    place.setImg(image);
                }
                placeRepository.save(place);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ModelAndView modelAndView = new ModelAndView("redirect:/admin/add-place");
        return modelAndView;
    }
}