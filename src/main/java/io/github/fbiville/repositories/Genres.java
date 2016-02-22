package io.github.fbiville.repositories;

import io.github.fbiville.domain.Genre;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "genres")
public interface Genres extends PagingAndSortingRepository<Genre, Long> {
}
