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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.separator.DefaultRecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import example.springdata.rest.stores.service.Store.Address;

/**
 * Component initializing a hand full of Starbucks stores and persisting them through a {@link StoreRepository}.
 * 
 * @author Oliver Gierke
 */
@Component
public class StoreInitializer {
	
	private static final Logger log = LoggerFactory.getLogger(StoreInitializer.class);

	@Autowired
	public StoreInitializer(StoreRepository repository, 
			MongoOperations operations){
		if(repository.count() != 0){
			return; 
		}
	 
		List<Store> stores = readStore(); 
		log.info("Importing {} stores into MongoDB…", stores.size());
		repository.save(stores); 
		log.info("Successfully imported {} stores.", repository.count());
	}

	/**
	 * Reads a file {@code starbucks.csv} from the class path and parses it into {@link Store} instances about to
	 * persisted.
	 * 
	 * @return
	 */ 
	public static List<Store> readStore(){
		
		List<Store> stores = new ArrayList<>(); 
		Store store = null; 
		
		ClassPathResource resource = new ClassPathResource("starbucks.csv"); 
		Scanner scanner;
		try {
			scanner = new Scanner(resource.getInputStream());
			String line = scanner.nextLine(); 
			scanner.close();
			
			FlatFileItemReader<Store> reader = new FlatFileItemReader<>(); 
			reader.setResource(resource);
			
			DelimitedLineTokenizer tokenizer = 
					new DelimitedLineTokenizer(DelimitedLineTokenizer.DELIMITER_COMMA); 
			tokenizer.setNames(line.split(","));
			tokenizer.setStrict(false);
			
			DefaultLineMapper<Store> lineMapper = new DefaultLineMapper<Store>(); 
			lineMapper.setLineTokenizer(tokenizer);
			lineMapper.setFieldSetMapper(fieldSet -> {
				Point location = new Point(fieldSet.readDouble("Longitude"), 
						fieldSet.readDouble("Latitude")); 
				Address address = new Address(fieldSet.readString("Street Address"), 
						fieldSet.readString("City"), 
						fieldSet.readString("Zip"), location); 
				return new Store(fieldSet.readString("Name"), address); 
			});

			reader.setLineMapper(lineMapper);
			reader.setRecordSeparatorPolicy(new DefaultRecordSeparatorPolicy());
			reader.setLinesToSkip(1);
			reader.open(new ExecutionContext());
			
			do{
				store = reader.read(); 
				if(store != null){
					stores.add(store); 
				}
			}while(store != null); 
			 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnexpectedInputException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
 
		return stores; 
	} 

}
