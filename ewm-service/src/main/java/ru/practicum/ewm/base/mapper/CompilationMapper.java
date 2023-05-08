package ru.practicum.ewm.base.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.base.dto.compilation.CompilationDto;
import ru.practicum.ewm.base.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.base.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewm.base.model.Compilation;

@Mapper(componentModel = "spring", uses = {EventMapper.class})
public abstract class CompilationMapper {
    public Compilation toCompilation(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .pinned(newCompilationDto.getPinned() != null && newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }

    @Mapping(ignore = true, target = "events")
    public abstract Compilation toCompilation(UpdateCompilationRequest updateCompilationRequest);

    public abstract CompilationDto toCompilationDto(Compilation compilation);
}
