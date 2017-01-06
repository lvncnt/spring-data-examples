package example.springdata.rest.stores.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import example.springdata.rest.stores.service.Store;
import example.springdata.rest.stores.service.StoreRepository;

@Controller
public class HomeController {
	
	private static final String INDEX = "index"; 

	private static final Distance DEFAULT_DISTANCE = new Distance(1.0, Metrics.MILES); 
	private static final List<Distance> DISTANCES = Arrays.asList(
			new Distance(0.5, Metrics.MILES), 
			new Distance(1.0, Metrics.MILES), 
			new Distance(2.0, Metrics.MILES)); 
	
	private static final Map<String, Point> KNOWN_LOCATIONS; 
	static {
		Map<String, Point> locations = new HashMap<>(); 
		locations.put("Pivotal SF", new Point(-122.4041764, 37.7819286)); 
		locations.put("Timesquare NY", new Point(-73.995146, 40.740337)); 
		KNOWN_LOCATIONS = Collections.unmodifiableMap(locations); 
	}
	
	@Autowired
	private StoreRepository repository; 
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model, 
			@RequestParam Optional<Point> location, 
			@RequestParam Optional<Distance> distance, Pageable pageable){
	  
		model.addAttribute("distances", DISTANCES); 
		model.addAttribute("selectedDistance", distance.orElse(DEFAULT_DISTANCE)); 
		
		Point point = location.orElse(KNOWN_LOCATIONS.get("Timesquare NY")); 
		model.addAttribute("location", point); 
		model.addAttribute("locations", KNOWN_LOCATIONS); 
		
		Page<Store> stores = repository.findByAddressLocationNear(point, 
				distance.orElse(DEFAULT_DISTANCE), pageable); 
		model.addAttribute("stores", stores);
 
		ObjectMapper mapper = new ObjectMapper(); 
		String jsonInString;
		try {
			jsonInString = mapper.writeValueAsString(stores.getContent());
			model.addAttribute("jstores", jsonInString);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		 
		return INDEX;
	}

}
