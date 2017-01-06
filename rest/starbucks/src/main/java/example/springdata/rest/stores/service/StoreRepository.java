package example.springdata.rest.stores.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for out-of-the-box paginating access to {@link Store}s and a query method to find stores by
 * location and distance.
 * 
 * @author Oliver Gierke
 */
@Repository
public interface StoreRepository extends PagingAndSortingRepository<Store, String>{

	@RestResource(rel = "by-location")
	Page<Store> findByAddressLocationNear(Point location, Distance distance, Pageable pageable); 
}
