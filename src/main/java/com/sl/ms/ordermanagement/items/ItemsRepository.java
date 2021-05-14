package com.sl.ms.ordermanagement.items;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface ItemsRepository extends JpaRepository<Items, Long>{
	public Optional<Items> findById(Long Id);
}
