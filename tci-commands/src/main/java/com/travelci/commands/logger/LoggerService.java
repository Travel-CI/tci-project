package com.travelci.commands.logger;

import com.travelci.commands.logger.entities.BuildDto;

public interface LoggerService {

    BuildDto endBuildByError(final BuildDto build, final Exception exception);
}
