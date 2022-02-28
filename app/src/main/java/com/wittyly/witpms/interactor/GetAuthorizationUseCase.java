package com.wittyly.witpms.interactor;

import com.wittyly.witpms.model.Authorization;
import com.wittyly.witpms.repository.UserRepository;
import com.wittyly.witpms.util.Environment;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetAuthorizationUseCase extends UseCase<Authorization, Params> {

    private static String GRANT_TYPE = "password";

    private final UserRepository userRepository;

    @Inject
    public GetAuthorizationUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    Observable<Authorization> buildUseCaseObservable(Params params) {
        return userRepository.getAuthorization(
                Environment.GRANT_TYPE,
                Environment.CLIENT_ID,
                Environment.CLIENT_SECRET,
                params.getString("username"),
                params.getString("password"),
                params.getString("scope")
        );
    }
}