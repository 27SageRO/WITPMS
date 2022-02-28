package com.wittyly.witpms.interactor;

import com.wittyly.witpms.model.User;
import com.wittyly.witpms.repository.UserRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetUserByAccessUseCase extends UseCase<User, Params> {

    private final UserRepository userRepository;

    @Inject
    public GetUserByAccessUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    Observable<User> buildUseCaseObservable(Params params) {
        return userRepository.getUserByAccess();
    }
}
