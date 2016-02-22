package io.github.fbiville.repositories;

import io.github.fbiville.domain.LameVid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "lame-vids", collectionResourceRel = "lame-vids")
public interface LameVids extends PagingAndSortingRepository<LameVid, Long> {

    @RestResource(path = "genre")
    Page<LameVid> findByGenreLabelLikeIgnoreCase(@Param("genre") String genre, Pageable paging);

    @RestResource(path = "title")
    Page<LameVid> findByTitleIgnoreCase(@Param("title") String title, Pageable paging);
}
