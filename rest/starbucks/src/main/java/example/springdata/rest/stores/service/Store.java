
/*
 * Copyright 2014-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.springdata.rest.stores.service;

import static org.springframework.data.mongodb.core.index.GeoSpatialIndexType.GEO_2DSPHERE;

import java.util.UUID;

import lombok.Value;

import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entity to represent a {@link Store}.
 * 
 * @author Oliver Gierke
 */
@Value
@Document
public class Store {

	public @Id UUID id = UUID.randomUUID();
	public String name;
	public Address address;
 
	public Store(String name, Address address) {
		this.name = name;
		this.address = address;
	}
	 

	/**
	 * Value object to represent an {@link Address}.
	 * 
	 * @author Oliver Gierke
	 */
	@Value
	public static class Address {

		String street, city, zip;
		@GeoSpatialIndexed(type = GEO_2DSPHERE) Point location;
	 
		public Address(String street, String city, String zip, Point location) {
			this.street = street;
			this.city = city;
			this.zip = zip;
			this.location = location;
		}
 
		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */ 
		public String toString() {
			return String.format("%s, %s %s", street, zip, city);
		}

 
	} 
}
