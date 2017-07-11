package com.travelci.logger.build;

import com.travelci.logger.build.entities.BuildAdapter;
import com.travelci.logger.build.entities.BuildDto;
import com.travelci.logger.build.exceptions.InvalidBuildException;
import com.travelci.logger.build.exceptions.NotFoundBuildException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.travelci.logger.build.entities.Status.*;

@Service
@Transactional
class BuildServiceImpl implements BuildService {

    private final BuildRepository buildRepository;
    private final BuildAdapter buildAdapter;

    BuildServiceImpl(final BuildRepository buildRepository, final BuildAdapter buildAdapter) {
        this.buildRepository = buildRepository;
        this.buildAdapter = buildAdapter;
    }

    @Override
    public BuildDto create(final BuildDto build) {

        build.setStatus(IN_PROGRESS);

        return buildAdapter.toBuildDto(
            buildRepository.save(buildAdapter.toBuild(build))
        );
    }

    @Override
    public BuildDto endBySuccess(final BuildDto build) {

        checkBuild(build);
        build.setStatus(SUCCESS);

        return buildAdapter.toBuildDto(
            buildRepository.save(buildAdapter.toBuild(build))
        );
    }

    @Override
    public BuildDto endByError(final BuildDto build) {

        checkBuild(build);
        build.setStatus(ERROR);

        return buildAdapter.toBuildDto(
            buildRepository.save(buildAdapter.toBuild(build))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<BuildDto> getBuildsByProjectId(final Long projectId) {
        return buildRepository.findByProjectIdOrderByIdDesc(projectId)
            .stream()
            .map(buildAdapter::toBuildDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BuildDto getLastBuildByProjectId(final Long projectId) {
        return buildAdapter.toBuildDto(
            buildRepository.findLastBuild(projectId)
                .orElseThrow(NotFoundBuildException::new)
        );
    }

    @Override
    public Long deleteBuildByProjectId(final Long projectId, final Long buildId) {
        return buildRepository.deleteByIdAndProjectId(buildId, projectId);
    }

    private void checkBuild(final BuildDto build) {

        buildRepository.findById(build.getId())
            .orElseThrow(NotFoundBuildException::new);

        if (build.getBuildEnd() == null)
            throw new InvalidBuildException();
    }
}
