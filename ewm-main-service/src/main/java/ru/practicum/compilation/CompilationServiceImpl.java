package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.exception.ExceptionMessages;
import ru.practicum.util.PageSettings;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final  CompilationRepository compilationRepository;

    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CompilationDto create(NewCompilationDto compilationDto) {
        List<Event> eventList = eventRepository.findByIdIn(compilationDto.getEvents());
        Compilation compilation =
                compilationRepository.save(CompilationMapper.toCompilation(compilationDto, eventList));
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    @Transactional
    public CompilationDto update(Long compId, UpdateCompilationRequest updatedCompilation) {
        Compilation compilation = getCompilationById(compId);
        updateCompilationFields(compilation, updatedCompilation);
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    @Transactional
    public void delete(Long compId) {
        getCompilationById(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto getById(Long compId) {
        Compilation compilation = getCompilationById(compId);
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    public List<CompilationDto> get(Boolean pinned, Integer from, Integer size) {
        List<Compilation> compilationList;
        Pageable pageable = new PageSettings(from, size, CompilationRepository.SORT_CATEGORY_ID_DESC);
        if (pinned != null) {
            compilationList = compilationRepository.findByPinned(pinned, pageable);
        } else {
            compilationList = compilationRepository.findAll(pageable).getContent();
        }
        return compilationList.stream().map(CompilationMapper::toCompilationDto).collect(Collectors.toList());
    }

    @Transactional
    private void updateCompilationFields(Compilation compilation, UpdateCompilationRequest updatedCompilation) {
        Boolean pinned = updatedCompilation.getPinned();
        String title = updatedCompilation.getTitle();
        List<Long> events = updatedCompilation.getEvents();

        if (pinned != null) {
            compilation.setPinned(pinned);
        }

        if (title != null) {
            compilation.setTitle(title);
        }

        if (events != null && !events.isEmpty()) {
            compilation.setEvents(eventRepository.findByIdIn(events));
        }
    }

    @Transactional(readOnly = true)
    private Compilation getCompilationById(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.COMPILATION_NOT_FOUND));
    }
}
