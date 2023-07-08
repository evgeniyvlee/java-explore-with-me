package ru.practicum.compilation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.compilation.model.Compilation;
import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    Sort SORT_CATEGORY_ID_DESC = Sort.by(Sort.Direction.DESC, "id");

    List<Compilation> findByPinned(Boolean pinned, Pageable pageable);
}
